package layr.http.handler;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName="wrap" )
public class AsyncEventHandlerWrapper implements EventHandler {
	
	@NonNull final AsyncEventHandler asyncEventHandler;

	@Override
	public AsyncEventHandler handle(Event data) {
		return asyncEventHandler;
	}
}
