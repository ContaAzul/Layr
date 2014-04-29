package layr.http.assertions;

import java.util.concurrent.CountDownLatch;

import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HandleConnectionsThatDoesntThrowsExceptionTest extends ConnectionHandlerTestSupport {

	private static final int TOTAL_MSGS = 100;
	private static final String SAMPLE_URL = "/api/cobol/blah/";

	ConnectionHandler connectionHandler;
	CountDownLatch requestCountdown = new CountDownLatch(TOTAL_MSGS);

	@Test(timeout=2000)
	public void grantIt() throws InterruptedException {
		for ( int i=0; i<TOTAL_MSGS; i++ ) {
			Connection conn = createConnection("GET", SAMPLE_URL);
			connectionHandler.handle(conn);
		}
		requestCountdown.await();
	}

	@Before
	public void oneTimeSetup(){
		connectionHandler = createConnectionHandler();
		connectionHandler.register( new SyncOperation( requestCountdown ) );
	}

	@After
	public void tearDown(){
		System.out.println("Handled connections: "
				+ (TOTAL_MSGS - requestCountdown.getCount()));
	}
}
