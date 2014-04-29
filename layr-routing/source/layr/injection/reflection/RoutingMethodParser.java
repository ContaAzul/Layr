package layr.injection.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import layr.api.routing.DELETE;
import layr.api.routing.GET;
import layr.api.routing.InjectableData;
import layr.api.routing.POST;
import layr.api.routing.PUT;
import layr.api.routing.Produces;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.parameters.Parameter;
import layr.injection.reflection.parameters.ParameterIterator;
import layr.injection.reflection.parameters.RoutingParameter;
import layr.injection.reflection.parameters.RoutingParameters;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoutingMethodParser implements Iterable<Parameter> {

	final List<RoutingParameter> parameters = new ArrayList<RoutingParameter>();

	final InjectableData injectableData;
	final Method method;
	final String rootPattern;
	final String rootProducedContentType;

	String urlPattern;
	String httpMethod;
	String producedContentType;

	public RoutingMethod parse() throws InstantiationException, IllegalAccessException, ConversionException {
		extractAndMemorizeContentType();
		extractAndMemorizeParameters();
		extractAndMemorizeURLPattern();
		return new RoutingMethod(
				this.method, this.parameters, this.httpMethod,
				this.urlPattern, this.producedContentType );
	}

	private void extractAndMemorizeContentType() {
		Produces produces = method.getAnnotation( Produces.class );
		if ( produces != null )
			producedContentType = produces.value();
		else
			producedContentType = rootProducedContentType;
	}

	public void extractAndMemorizeURLPattern() {
		tryMemorizePatternFromGetAnnotation();
		tryMemorizePatternFromPostAnnotation();
		tryMemorizePatternFromPutAnnotation();
		tryMemorizePatternFromDeleteAnnotation();
	}

	private void tryMemorizePatternFromGetAnnotation() {
		final GET getAnnotation = this.method.getAnnotation( GET.class );
		if ( getAnnotation != null ) {
			setUrlPattern( getAnnotation.value() );
			this.httpMethod = "GET";
		}
	}

	private void tryMemorizePatternFromPostAnnotation() {
		final POST postAnnotation = this.method.getAnnotation( POST.class );
		if ( postAnnotation != null ) {
			setUrlPattern( postAnnotation.value() );
			this.httpMethod = "POST";
		}
	}

	private void tryMemorizePatternFromPutAnnotation() {
		final PUT putAnnotation = this.method.getAnnotation( PUT.class );
		if ( putAnnotation != null ) {
			setUrlPattern( putAnnotation.value() );
			this.httpMethod = "PUT";
		}
	}

	private void tryMemorizePatternFromDeleteAnnotation() {
		final DELETE deleteAnnotation = this.method.getAnnotation( DELETE.class );
		if ( deleteAnnotation != null ) {
			setUrlPattern( deleteAnnotation.value() );
			this.httpMethod = "DELETE";
		}
	}

	private void setUrlPattern( final String extractURLPattern ) {
		this.urlPattern = String.format( "%s/%s", this.rootPattern, extractURLPattern );
	}

	public void extractAndMemorizeParameters() throws InstantiationException, IllegalAccessException, ConversionException {
		final RoutingParameters routingParameters = new RoutingParameters( this.injectableData );
		for ( Parameter parameter : this )
			this.parameters.add(
					routingParameters.parse(
							parameter.clazz(),
							parameter.annotations() )
					);
	}

	@Override
	public Iterator<Parameter> iterator() {
		final Class<?>[] parameterTypes = this.method.getParameterTypes();
		final Annotation[][] parameterAnnotations = this.method.getParameterAnnotations();
		return new ParameterIterator( parameterTypes, parameterAnnotations );
	}
}
