package layr.http.handler;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.FailureEvent;
import layr.api.http.FailureEventHandler;

public class FailureEventHandlerFactory<T extends Throwable> extends AsyncEventHandlerFactory {

	public FailureEventHandlerFactory( Class<? extends FailureEventHandler<T>> targetClass ) {
		super( targetClass );
	}

	@SuppressWarnings( "unchecked" )
	@Override
	protected AsyncEventHandler instantiate( Event data )
			throws InstantiationException, IllegalAccessException {
		FailureEventHandler<T> instance = (FailureEventHandler<T>)super.instantiate( data );
		instance.failureEvent( (FailureEvent<T>)data );
		return instance;
	}
}
