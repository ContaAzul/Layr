package layr.injection.reflection.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import layr.api.routing.InjectableData;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RoutingClass;
import layr.injection.reflection.RoutingClassParser;
import layr.injection.reflection.RoutingMethod;
import layr.injection.reflection.parameters.BodyRoutingParameter;
import layr.injection.reflection.parameters.CookieRoutingParameter;
import layr.injection.reflection.parameters.DataProvidedRoutingParameter;
import layr.injection.reflection.parameters.HeaderRoutingParameter;
import layr.injection.reflection.parameters.PathRoutingParameter;
import layr.injection.reflection.parameters.QueryRoutingParameter;
import layr.injection.reflection.parameters.RoutingParameter;
import layr.routing.impl.TwoMethodsResource;

import org.junit.Test;

// @Checkstyle:OFF test class
public class RoutingClassAreParsedAsWell {

	@Test
	public void grantIt() throws InstantiationException, IllegalAccessException, ConversionException {
		InjectableData injectableData = null;
		RoutingClassParser classParser = new RoutingClassParser( injectableData, TwoMethodsResource.class );

		RoutingClass routingClass = classParser.parse();
		assertNotNull( "Routing class should not be null:", routingClass );

		List<RoutingMethod> methods = routingClass.methods();
		assertNotNull( "Routing class methods should not be null:", methods );
		assertEquals( "This routing class should have 1 method:", 1, methods.size() );

		RoutingMethod firstMethod = methods.get( 0 );
		List<RoutingParameter> parameters = firstMethod.parameters();
		assertEquals( "Invalida number of parameters:", 6, parameters.size() );
		assertEquals( PathRoutingParameter.class, parameters.get( 0 ).getClass() );
		assertEquals( QueryRoutingParameter.class, parameters.get( 1 ).getClass() );
		assertEquals( HeaderRoutingParameter.class, parameters.get( 2 ).getClass() );
		assertEquals( CookieRoutingParameter.class, parameters.get( 3 ).getClass() );
		assertEquals( BodyRoutingParameter.class, parameters.get( 4 ).getClass() );
		assertEquals( DataProvidedRoutingParameter.class, parameters.get( 5 ).getClass() );
	}
}
