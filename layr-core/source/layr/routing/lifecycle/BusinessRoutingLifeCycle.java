package layr.routing.lifecycle;

import static layr.commons.ListenableCall.listenable;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import layr.api.ApplicationContext;
import layr.api.ClassFactory;
import layr.api.RequestContext;
import layr.commons.ListenableCall;
import layr.commons.Listener;
import layr.exceptions.ClassFactoryException;

public class BusinessRoutingLifeCycle implements LifeCycle {

	ApplicationContext configuration;
	RequestContext requestContext;
	Listener<Object> onSuccess;
	List<Listener<Exception>> onFail;
	HandledMethod matchedRouteMethod;

	public BusinessRoutingLifeCycle(ApplicationContext configuration,
			RequestContext requestContext) {
		this.configuration = configuration;
		this.requestContext = requestContext;
		this.onFail = new ArrayList<Listener<Exception>>();
	}

	@Override
	public boolean canHandleRequest() {
		matchedRouteMethod = getMatchedRouteMethod();
		return matchedRouteMethod != null;
	}

	public HandledMethod getMatchedRouteMethod() {
		BusinessRoutingMethodMatching businessRoutingMethodMatching = new BusinessRoutingMethodMatching(
				configuration, requestContext);
		HandledMethod routeMethod = businessRoutingMethodMatching
				.getMatchedRouteMethod();
		return routeMethod;
	}

	public void run() {
		runMethod(matchedRouteMethod);
	}

	public void runMethod(HandledMethod routeMethod) {
		try {
			ListenableCall listenable = createListenableAsyncRunner(routeMethod);
			Future<?> submit = configuration.getMethodExecutionThreadPool()
					.submit(listenable);
			if (!requestContext.isAsyncRequest())
				submit.get();
		} catch (ExecutionException e) {
			onFail(e);
		} catch (Exception e) {
			onFail(e);
		}
	}

	public ListenableCall createListenableAsyncRunner(HandledMethod routeMethod)
			throws Exception {
		Object instance = newInstanceOf(routeMethod.getRouteClass());
		BusinessRoutingMethodRunner runner = new BusinessRoutingMethodRunner(
				configuration, requestContext, routeMethod, instance);
		ListenableCall listenable = listenable(runner);
		listenable
				.onSuccess(new RendererListener(configuration, requestContext));
		listenable.onSuccess(onSuccess);
		defineOnFail(listenable);
		return listenable;
	}

	public Object newInstanceOf(HandledClass routeClass)
			throws ClassFactoryException {
		try {
			ClassFactory<?> classFactory = getClassFactory(routeClass);
			return classFactory.newInstance(configuration, requestContext, routeClass.getTargetClass());
		} catch (InstantiationException e) {
			throw new ClassFactoryException("Bad ClassFactory instantiation.", e);
		} catch (IllegalAccessException e) {
			throw new ClassFactoryException("Bad ClassFactory instantiation.", e);
		}
	}

	@SuppressWarnings("rawtypes")
	private ClassFactory<?> getClassFactory(HandledClass routeClass) throws InstantiationException, IllegalAccessException {
		Map<String, Class<? extends ClassFactory>> registeredClassFactories = configuration.getRegisteredClassFactories();
		Class<?> targetClass = routeClass.getTargetClass();
		Class<? extends ClassFactory> classFactory = null;
		for (Annotation annotation : targetClass.getAnnotations()) {
			classFactory = registeredClassFactories.get(annotation.annotationType().getCanonicalName());
			if ( classFactory != null )
				return classFactory.newInstance();
		}
		return new DefaultClassFactory();
	}

	public void defineOnFail(ListenableCall listenable) {
		for (Listener<Exception> onFailListener : onFail)
			listenable.onFail(onFailListener);
	}

	public RequestContext getRequestContext() {
		return requestContext;
	}

	public void onFail(Listener<Exception> listener) {
		this.onFail.add(listener);
	}

	protected void onFail(Exception cause) {
		for (Listener<Exception> onFailListener : onFail)
			onFailListener.listen(cause);
	}

	public void onSuccess(Listener<Object> listener) {
		this.onSuccess = listener;
	}
}
