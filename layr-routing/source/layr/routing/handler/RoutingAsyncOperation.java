package layr.routing.handler;

import java.io.IOException;
import java.util.Map;

import layr.api.http.Connection;
import layr.api.http.ConnectionRequest;
import layr.api.routing.ExceptionHandler;
import layr.api.routing.InjectableData;
import layr.api.routing.InjectionException;
import layr.api.routing.OutputRenderer;
import layr.api.routing.Response;
import layr.api.routing.impl.Responses;
import layr.http.handler.AbstractConnectionAsyncEvent;
import layr.http.handler.AbstractConnectionEventHandler;
import layr.injection.reflection.RouteableRequest;
import layr.injection.reflection.RoutingMethod;
import layr.injection.reflection.URLPattern;
import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class RoutingAsyncOperation extends AbstractConnectionEventHandler {

	@Delegate
	final RoutingMethod routingMethod;
	final InjectableData injectableData;
	final Class<?> targetClass;

	@Override
	public ConnectionAsyncEvent handle( Connection connection ) {
		return ConnectionAsyncEvent.with(
				this.routingMethod,
				this.injectableData,
				this.targetClass );
	}

	@RequiredArgsConstructor( staticName = "with" )
	public static class ConnectionAsyncEvent extends AbstractConnectionAsyncEvent {

		final RoutingMethod routingMethod;
		final InjectableData injectableData;
		final Class<?> targetClass;

		@Override
		public void run( Connection connection ) throws InjectionException, IOException {
			try {
				RouteableRequest request = createRoutableRequestFrom( connection );
				Object instance = this.injectableData.instantiate( this.targetClass );
				Object response = invokeRequestMethod( connection, request, instance);
				tryRenderResponse( connection, response );
				connection.response().flush();
			} finally {
			}
		}

		protected RouteableRequest createRoutableRequestFrom( Connection conn ) {
			ConnectionRequest request = conn.request();
			URLPattern urlPattern = new URLPattern();
			Map<String, String> placeHolders = urlPattern.extractMethodPlaceHoldersValueFromURL(
					this.routingMethod.url(), request.url() );
			return new RouteableRequest( request, placeHolders );
		}

		protected Object invokeRequestMethod( Connection connection, RouteableRequest request, Object instance) throws InjectionException {
			try {
				return this.routingMethod.invoke( request, instance );
			} catch (Throwable cause) {
				return handleRoutingInvocationFailure(connection, cause);
			}
		}

		protected Object handleRoutingInvocationFailure(Connection connection, Throwable cause) throws InjectionException {
			Throwable rootCause = cause;
			if ( rootCause.getCause() != null )
				rootCause = rootCause.getCause();
			ExceptionHandler<Throwable> exceptionHandler =
					(ExceptionHandler<Throwable>) injectableData.exceptionHandlerFor( rootCause );
			return exceptionHandler.render( connection, rootCause );
		}

		protected void tryRenderResponse( Connection conn, Object possibleResponse )
					throws InjectionException, IOException {
			Response response = newResponseFrom( possibleResponse );
			tryRenderResponse(conn, response);
		}

		protected void tryRenderResponse(Connection conn, Response response)
				throws InjectionException, IOException {
			String contentType = response.contentType() != null
					? response.contentType()
					: routingMethod.contentType();
			OutputRenderer outputRenderer = injectableData.outputRendererFor( contentType );
			outputRenderer.render(conn, response);
		}

		protected Response newResponseFrom( Object object ) {
			if ( object instanceof Response )
				return (Response)object;
			return Responses.render(object, null).build();
		}
	}
}
