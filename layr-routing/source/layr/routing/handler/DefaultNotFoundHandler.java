package layr.routing.handler;

import java.io.IOException;

import layr.api.dispatcher.FailureEvent;
import layr.api.http.Connection;
import layr.api.http.ConnectionResponse;

public class DefaultNotFoundHandler extends DefaultExceptionHandler {

	@Override
	public void handleFailure( FailureEvent<Throwable> failureEvent ) throws IOException {
		Connection connection = (Connection)failureEvent.originalEvent();
		ConnectionResponse response = connection.response();
		response.statusCode(404);
		response.write( "Could not found: " + connection.request().url() );
	}
}
