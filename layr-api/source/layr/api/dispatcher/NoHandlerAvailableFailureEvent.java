package layr.api.dispatcher;

public class NoHandlerAvailableFailureEvent extends FailureEvent<Throwable> {

	public NoHandlerAvailableFailureEvent(String event, Event originalEvent) {
		super(event, originalEvent, null);
	}
}
