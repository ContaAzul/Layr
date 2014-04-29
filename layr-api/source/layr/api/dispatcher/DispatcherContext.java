package layr.api.dispatcher;

public interface DispatcherContext {

	/**
	 * Define an event handler to handle dispatched non-handled event.
	 * 
	 * @param eventHandler
	 * @return
	 */
	DispatcherContext whenNoEventWasFound( EventHandler eventHandler );

	/**
	 * Define a handler to handle thrown exceptions.
	 * 
	 * @param event
	 * @param eventHandler
	 * @return
	 */
	<T extends Throwable> DispatcherContext on( Class<T> event, EventHandler eventHandler );

	/**
	 * Define a handler to handle events named as 'event' parameter.
	 * 
	 * @param event
	 * @param eventHandler
	 * @return
	 */
	DispatcherContext on( String event, EventHandler eventHandler );

	/**
	 * Notify a handler that an exception have been thrown.
	 * 
	 * @param cause
	 */
	void notify( Throwable cause );

	/**
	 * Notify a handler that the event named 'event' happened.
	 * 
	 * @param event
	 * @param data
	 */
	void notify( String event, Event data );

	/**
	 * Notify a handler that an exception have been thrown. It will propagate
	 * the original event, sent as 'originalEvent', to the event handler.
	 * 
	 * @param cause
	 * @param originalEvent
	 */
	<T extends Throwable> void notify( T cause, Event originalEvent );

	/**
	 * Defines the default exception handler when no handler are defined.
	 * 
	 * @param event
	 */
	DispatcherContext defaultExceptionEventHandler(EventHandler event);

}
