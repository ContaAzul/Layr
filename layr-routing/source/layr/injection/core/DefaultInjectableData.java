package layr.injection.core;

import java.util.List;
import java.util.Map;

import layr.api.routing.DataProvider;
import layr.api.routing.ExceptionHandler;
import layr.api.routing.InjectableData;
import layr.api.routing.InjectionException;
import layr.api.routing.InputConverter;
import layr.api.routing.OutputRenderer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class DefaultInjectableData implements InjectableData {

	final ClassInstantiationHandlers handlers;
	final Map<String, Class<? extends InputConverter>> inputConverters;
	final Map<String, Class<? extends OutputRenderer>> outputRenderers;
	final Map<Class<?>, Class<? extends DataProvider<?>>> dataProviders;
	final Map<Class<?>, Class<? extends ExceptionHandler<?>>> exceptionHandlers;
	final List<Class<?>> webResources;

	@Override
	public InputConverter inputConverterFor( String contentType ) throws InjectionException {
		Class<? extends InputConverter> clazz = this.inputConverters.get( contentType );
		if ( clazz != null )
			return instantiate( clazz );
		throw new InjectionException( "No InputConverter defined for " + contentType );
	}

	@Override
	public OutputRenderer outputRendererFor(String contentType) throws InjectionException {
		Class<? extends OutputRenderer> clazz = this.outputRenderers.get( contentType );
		if ( clazz != null )
			return instantiate( clazz );
		throw new InjectionException( "No OutputRenderer defined for " + contentType );
	}

	@Override
	public DataProvider<?> dataProviderFor( Class<?> targetClass ) throws InjectionException {
		for ( Class<?> clazz : this.dataProviders.keySet() )
			if ( clazz.isAssignableFrom( targetClass ) )
				return instantiate( this.dataProviders.get( clazz ) );
		throw new InjectionException( "No DataProvider defined for " + targetClass.getCanonicalName() );
	}

	@Override
	@SuppressWarnings("unchecked")
	public ExceptionHandler<Throwable> exceptionHandlerFor( Throwable targetClass ) throws InjectionException {
		for ( Class<?> clazz : this.exceptionHandlers.keySet() )
			if ( clazz.isAssignableFrom( targetClass.getClass() ) )
				return (ExceptionHandler<Throwable>) instantiate( this.exceptionHandlers.get( clazz ) );
		throw new InjectionException( "No ExceptionHandler defined for " + targetClass.getClass().getCanonicalName(), targetClass );
	}

	@Override
	public <T> T instantiate( Class<T> targetClazz ) throws InjectionException {
		ClassInstantiationHandler<?> instantiationHandler = this.handlers.forType( targetClazz );
		return instantiationHandler.newInstance( targetClazz );
	}
}
