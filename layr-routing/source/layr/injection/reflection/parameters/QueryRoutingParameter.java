package layr.injection.reflection.parameters;

import layr.injection.converter.AbstractConverter;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName = "from" )
public class QueryRoutingParameter<T> implements RoutingParameter {

	final String name;
	final AbstractConverter<T> converter;

	@Override
	public Object extractValueFromRequest( RouteableRequest request )
			throws ConversionException {
		String[] values = request.parameters().get( this.name );
		if ( values == null || values.length == 0 )
			return null;
		String value = values[0];
		if ( value == null )
			return null;
		return this.converter.convert( value );
	}
}
