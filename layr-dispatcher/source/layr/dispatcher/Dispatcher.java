package layr.dispatcher;

import java.util.List;
import java.util.concurrent.ExecutorService;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.DispatcherContext;
import layr.api.dispatcher.Event;
import layr.api.dispatcher.EventHandler;
import layr.api.dispatcher.FailureEvent;
import layr.api.dispatcher.NoHandlerAvailableFailureEvent;
import layr.commons.Tuple;
import layr.dispatcher.tree.DefaultNodeFactory;
import layr.dispatcher.tree.Node;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;

@Accessors( fluent=true )
@RequiredArgsConstructor(staticName="handledBy")
public class Dispatcher implements DispatcherContext {

	final Node<EventHandler> eventHandlers = DefaultNodeFactory.rootNode();
	final ExecutorService executorService;

	@Setter
	EventHandler defaultExceptionEventHandler;
	EventHandler notFoundEventHandler;

	@Override
	public Dispatcher on( String event, EventHandler eventHandler ) {
		this.eventHandlers.set( event, eventHandler );
		return this;
	}

	@Override
	public <T extends Throwable> Dispatcher on( Class<T> event, EventHandler eventHandler ) {
		this.eventHandlers.set( MetaInf.applyPrefix( event ), eventHandler );
		return this;
	}

	@Override
	public Dispatcher whenNoEventWasFound( EventHandler eventHandler ) {
		this.notFoundEventHandler = eventHandler;
		return this;
	}

	@Override
	public void notify( String event, Event data ) {
		data.context(this);

		EventHandler eventHandler = this.eventHandlers.get(event);
		if ( eventHandler == null )
			handleEventNotFound( event, data );
		else
			handle(eventHandler, data);
	}

	@Override
	public void notify( Throwable cause ) {
		notify( cause, null );
	}

	@Override
	public <T extends Throwable> void notify( T cause, Event originalEvent ) {
		try {
			notifyNoiselessly(cause, originalEvent);
		} catch ( Throwable newFailureCause ) {
			newFailureCause.printStackTrace();
		}
	}

	private <T extends Throwable> void notifyNoiselessly(T cause, Event originalEvent) {
		Tuple<String, EventHandler> eventNameAndHandler = retrieveExceptionHandlerFor( cause );
		String eventName = eventNameAndHandler.value1();
		EventHandler eventHandler = eventNameAndHandler.value2();
		FailureEvent<T> failureEvent = new FailureEvent<T>( eventName, originalEvent, cause );
		handle(eventHandler, failureEvent);
	}

	public <T extends Throwable> Tuple<String, EventHandler> retrieveExceptionHandlerFor( T cause ) {
		List<String> clazzHierarchy = MetaInf.from(cause.getClass()).extract();
		for ( String possibleExceptionHandler : clazzHierarchy ) {
			EventHandler eventHandler = eventHandlers.get(possibleExceptionHandler);
			if ( eventHandler != null )
				return Tuple.from(possibleExceptionHandler, eventHandler);
		}
		return Tuple.from( "@Unhandled", defaultExceptionEventHandler );
	}

	private void handleEventNotFound( String event, Event originalEvent ) {
		assertNotFoundHandlerIsNotNull(event);
		val failureEvent = new NoHandlerAvailableFailureEvent(event, originalEvent);
		handle(notFoundEventHandler, failureEvent);
	}

	private void assertNotFoundHandlerIsNotNull(String event) {
		if ( notFoundEventHandler == null )
			throw new RuntimeException( "No event handler defined for " + event + "." );
	}

	private void handle(EventHandler eventHandler, Event data) {
		AsyncEventHandler asyncEventHandler = eventHandler.handle( data );
		if ( asyncEventHandler != null )
			executorService.submit(asyncEventHandler);
	}
}
