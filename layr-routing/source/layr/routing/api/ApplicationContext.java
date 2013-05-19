package layr.routing.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import layr.engine.Cache;
import layr.engine.RequestContext;
import layr.engine.components.ComponentFactory;
import layr.routing.exceptions.RoutingException;
import layr.routing.lifecycle.ContainerRequestData;
import layr.routing.lifecycle.HandledClass;

/**
 * Holds application data useful for all requests.
 */
public interface ApplicationContext {

	/**
	 * @return a map with TagLibs found during the deploy
	 */
	public abstract Map<String, ComponentFactory> getRegisteredTagLibs();

	/**
	 * @return a list of web resources found during the deploy
	 */
	public abstract List<HandledClass> getRegisteredWebResources();

	/**
	 * @return a map with Exception handlers found during the deploy
	 */
	public abstract Map<String, Class<ExceptionHandler<?>>> getRegisteredExceptionHandlers();

	/**
	 * @return the cache defined during deploy
	 */
	public abstract Cache getCache();

	/**
	 * Retrieves the default web resource path. It used to define the path
	 * to be rendered when requested the application root URI ( '/' ) .
	 * @return the default web resource path
	 */
	public abstract String getDefaultResource();
	
	/**
	 * @return the default encoding for template and request
	 */
	public abstract String getDefaultEncoding();

	/**
	 * @param containerRequestData
	 * @return the request context object that will be used during request Life
	 *         Cycle
	 */
	public abstract RequestContext createContext(ContainerRequestData<?, ?> containerRequestData);

	/**
	 * Instantiate the route class
	 * 
	 * @param routeClass
	 * @return a new object instance from the RouteClass
	 * @throws RoutingException
	 */
	public abstract Object newInstanceOf(HandledClass routeClass) throws Exception;

	/**
	 * Retrieves the ExecutorService that will execute every asynchronous
	 * template rendering process.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ExecutorService getRendererExecutorService() throws Exception;

	/**
	 * Retrieves the ExecutorService that will execute every CPU blocking route
	 * during render Life Cycle.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ExecutorService getTaskExecutorService() throws Exception;
}