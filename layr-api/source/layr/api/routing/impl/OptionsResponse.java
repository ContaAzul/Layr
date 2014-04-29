package layr.api.routing.impl;

public interface OptionsResponse extends HeaderResponse, StatusCodeResponse {

	OptionsResponse set(String name, Object value);

	OptionsResponse parameterObject(Object parameters);

	OptionsResponse encoding(String encoding);
}

