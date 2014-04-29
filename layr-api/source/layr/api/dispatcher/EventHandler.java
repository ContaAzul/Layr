package layr.api.dispatcher;

public interface EventHandler {

	AsyncEventHandler handle( Event data );

}
