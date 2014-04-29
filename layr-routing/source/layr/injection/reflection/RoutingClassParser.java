package layr.injection.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import layr.api.routing.DELETE;
import layr.api.routing.GET;
import layr.api.routing.InjectableData;
import layr.api.routing.POST;
import layr.api.routing.PUT;
import layr.api.routing.Produces;
import layr.api.routing.WebResource;
import layr.injection.converter.ConversionException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoutingClassParser {

	final List<RoutingMethod> routeableMethods = new ArrayList<RoutingMethod>();
	final InjectableData injectableData;
	final Class<?> targetClass;

	String urlRootPattern;
	String producedContentType;

	public RoutingClass parse() throws InstantiationException, IllegalAccessException, ConversionException {
		extractAndMemorizeUrlPattern();
		extractAndMemorizeContentType();
		extractAndMemorizeRouteableMethods();
		assertClassHasAtLeastOneMethod();
		return new RoutingClass( this.routeableMethods );
	}

	private void extractAndMemorizeContentType() {
		Produces produces = targetClass.getAnnotation( Produces.class );
		producedContentType = produces != null ? produces.value() : "plain/text";
	}

	public void assertClassHasAtLeastOneMethod() {
		if ( this.routeableMethods.size() == 0 )
			throw new IllegalStateException( this.targetClass.getCanonicalName() + " should have at least one routeable method." );
	}

	public void extractAndMemorizeUrlPattern() {
		final WebResource annotation = this.targetClass.getAnnotation( WebResource.class );
		if ( annotation == null )
			throw new IllegalStateException( this.targetClass.getCanonicalName() + " aren't annotated with @WebResource." );
		this.urlRootPattern = annotation.value();
	}

	public void extractAndMemorizeRouteableMethods() throws InstantiationException, IllegalAccessException, ConversionException {
		Class<?> clazz = this.targetClass;
		while ( !Object.class.equals( clazz ) ) {
			extractAndMemorizeRouteableMethods( clazz );
			clazz = clazz.getSuperclass();
		}
	}

	private void extractAndMemorizeRouteableMethods( final Class<?> clazz ) throws InstantiationException, IllegalAccessException,
			ConversionException {
		for ( Method method : clazz.getDeclaredMethods() )
			if ( isRouteableMethod( method ) )
				parseAndMemorizeMethod( method );
	}

	private boolean isRouteableMethod( final Method method ) {
		return method.isAnnotationPresent( GET.class )
				|| method.isAnnotationPresent( POST.class )
				|| method.isAnnotationPresent( PUT.class )
				|| method.isAnnotationPresent( DELETE.class );
	}

	private void parseAndMemorizeMethod( final Method method ) throws InstantiationException, IllegalAccessException, ConversionException {
		final RoutingMethodParser methodParser = new RoutingMethodParser( this.injectableData, method, this.urlRootPattern, producedContentType );
		final RoutingMethod routingMethod = methodParser.parse();
		this.routeableMethods.add( routingMethod );
	}
}
