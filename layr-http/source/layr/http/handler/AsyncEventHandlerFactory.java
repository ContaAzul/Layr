package layr.http.handler;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AsyncEventHandlerFactory implements EventHandler  {

	final Class<? extends AsyncEventHandler> targetClass;

	@Override
	public AsyncEventHandler handle(Event data) {
		try {
			return instantiate(data);
		} catch (SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException cause ) {
			throw new RuntimeException( "Could not handle failure!" );
		}
	}

	protected AsyncEventHandler instantiate(Event data)
			throws InstantiationException, IllegalAccessException {
		AsyncEventHandler eventHandler = targetClass.newInstance();
		return eventHandler;
	}
}
