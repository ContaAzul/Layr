package layr.api.http;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;

public interface ConnectionEventHandler extends EventHandler {

	public abstract String url();

	public abstract String method();

	@Override
	public abstract AsyncEventHandler handle( Event data );

	public abstract ConnectionAsyncEvent handle( Connection connection ) throws Exception;

}