package layr.routing.handler;

import java.io.IOException;

import layr.api.dispatcher.FailureEvent;
import layr.api.http.Connection;
import layr.api.http.ConnectionResponse;
import layr.http.handler.AbstractFailureEventHandler;

public class DefaultExceptionHandler extends AbstractFailureEventHandler<Throwable> {

	@Override
	public void run( FailureEvent<Throwable> failureEvent, Connection connection ) {
		try {
			handleFailure(failureEvent);
		// @Checkstyle:OFF I really need to check all exceptions here.
		} catch ( Throwable cause ) {
		// @Checkstyle:ON
		} finally {
			failureEvent.cause().printStackTrace();
		}
	}

	public void handleFailure(FailureEvent<Throwable> failureEvent) throws IOException {
		Connection connection = (Connection)failureEvent.originalEvent();
		ConnectionResponse response = connection.response();
		response.statusCode(500);
		response.write("Internal Server Error. Check your log for details.");
	}
}
