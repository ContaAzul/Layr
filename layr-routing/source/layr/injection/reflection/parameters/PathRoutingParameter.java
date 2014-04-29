package layr.injection.reflection.parameters;

import java.util.Map;

import layr.injection.converter.AbstractConverter;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName = "from" )
public class PathRoutingParameter<T> implements RoutingParameter {

	final String name;
	final AbstractConverter<T> converter;

	@Override
	public Object extractValueFromRequest( RouteableRequest request ) throws ConversionException {
		Map<String, String> placeHolders = request.placeHolders();
		String value = placeHolders.get( this.name );
		if ( value == null || value.isEmpty() )
			return null;
		return this.converter.convert( value );
	}
}
