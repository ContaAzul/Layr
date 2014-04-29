package layr.http.assertions;

import java.util.concurrent.CountDownLatch;

import layr.api.http.Connection;
import layr.api.http.ConnectionAsyncEvent;
import layr.http.handler.AbstractConnectionEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors( fluent = true )
@Getter
@Setter
@RequiredArgsConstructor
public class SyncOperation
		extends AbstractConnectionEventHandler {

	final CountDownLatch countDownLatch;

	String url = "api/cobol/{name}";
	String method = "GET";

	@Override
	public ConnectionAsyncEvent handle(Connection connection) {
		this.countDownLatch.countDown();
		return null;
	}
}
