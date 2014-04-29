package layr.http.handler;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.http.Connection;
import layr.api.http.ConnectionAsyncEvent;
import layr.api.http.ConnectionEventHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors( fluent = true )
public abstract class AbstractConnectionEventHandler implements ConnectionEventHandler {

	@Override
	public AsyncEventHandler handle( Event data ) {
		try {
			return tryHandleEvent( data );
			// @Checkstyle:OFF I really needs to catch all errors here
		} catch ( Exception cause ) {
			// @Checkstyle:ON
			data.context().notify( cause, data );
			return null;
		}
	}

	private AsyncEventHandler tryHandleEvent( Event data ) throws Exception {
		Connection connection = (Connection)data;
		ConnectionAsyncEvent asyncEvent = handle( connection );
		if ( asyncEvent != null )
			asyncEvent.connection( connection );
		return asyncEvent;
	}

}
