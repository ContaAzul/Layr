package layr.http.assertions;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;
import layr.api.http.ConnectionRequest;
import layr.dispatcher.Dispatcher;
import layr.http.handler.DefaultConnectionHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.Accessors;

public class ConnectionHandlerTestSupport {
	
	static final int NUMBER_OF_THREADS = 100;

	protected ConnectionHandler createConnectionHandler() {
		return createConnectionHandler( Executors.newFixedThreadPool( NUMBER_OF_THREADS ) );
	}

	protected ConnectionHandler createConnectionHandler(ExecutorService asyncExecutorService) {
		val dispatcher = Dispatcher.handledBy( asyncExecutorService );
		return new DefaultConnectionHandler(dispatcher);
	}

	protected Connection createConnection(String method, String url) {
		return new Connection( new StubRequest( url, method), null );
	}
	
	@Getter
	@Accessors( fluent=true )
	@RequiredArgsConstructor
	static class StubRequest implements ConnectionRequest {
		
		final String url;
		final String method;

		final InputStream body = null;
		final Map<String, String> cookies = null;
		final Map<String, String[]> headers = null;
		final Map<String, String[]> parameters = null;
		final String contentType = null;
		final String encoding = null;

		@Override
		public InputStream readAsStream() {
			return null;
		}

		@Override
		public String headers(String headerName) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String headers(String headerName, String defaultValue) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Long contentLength() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}