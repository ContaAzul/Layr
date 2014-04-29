package layr.routing.handler;

public class RoutingInitializationException extends RuntimeException {

	private static final long serialVersionUID = 12093123971230L;

	public RoutingInitializationException( Throwable cause ) {
		super( cause );
	}

	public RoutingInitializationException( String message ) {
		super( message );
	}

}
