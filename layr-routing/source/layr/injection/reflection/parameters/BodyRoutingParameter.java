package layr.injection.reflection.parameters;

import layr.api.routing.InjectableData;
import layr.api.routing.InjectionException;
import layr.api.routing.InputConverter;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BodyRoutingParameter implements RoutingParameter {

	final Class<?> targetClass;
	final InjectableData injectableData;

	@Override
	public Object extractValueFromRequest( RouteableRequest request )
			throws ConversionException {
		try {
			String contentType = request.contentType();
			if ( contentType == null )
				return null;
			InputConverter inputConverter = this.injectableData.inputConverterFor( contentType );
			return inputConverter.convert( request.readAsStream(), this.targetClass );
		} catch ( InjectionException cause ) {
			throw new ConversionException( cause );
		}
	}
}
