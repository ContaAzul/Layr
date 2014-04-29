package layr.dispatcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import layr.api.dispatcher.GenericEvent;
import lombok.RequiredArgsConstructor;
import lombok.val;

import org.junit.Before;
import org.junit.Test;

public class DispatcherTest {

	private static final String URL_COUNT_DOWN = "/count/down";
	private static final int NUMBER_OF_SUCCESSFUL_REQUEST_TIMES = 1600000;
	private static final int NUMBER_OF_FAILURE_REQUEST_TIMES = 1000000;

	final ExecutorService executorService = Executors.newCachedThreadPool();
	final Dispatcher dispatcher = Dispatcher.handledBy( executorService );
	final CountDownLatch successRequestCounter = new CountDownLatch( NUMBER_OF_SUCCESSFUL_REQUEST_TIMES );
	final CountDownLatch failureRequestCounter = new CountDownLatch( NUMBER_OF_FAILURE_REQUEST_TIMES );
	final Event eventData = new GenericEvent();

	@Before
	public void populateDispatcherWithEvents() {
		val countDownEventHandler = new CountdownEvent(successRequestCounter);
		val failureEventHandler = new CountdownEvent(failureRequestCounter);
		dispatcher.on(URL_COUNT_DOWN, countDownEventHandler);
		dispatcher.whenNoEventWasFound(failureEventHandler);
	}

	@Test( timeout=2000 )
	public void grantThatDispatchManyRequestAsynchronously() throws InterruptedException {
		for ( int i=0; i<NUMBER_OF_SUCCESSFUL_REQUEST_TIMES; i++ )
			dispatcher.notify(URL_COUNT_DOWN, eventData);
		successRequestCounter.await();
	}

	@Test( timeout=2000 )
	public void grantThatDispatchManyFailureRequestAsynchronously() throws InterruptedException {
		for ( int i=0; i<NUMBER_OF_SUCCESSFUL_REQUEST_TIMES; i++ )
			dispatcher.notify("/hello/world", eventData);
		failureRequestCounter.await();
	}

	@RequiredArgsConstructor
	static class CountdownEvent implements EventHandler {
		
		final CountDownLatch countDownLatch;

		@Override
		public AsyncEventHandler handle( Event data ) {
			return new AsyncCountdownEvent(countDownLatch);
		}
	}

	@RequiredArgsConstructor
	static class AsyncCountdownEvent implements AsyncEventHandler {
		
		final CountDownLatch countDownLatch;

		@Override
		public void run() {
			countDownLatch.countDown();
		}
	}
}
