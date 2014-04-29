package layr.injection.reflection.parameters;

import layr.injection.converter.AbstractConverter;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName = "from" )
public class HeaderRoutingParameter<T> implements RoutingParameter {

	final String name;
	final AbstractConverter<T> converter;

	@Override
	public Object extractValueFromRequest( RouteableRequest request )
			throws ConversionException {
		String value = request.headers( this.name );
		if ( value == null || value.isEmpty() )
			return null;
		return this.converter.convert( value );
	}
}
