package layr.routing.impl;

import layr.api.http.ConnectionRequest;
import layr.api.routing.DataProvider;

public class HelloDataProvider implements DataProvider<Hello> {

	@Override
	public Hello newInstance( ConnectionRequest request ) {
		return new DefaultHello();
	}

}
