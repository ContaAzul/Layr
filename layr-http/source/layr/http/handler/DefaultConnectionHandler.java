package layr.http.handler;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.Executors;

import layr.api.dispatcher.AsyncEventHandler;
import layr.api.dispatcher.DispatcherContext;
import layr.api.dispatcher.EventHandler;
import layr.api.http.Connection;
import layr.api.http.ConnectionEventHandler;
import layr.api.http.ConnectionHandler;
import layr.api.http.ConnectionRequest;
import layr.api.http.FailureEventHandler;
import layr.dispatcher.Dispatcher;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class DefaultConnectionHandler implements ConnectionHandler {

	static final String SLASH = "/";
	final DispatcherContext dispatcher;

	public static ConnectionHandler withDefaultDispatcher() {
		val numberOfProcessors = Runtime.getRuntime().availableProcessors();
		val executorService = Executors.newFixedThreadPool( numberOfProcessors );
		val dispatcher = Dispatcher.handledBy( executorService );
		return new DefaultConnectionHandler( dispatcher );
	}

	@Override
	public void handle( final Connection connection ) {
		final String url = createUrl( connection );
		this.dispatcher.notify( url, connection );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <V extends Throwable, T extends FailureEventHandler<V>>
			ConnectionHandler register( final Class<T> failureHandler )
	{
		val wrapper = new FailureEventHandlerFactory<V>( failureHandler );
		Class<?> genericClass = retrieveGenericClassFrom( failureHandler );
		this.dispatcher.on( (Class<Throwable>)genericClass, wrapper );
		return this;
	}

	@Override
	public Class<?> retrieveGenericClassFrom( Class<?> type ) {
		ParameterizedType ptype = (ParameterizedType)type.getGenericSuperclass();
		return (Class<?>)ptype.getActualTypeArguments()[0];
	}

	@Override
	public <V extends Throwable, T extends FailureEventHandler<V>>
			ConnectionHandler whenNotFound( final Class<T> failureHandler ) {
		val wrapper = new FailureEventHandlerFactory<V>( failureHandler );
		return whenNotFound( wrapper );
	}

	@Override
	public ConnectionHandler whenNotFound( final AsyncEventHandler asyncEvent ) {
		val wrapper = AsyncEventHandlerWrapper.wrap( asyncEvent );
		return whenNotFound( wrapper );
	}

	@Override
	public ConnectionHandler whenNotFound( final EventHandler event ) {
		this.dispatcher.whenNoEventWasFound( event );
		return this;
	}

	@Override
	public ConnectionHandler defaultExceptionHandler( final EventHandler event ) {
		this.dispatcher.defaultExceptionEventHandler( event );
		return this;
	}

	@Override
	public DefaultConnectionHandler register( ConnectionEventHandler asyncOperation ) {
		final String url = createUrl( asyncOperation );
		log.info( "Registering " + url + " to " + asyncOperation.getClass().getCanonicalName() );
		this.dispatcher.on( url, asyncOperation );
		return this;
	}

	protected String createUrl( final Connection connection ) {
		final ConnectionRequest request = connection.request();
		return createUrl( request.url(), request.method() );
	}

	protected String createUrl( final ConnectionEventHandler operation ) {
		final String url = ( SLASH + operation.url() + SLASH ).replaceAll("/+", SLASH);
		return createUrl( url, operation.method() );
	}

	protected String createUrl( final String url, final String method ) {
		return method + "::" + url;
	}
}
