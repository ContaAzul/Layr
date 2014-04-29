package layr.api.routing;

import java.util.List;

/**
 * Contains all information about injections in Layr. It provide some factories
 * and will memorize some data useful for routing algorithm.
 */
public interface InjectableData {

	InputConverter inputConverterFor( String contentType ) throws InjectionException;

	OutputRenderer outputRendererFor( String contentType ) throws InjectionException;

	DataProvider<?> dataProviderFor( Class<?> targetClass ) throws InjectionException;

	List<Class<?>> webResources();

	<T> T instantiate( Class<T> targetClazz ) throws InjectionException;

	<T extends Throwable> ExceptionHandler<T> exceptionHandlerFor( T targetClass ) throws InjectionException;

}
