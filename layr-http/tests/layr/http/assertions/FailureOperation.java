package layr.http.assertions;

import layr.api.http.Connection;
import layr.api.http.ConnectionAsyncEvent;
import layr.http.handler.AbstractConnectionAsyncEvent;
import layr.http.handler.AbstractConnectionEventHandler;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
public class FailureOperation extends AbstractConnectionEventHandler {
	
	String url = "api/cobol/blah";
	String method = "GET";

	@Override
	public ConnectionAsyncEvent handle(Connection connection) {
		return new AsyncFailureOperation();
	}

	static class AsyncFailureOperation extends AbstractConnectionAsyncEvent {

		@Override
		public void run(Connection connection) {
			throw new UnsupportedOperationException();
		}
	}
}
