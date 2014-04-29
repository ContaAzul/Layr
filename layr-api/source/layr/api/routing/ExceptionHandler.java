package layr.api.routing;

import layr.api.http.Connection;

public interface ExceptionHandler<T extends Throwable> {

	Response render( Connection conn, T exception );

}
