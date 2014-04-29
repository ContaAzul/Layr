package layr.api.dispatcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * The event dispatched when an exception is thrown.
 *
 * @param <T>
 */
@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
public class FailureEvent<T extends Throwable> extends GenericEvent {

	final String event;
	final Event originalEvent;
	final T cause;
}
