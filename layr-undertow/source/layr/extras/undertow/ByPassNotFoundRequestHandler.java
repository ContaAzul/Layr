package layr.extras.undertow;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import layr.api.dispatcher.NoHandlerAvailableFailureEvent;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import com.texoit.undertow.standalone.api.UndertowStandaloneException;

@Log
@NoArgsConstructor( staticName="create" )
public class ByPassNotFoundRequestHandler implements EventHandler {

	@Override
	public AsyncEventHandler handle(Event data) {
		try {
			NoHandlerAvailableFailureEvent noHandlerEvent = (NoHandlerAvailableFailureEvent)data;
			UndertowConnection connection = (UndertowConnection)noHandlerEvent.originalEvent();
			connection.chain().executeNext();
		} catch ( UndertowStandaloneException cause ) {
			log.severe( cause.getMessage() );
			cause.printStackTrace();
		}
		return null;
	}
}
