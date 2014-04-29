package layr.injection.core;

import java.lang.annotation.Annotation;
import java.util.Map;

import layr.api.routing.InjectionException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class ClassInstantiationHandlers {

	final Map<Class<?>, ClassInstantiationHandler<?>> handlers;
	final DefaultInstantiationHandler defaultInstantiationHandler = new DefaultInstantiationHandler();

	@SuppressWarnings( "unchecked" )
	public ClassInstantiationHandler<?> forType( final Class<?> targetClazz ) throws InjectionException {
		for ( Class<?> clazz : this.handlers.keySet() )
			if ( clazz.isAssignableFrom( targetClazz )
					|| targetClazz.isAnnotationPresent( (Class<? extends Annotation>)clazz ) )
				return this.handlers.get( clazz );
		return this.defaultInstantiationHandler;
	}
}
