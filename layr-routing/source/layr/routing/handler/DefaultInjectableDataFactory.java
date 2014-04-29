package layr.routing.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layr.api.routing.ContentType;
import layr.api.routing.DataProvider;
import layr.api.routing.ExceptionHandler;
import layr.injection.core.ClassInstantiationHandlers;
import layr.injection.core.DefaultInjectableData;
import lombok.RequiredArgsConstructor;

@SuppressWarnings( {"rawtypes", "unchecked"} )
@RequiredArgsConstructor
public class DefaultInjectableDataFactory {

	final AvailableClassPathClassesAnalysis analysis;
	final ClassInstantiationHandlers instantiationHandlers;

	public DefaultInjectableDataFactory( AvailableClassPathClassesAnalysis analysis ) {
		this.analysis = analysis;
		this.instantiationHandlers = new ClassInstantiationHandlers( this.analysis.instantiationHandlers );
	}

	public DefaultInjectableData newInstance() {
		return new DefaultInjectableData(
				this.instantiationHandlers,
				sortByContentType( this.analysis.inputConverters() ),
				sortByContentType( this.analysis.outputRenderers() ),
				extractDataProviderFromGenerics( this.analysis.dataProviders() ),
				extractExceptionHandlerFromGenerics( this.analysis.exceptionHandler() ),
				this.analysis.webResources() );
	}

	Map<Class<?>, Class<? extends DataProvider<?>>> extractDataProviderFromGenerics( List<Class<DataProvider>> dataProviderList ) {
		Map<Class<?>, Class<? extends DataProvider<?>>> dataProviders = new HashMap<>();
		for ( Class dataProviderClass : dataProviderList ) {
			Class<?> genericClass = extractGenericClassWhenImplementsTheInterface( dataProviderClass, DataProvider.class );
			dataProviders.put( genericClass, dataProviderClass );
		}
		return dataProviders;
	}

	Map<Class<?>, Class<? extends ExceptionHandler<?>>> extractExceptionHandlerFromGenerics( List<Class<ExceptionHandler>> dataProviderList ) {
		Map<Class<?>, Class<? extends ExceptionHandler<?>>> dataProviders = new HashMap<>();
		for ( Class dataProviderClass : dataProviderList ) {
			Class<?> genericClass = extractGenericClassWhenImplementsTheInterface( dataProviderClass, ExceptionHandler.class );
			dataProviders.put( genericClass, dataProviderClass );
		}
		return dataProviders;
	}

	Class<?> extractGenericClassWhenImplementsTheInterface( Class<?> clazz, Class<?> interfaceClazz ) {
		for ( Type type : clazz.getGenericInterfaces() )
			if ( typeImplementsInterfaceInterface( type, interfaceClazz ) )
				return (Class<?>)( (ParameterizedType)type ).getActualTypeArguments()[0];
		throw new IllegalArgumentException();
	}

	boolean typeImplementsInterfaceInterface( Type interfaceType, Class<?> interfaceClazz ) {
		String canonicalName = interfaceClazz.getCanonicalName() + "<";
		return interfaceType.toString().startsWith( canonicalName );
	}

	<T> Map<String, Class<? extends T>> sortByContentType( List<Class<T>> data ) {
		Map<String, Class<? extends T>> sorted = new HashMap<>();
		for ( Class<T> clazz : data ) {
			ContentType annotation = clazz.getAnnotation( ContentType.class );
			if ( annotation != null )
				for ( String contentType : annotation.value() )
					sorted.put( contentType, clazz );
		}
		return sorted;
	}
}