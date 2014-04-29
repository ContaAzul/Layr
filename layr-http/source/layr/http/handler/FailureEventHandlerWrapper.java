package layr.http.handler;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import layr.api.dispatcher.FailureEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "wrap")
public class FailureEventHandlerWrapper<T extends Throwable> implements EventHandler {

	@NonNull final AbstractFailureEventHandler<T> asyncEventHandler;

	@Override
	@SuppressWarnings("unchecked")
	public AsyncEventHandler handle(Event data) {
		asyncEventHandler.failureEvent((FailureEvent<T>) data);
		return asyncEventHandler;
	}
}