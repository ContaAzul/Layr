package layr.api.routing;

public class InjectionException extends Exception {

	private static final long serialVersionUID = 2451530309282424109L;

	public InjectionException( String message, Throwable cause ) {
		super( message, cause );
	}

	public InjectionException( String message ) {
		super( message );
	}
}
