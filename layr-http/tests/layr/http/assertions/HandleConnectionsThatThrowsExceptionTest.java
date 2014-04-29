package layr.http.assertions;

import java.util.concurrent.CountDownLatch;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import layr.api.dispatcher.FailureEvent;
import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;
import layr.http.handler.AbstractFailureEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HandleConnectionsThatThrowsExceptionTest extends ConnectionHandlerTestSupport {

	static final int TOTAL_MSGS = 100000;
	static final String SAMPLE_URL = "/api/cobol/blah/";
	static final String BAD_URL = "/api/blah/cobol/";

	static CountDownLatch failureRequestCountdown = new CountDownLatch(TOTAL_MSGS);

	ConnectionHandler connectionHandler;
	CountDownLatch notFoundCountdown = new CountDownLatch(TOTAL_MSGS);

	@Test(timeout=6000)
	public void grantIt() throws InterruptedException {
		sendMessagesTo(SAMPLE_URL);
		sendMessagesTo(BAD_URL);
		failureRequestCountdown.await();
		notFoundCountdown.await();
	}

	private void sendMessagesTo(String url) {
		for ( int i=0; i<TOTAL_MSGS; i++ ) {
			Connection conn = createConnection("GET", url);
			connectionHandler.handle(conn);
		}
	}

	@Before
	public void oneTimeSetup(){
		connectionHandler = createConnectionHandler();
		connectionHandler
			.register( new FailureOperation() )
			.register( CountdownFailureHandler.class )
			.whenNotFound( new CountdownNotFoundFailureHandler( notFoundCountdown ));
	}

	@After
	public void tearDown(){
		System.out.println("Handled failed connections: "
				+ (TOTAL_MSGS - failureRequestCountdown.getCount()));
		System.out.println("Handled unknown connections: "
				+ (TOTAL_MSGS - notFoundCountdown.getCount()));
	}

	@Getter
	@Accessors(fluent=true)
	@RequiredArgsConstructor
	public static class CountdownFailureHandler extends AbstractFailureEventHandler<UnsupportedOperationException> {

		@Override
		public void run(
				FailureEvent<UnsupportedOperationException> failureEvent, Connection connection) {
			failureRequestCountdown.countDown();
		}
	}

	@Getter
	@Accessors(fluent=true)
	@RequiredArgsConstructor
	private static class CountdownNotFoundFailureHandler implements EventHandler {

		final CountDownLatch notFoundCountdown;

		@Override
		public AsyncEventHandler handle(Event data) {
			notFoundCountdown.countDown();
			return null;
		}
	}
}
