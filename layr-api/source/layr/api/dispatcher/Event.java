package layr.api.dispatcher;

/**
 * An dispatched event.
 * 
 */
public interface Event {

	/**
	 * Retrieves the dispatcher context.
	 * 
	 * @return
	 */
	DispatcherContext context();
	
	
	/**
	 * Defines a dispatcher context to this event.
	 * 
	 * @param context
	 * @return
	 */
	Event context( DispatcherContext context );

}
