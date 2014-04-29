package layr.injection.core;

import layr.api.routing.InjectionException;

/**
 * Handle class instantiation for classes that are annotated with or implements
 * T type defined as generic parameter of this interface.
 * 
 * @param <T>
 */
public interface ClassInstantiationHandler<T> {

	<V> V newInstance( Class<V> targetClazz ) throws InjectionException;

}
