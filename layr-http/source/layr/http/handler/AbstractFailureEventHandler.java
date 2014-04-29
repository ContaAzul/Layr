package layr.http.handler;

import layr.api.dispatcher.FailureEvent;
import layr.api.http.Connection;
import layr.api.http.FailureEventHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors( fluent = true )
public abstract class AbstractFailureEventHandler<T extends Throwable> implements FailureEventHandler<T> {

	private FailureEvent<T> failureEvent;

	@Override
	public void run() {
		Connection connection = (Connection)this.failureEvent.originalEvent();
		run( failureEvent, connection );
	}

	public abstract void run( FailureEvent<T> failureEvent, Connection connection );
}
