package layr.injection.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import layr.injection.converter.ConversionException;
import layr.injection.reflection.parameters.RoutingParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class RoutingMethod {

	final Method targetMethod;
	final List<RoutingParameter> parameters;
	final String method;
	final String url;
	final String contentType;

	public String name() {
		return this.targetMethod.getName();
	}

	public Object invoke( final RouteableRequest request, final Object instance ) throws Throwable {
		try {
			final Object[] params = getParameters( request );
			return this.targetMethod.invoke( instance, params );
		} catch ( final IllegalAccessException | IllegalArgumentException | InvocationTargetException cause ) {
			Throwable rootCause = cause;
			if ( rootCause.getCause() != null )
				rootCause = rootCause.getCause();
			throw rootCause;
		}
	}

	private Object[] getParameters( final RouteableRequest request ) throws ConversionException {
		final Object[] params = new Object[this.parameters.size()];
		int i = 0;
		for ( RoutingParameter parameter : this.parameters )
			params[i++] = parameter.extractValueFromRequest( request );
		return params;
	}
}
