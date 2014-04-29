package layr.api.routing.impl;

import layr.api.routing.ResponseBuilder;

public interface HeaderResponse extends ResponseBuilder {
	HeaderResponse header(String name, String value);
}
