package layr.injection.reflection.assertions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layr.api.http.ConnectionRequest;
import layr.api.routing.DataProvider;
import layr.api.routing.InjectableData;
import layr.api.routing.InputConverter;
import layr.api.routing.Response;
import layr.injection.converter.ConversionException;
import layr.injection.core.ClassInstantiationHandler;
import layr.injection.core.ClassInstantiationHandlers;
import layr.injection.core.DefaultInjectableData;
import layr.injection.reflection.RouteableRequest;
import layr.injection.reflection.RoutingClass;
import layr.injection.reflection.RoutingClassParser;
import layr.injection.reflection.RoutingMethod;
import layr.routing.impl.Hello;
import layr.routing.impl.HelloDataProvider;
import layr.routing.impl.TwoMethodsResource;

import org.junit.Before;
import org.junit.Test;

public class RoutingClassHaveRunnableMethodsAsWell {

	private static final int THOUSAND_OF_TIMES = 10001;
	RoutingMethod firstMethod;
	RouteableRequest request;

	@Before
	public void setup() throws InstantiationException, IllegalAccessException, ConversionException {
		InjectableData injectableData = getInjectableData();
		RoutingClassParser classParser = new RoutingClassParser( injectableData, TwoMethodsResource.class );

		RoutingClass routingClass = classParser.parse();
		assertNotNull( "Routing class should not be null:", routingClass );

		List<RoutingMethod> methods = routingClass.methods();
		assertNotNull( "Routing class methods should not be null:", methods );
		assertEquals( "This routing class should have 2 method:", 2, methods.size() );

		this.firstMethod = methods.get( 0 );
		assertThat( this.firstMethod.contentType(), is( "plain/text" ) );
		
		this.request = createRequest();
		
		RoutingMethod routingMethod = methods.get(1);
		assertThat( routingMethod.contentType(), is( "application/json" ) );
	}

	@Test
	public void grantIt() throws Throwable {
		Response response = (Response)this.firstMethod.invoke( this.request, new TwoMethodsResource() );
		Hello hello = (Hello)response.parameterObject();
		assertEquals( "Hello", hello.getValue1() );
		assertEquals( "World", hello.getValue2() );
	}

	@Test
	public void grantItThousandOfTimes() throws Throwable {
		long start = System.currentTimeMillis();
		for ( int i = 0; i < THOUSAND_OF_TIMES; i++ )
			grantIt();
		long end = System.currentTimeMillis();
		System.out.println( "Elapsed time: " + ( end - start ) );
	}

	private RouteableRequest createRequest() {
		ConnectionRequest mockedRequest = mock( ConnectionRequest.class );
		RouteableRequest request = new RouteableRequest( mockedRequest, new HashMap<String, String>() );
		return request;
	}

	private InjectableData getInjectableData() {
		Map<Class<?>, ClassInstantiationHandler<?>> handledTypes = new HashMap<Class<?>, ClassInstantiationHandler<?>>();
		ClassInstantiationHandlers handlers = new ClassInstantiationHandlers( handledTypes );
		Map<String, Class<? extends InputConverter>> inputConverters = new HashMap<String, Class<? extends InputConverter>>();
		Map<Class<?>, Class<? extends DataProvider<?>>> dataProviders = getDataProviders();
		InjectableData injectableData = new DefaultInjectableData( handlers, inputConverters, null, dataProviders, null, null );
		return injectableData;
	}

	private Map<Class<?>, Class<? extends DataProvider<?>>> getDataProviders() {
		Map<Class<?>, Class<? extends DataProvider<?>>> dataProviders = new HashMap<Class<?>, Class<? extends DataProvider<?>>>();
		dataProviders.put( Hello.class, HelloDataProvider.class );
		return dataProviders;
	}
}
