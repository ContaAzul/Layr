package layr.api.http;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.FailureEvent;

public interface FailureEventHandler<T extends Throwable> extends AsyncEventHandler {

	FailureEvent<T> failureEvent();

	FailureEventHandler<T> failureEvent( FailureEvent<T> failureEvent );

}