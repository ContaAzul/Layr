package layr.api.http;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.EventHandler;

public interface ConnectionHandler {

	/**
	 * Handle incoming connections
	 * 
	 * @param connection
	 */
	public abstract void handle( Connection connection );

	/**
	 * Define a class that will be instantiated and run always an Exception were
	 * thrown.
	 * 
	 * @param failureHandler
	 * @return
	 */
	public abstract <V extends Throwable, T extends FailureEventHandler<V>>
			ConnectionHandler register( Class<T> failureHandler );

	public abstract Class<?> retrieveGenericClassFrom( Class<?> type );

	/**
	 * Define a class that will be instantiated and run always an event has no
	 * EventHandler.
	 * 
	 * @param failureHandler
	 * @return
	 */
	public abstract <V extends Throwable, T extends FailureEventHandler<V>>
			ConnectionHandler whenNotFound( Class<T> failureHandler );

	/**
	 * Define an asynchronous event handler to run always an event has no
	 * EventHandler.
	 * 
	 * @param asyncEvent
	 * @return
	 */
	public abstract ConnectionHandler whenNotFound( AsyncEventHandler asyncEvent );

	/**
	 * Define an event handler to run always an event has no EventHandler.
	 * 
	 * @param wrapper
	 * @return
	 */
	public abstract ConnectionHandler whenNotFound( EventHandler event );

	/**
	 * Defines an event handler to run always an exception is thrown and no
	 * event handled was assigned to it.
	 * 
	 * @param event
	 * @return
	 */
	public abstract ConnectionHandler defaultExceptionHandler( EventHandler event );

	/**
	 * Register an asynchronous connection handler.
	 * 
	 * @param asyncOperation
	 * @return
	 */
	public abstract ConnectionHandler register( ConnectionEventHandler asyncOperation );

}