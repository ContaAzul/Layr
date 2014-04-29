package layr.routing.http.assertions;

import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import layr.api.dispatcher.FailureEvent;
import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;
import layr.api.http.ConnectionResponse;
import layr.api.routing.ContentType;
import layr.api.routing.DeploymentException;
import layr.api.routing.ExceptionHandler;
import layr.api.routing.InjectionException;
import layr.api.routing.InputConverter;
import layr.api.routing.Response;
import layr.api.routing.impl.Responses;
import layr.http.handler.AbstractFailureEventHandler;
import layr.http.handler.DefaultConnectionHandler;
import layr.http.handler.FailureEventHandlerWrapper;
import layr.injection.core.ClassInstantiationHandler;
import layr.injection.core.DefaultInjectableData;
import layr.injection.core.PlainTextOutputRenderer;
import layr.routing.handler.RoutingDeploymentHook;
import layr.routing.handler.RoutingLifeCycle;
import layr.routing.impl.HelloDataProvider;
import layr.routing.impl.TwoMethodsResource;
import layr.routing.impl.TwoMethodsResource.FailureException;
import lombok.RequiredArgsConstructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FullRequestThatHandleConnectionsAndDoesntThrowsExceptionTest {

	private static final int TOTAL_MSGS = 100000;
	private static final String SAMPLE_URL = "/hello/world/123/";
	private static final String FAILURE_URL = "/hello/world/";

	final CountDownLatch requestCountdown = new CountDownLatch( TOTAL_MSGS );

	ConnectionHandler connectionHandler;
	RoutingLifeCycle lifeCycle;

	@Test( timeout = 4000 )
	public void grantThatHandleSuccessRequests() throws InterruptedException {
		final Connection conn = createConnection( "GET", SAMPLE_URL );
		for ( int i = 0; i < TOTAL_MSGS; i++ )
			this.connectionHandler.handle( conn );
		this.requestCountdown.await();
	}

	@Test( timeout = 4000 )
	public void grantThatHandleFailures() throws InterruptedException {
		final Connection conn = createConnection( "POST", FAILURE_URL );
		for ( int i = 0; i < TOTAL_MSGS; i++ )
			this.connectionHandler.handle( conn );
		this.requestCountdown.await();
	}

	@Before
	public void oneTimeSetup() throws DeploymentException {
		DefaualtFailureHandler failureHandler = new DefaualtFailureHandler( this.requestCountdown );
		ConnectionHandler connectionHandlerWithDefaultDispatcher = DefaultConnectionHandler.withDefaultDispatcher();
		this.lifeCycle = new RoutingLifeCycle( createClassesLists(), connectionHandlerWithDefaultDispatcher );
		forceInjectionOfTwoMethodResourceClassInstantiationHandler();
		this.connectionHandler = this.lifeCycle.initialize();
		connectionHandler.defaultExceptionHandler( FailureEventHandlerWrapper.wrap(failureHandler) );
	}

	private void forceInjectionOfTwoMethodResourceClassInstantiationHandler() {
		DefaultInjectableData injectableData = (DefaultInjectableData)this.lifeCycle.injectableData();
		injectableData.handlers().handlers()
				.put( TwoMethodsResource.class,
						new TwoMethodsResourceInstatiationHandler( this.requestCountdown ) );
//		injectableData.
	}

	@SuppressWarnings( "unchecked" )
	public List<Class<?>> createClassesLists() {
		return list(
				TwoMethodsResource.class,
				RoutingDeploymentHook.class,
				HelloDataProvider.class,
				HtmlInputConverter.class,
				PlainTextOutputRenderer.class,
				FailureExceptionHandler.class,
				ClassInstantiationHandler.class,
				AbstractFailureEventHandler.class );
	}

	@SuppressWarnings( "unchecked" )
	public <T> List<T> list( T... objects ) {
		List<T> list = new ArrayList<T>();
		for ( T t : objects )
			list.add( t );
		return list;
	}

	protected Connection createConnection( String method, String url ) {
		StubRequest request = new StubRequest( url, method );
		ConnectionResponse response = mock( ConnectionResponse.class );
		return new Connection( request, response );
	}

	@After
	public void tearDown() throws Throwable {
		this.lifeCycle.shutdown();
		System.out.println( "Handled connections: "
				+ ( TOTAL_MSGS - this.requestCountdown.getCount() ) );
	}

	@RequiredArgsConstructor
	private static class DefaualtFailureHandler extends AbstractFailureEventHandler<Throwable> {

		final CountDownLatch countDownLatch;

		@Override
		public void run(FailureEvent<Throwable> failureEvent,
				Connection connection) {
			this.countDownLatch.countDown();
		}
	}

	@RequiredArgsConstructor
	@SuppressWarnings( "unchecked" )
	private static class TwoMethodsResourceInstatiationHandler implements ClassInstantiationHandler<TwoMethodsResource> {

		final CountDownLatch countDownLatch;

		@Override
		public <V> V newInstance( Class<V> targetClazz ) throws InjectionException {
			TwoMethodsResource resource = new TwoMethodsResource();
			resource.countDownLatch( this.countDownLatch );
			return (V)resource;
		}
	}

	@ContentType("text/html")
	public static class HtmlInputConverter implements InputConverter {

		@Override
		public Object convert(InputStream body, Class<?> targetClass) {
			return null;
		}
	}

	public static class FailureExceptionHandler implements ExceptionHandler<FailureException> {

		@Override
		public Response render(Connection conn, FailureException exception) {
			exception.countDownLatch().countDown();
			return Responses.statusCode(500).build();
		}
	}
}
