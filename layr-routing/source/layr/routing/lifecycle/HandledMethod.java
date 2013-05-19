package layr.routing.lifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import layr.engine.RequestContext;
import layr.engine.expressions.URLPattern;

public class HandledMethod {

	HandledClass routeClass;
	Method targetMethod;
	List<HandledParameter> parameters;
	String pattern;

	Object lastReturnedValue;
	String httpMethod;

	public HandledMethod(HandledClass routeClass, Method targetMethod,
			String httpMethod, String pattern) {
		this.routeClass = routeClass;
		this.targetMethod = targetMethod;
		this.parameters = new ArrayList<HandledParameter>();
		this.httpMethod = httpMethod;
		setRouteMethodPattern( pattern );
		extractRouteMethodParameters();
	}

	public void extractRouteMethodParameters() {
		Class<?>[] parameterTypes = targetMethod.getParameterTypes();
		Annotation[][] parameterAnnotations = targetMethod
				.getParameterAnnotations();

		short cursor = 0;
		for (Annotation[] annotations : parameterAnnotations) {
			Annotation annotation = annotations[0];
			Class<?> clazz = parameterTypes[cursor];
			memorizeParameterFromAnnotation(annotation, clazz);
			cursor++;
		}
	}

	void memorizeParameterFromAnnotation(Annotation annotation,
			Class<?> targetClazz) {
		this.parameters.add(HandledParameterFactory.newInstance(annotation,
				targetClazz));
	}

	public Object invoke(Request request, Object instance) throws Throwable {
		try {
			Object[] methodParameters = new Object[parameters.size()];
			short cursor = 0;
			for (HandledParameter parameter : parameters)
				methodParameters[cursor++] = request.getValue(parameter);
			lastReturnedValue = targetMethod.invoke(instance, methodParameters);
			return lastReturnedValue;
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	public Map<String, String> extractPathParameters(
			RequestContext requestContext) {
		return new URLPattern().extractMethodPlaceHoldersValueFromURL(
				getRouteMethodPattern(), requestContext.getRequestURI());
	}

	public boolean matchesTheRequest(RequestContext requestContext) {
		return matchesTheRequestURI(requestContext)
				&& matchesTheHTTPMethod(requestContext);
	}

	public boolean matchesTheRequestURI(RequestContext requestContext) {
		String fullPathPattern = getRouteMethodPattern();
		String methodUrlPattern = new URLPattern()
				.parseMethodUrlPatternToRegExp(fullPathPattern);
		return requestContext.getRequestURI().matches(methodUrlPattern);
	}

	public boolean matchesTheHTTPMethod(RequestContext requestContext) {
		return requestContext.getRequestHttpMethod().equals(httpMethod);
	}
	
	void setRouteMethodPattern(String pattern) {
		this.pattern = (routeClass.rootPath + pattern + "/?").replace( "//", "/" );
	}

	public String getRouteMethodPattern() {
		return pattern;
	}

	public HandledClass getRouteClass() {
		return routeClass;
	}

	public Object getLastReturnedValue() {
		return lastReturnedValue;
	}
}