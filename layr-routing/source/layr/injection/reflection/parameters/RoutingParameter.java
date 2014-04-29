package layr.injection.reflection.parameters;

import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;

public interface RoutingParameter {

	Object extractValueFromRequest( RouteableRequest request ) throws ConversionException;
}
