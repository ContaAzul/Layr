package layr.api.routing.impl;

import layr.api.routing.ResponseBuilder;

public interface RedirectResponse extends ResponseBuilder {
	RedirectResponse redirectTo(String url);
}
