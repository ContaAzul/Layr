package layr.http.assertions;

import java.util.concurrent.CountDownLatch;

import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HandleAsyncConnectionsThatDoesntThrowsExceptionTest extends ConnectionHandlerTestSupport {

	private static final int TOTAL_MSGS = 1000000;
	private static final String SAMPLE_URL = "/api/cobol/blah/";

	final CountDownLatch requestCountdown = new CountDownLatch(TOTAL_MSGS);
	final Connection conn = createConnection("GET", SAMPLE_URL);
	ConnectionHandler connectionHandler;

	@Test(timeout=6000)
	public void grantIt() throws InterruptedException {
		for ( int i=0; i<TOTAL_MSGS; i++ )
			connectionHandler.handle(conn);
		requestCountdown.await();
	}

	@Before
	public void oneTimeSetup(){
		connectionHandler = createConnectionHandler();
		connectionHandler.register( new AsyncOperation( requestCountdown ) );
	}

	@After
	public void tearDown(){
		System.out.println("Handled connections: "
				+ (TOTAL_MSGS - requestCountdown.getCount()));
	}
}
