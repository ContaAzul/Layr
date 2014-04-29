package layr.routing.handler;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layr.api.routing.DataProvider;
import layr.api.routing.DeploymentHook;
import layr.api.routing.ExceptionHandler;
import layr.api.routing.InputConverter;
import layr.api.routing.OutputRenderer;
import layr.api.routing.WebResource;
import layr.injection.core.ClassInstantiationHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class AvailableClassPathClassesAnalysis {

	final List<Class<DeploymentHook>> deploymentHooks = new ArrayList<>();
	final List<Class<DataProvider>> dataProviders = new ArrayList<>();
	final List<Class<InputConverter>> inputConverters = new ArrayList<>();
	final List<Class<OutputRenderer>> outputRenderers = new ArrayList<>();
	final List<Class<ExceptionHandler>> exceptionHandler = new ArrayList<>();
	final List<Class<?>> webResources = new ArrayList<>();
	final Map<Class<?>, ClassInstantiationHandler<?>> instantiationHandlers = new HashMap<>();

	public AvailableClassPathClassesAnalysis( Collection<Class<?>> classes ) {
		analyze( classes );
	}

	public void analyze( Collection<Class<?>> classes ) {
		for ( Class<?> clazz : classes )
			analyze( clazz );
	}

	public void analyze( Class<?> clazz ) {
		memorizeIfAssignableFrom( clazz, DeploymentHook.class, this.deploymentHooks );
		memorizeIfAssignableFrom( clazz, DataProvider.class, this.dataProviders );
		memorizeIfAssignableFrom( clazz, InputConverter.class, this.inputConverters );
		memorizeIfAssignableFrom( clazz, OutputRenderer.class, this.outputRenderers );
		memorizeIfAssignableFrom( clazz, ExceptionHandler.class, this.exceptionHandler );
		memorizeIfIsWebResource( clazz );
		memorizeAndInstantiateIfIsClassInstantationHandler( clazz );
	}

	protected <T> void memorizeIfAssignableFrom( Class<?> clazz, Class<T> type, List<Class<T>> listOfType ) {
		if ( isAssignableFrom( clazz, type ) )
			listOfType.add( (Class<T>)clazz );
	}

	protected void memorizeIfIsWebResource( Class<?> clazz ) {
		if ( clazz.isAnnotationPresent( WebResource.class ) )
			this.webResources.add( clazz );
	}

	protected void memorizeAndInstantiateIfIsClassInstantationHandler( Class<?> clazz ) {
		if ( isAssignableFrom( clazz, ClassInstantiationHandler.class ) )
			this.instantiationHandlers.put( clazz, instantiate( clazz ) );
	}

	protected boolean isAssignableFrom( Class<?> clazz, Class<?> type ){
		return !Modifier.isAbstract( clazz.getModifiers() )
			&& !Modifier.isInterface( clazz.getModifiers() )
			&& type.isAssignableFrom( clazz );
	}

	protected ClassInstantiationHandler instantiate( Class<?> clazz ) {
		try {
			return (ClassInstantiationHandler)clazz.newInstance();
		} catch ( InstantiationException | IllegalAccessException e ) {
			throw new RoutingInitializationException( e );
		}
	}
}
