package layr.api.routing;

import layr.api.http.ConnectionRequest;

public interface DataProvider<T> {

	T newInstance( ConnectionRequest request );

}
