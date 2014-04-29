package layr.http.handler;

import layr.api.http.Connection;
import layr.api.http.ConnectionAsyncEvent;
import layr.api.http.ConnectionResponse;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors( fluent = true )
public abstract class AbstractConnectionAsyncEvent implements ConnectionAsyncEvent {

	private Connection connection;

	@Override
	public void run() {
		try {
			run( this.connection );
			// @Checkstyle:OFF I really needs to catch all errors here
		} catch ( Throwable cause ) {
			// @Checkstyle:ON
			handleFailure(cause);
		}
	}

	@Override
	public abstract void run( Connection connection ) throws Exception;

	private void handleFailure(Throwable cause) {
		ConnectionResponse response = connection.response();
		if ( response != null )
			response.flush();
		this.connection.context().notify( cause );
	}
}
