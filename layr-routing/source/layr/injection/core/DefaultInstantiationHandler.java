package layr.injection.core;

import layr.api.routing.InjectionException;

public class DefaultInstantiationHandler implements ClassInstantiationHandler<Object> {

	@Override
	public <V> V newInstance( Class<V> targetClazz ) throws InjectionException {
		try {
			return targetClazz.newInstance();
		} catch ( InstantiationException | IllegalAccessException e ) {
			throw new InjectionException( "Can't instantiate " + targetClazz.getCanonicalName(), e );
		}
	}
}
