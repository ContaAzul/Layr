package layr.extras.undertow;

import layr.api.http.ConnectionHandler;
import layr.dispatcher.Dispatcher;
import layr.http.handler.AsyncEventHandlerWrapper;
import layr.http.handler.DefaultConnectionHandler;
import layr.routing.handler.DefaultExceptionHandler;
import layr.routing.handler.RoutingLifeCycle;
import lombok.extern.java.Log;

import com.texoit.undertow.standalone.api.DeploymentContext;
import com.texoit.undertow.standalone.api.DeploymentHook;

@Log
public class LayrDeploymentHook implements DeploymentHook {

	@Override
	public void onDeploy(DeploymentContext context) {
		DefaultConnectionHandler connectionHandler = createConnectionHandler();
		RoutingLifeCycle lifeCycle = new RoutingLifeCycle( context.availableClasses(), connectionHandler );
		context.attribute( RoutingLifeCycle.class , lifeCycle );
		lifeCycle.initialize();
		context.attribute( ConnectionHandler.class , connectionHandler );
	}

	DefaultConnectionHandler createConnectionHandler() {
		LayrConfiguration config = LayrConfiguration.instance();
		Dispatcher dispatcher = Dispatcher.handledBy( config.defaultExecutorService() );
		DefaultConnectionHandler connectionHandler = new DefaultConnectionHandler( dispatcher );
		connectionHandler.whenNotFound( ByPassNotFoundRequestHandler.create() );
		connectionHandler.defaultExceptionHandler( createDefaultEventHandler() );
		return connectionHandler;
	}

	private AsyncEventHandlerWrapper createDefaultEventHandler() {
		return AsyncEventHandlerWrapper.wrap( new DefaultExceptionHandler() );
	}

	@Override
	public void onUndeploy(DeploymentContext context) {
		try {
			RoutingLifeCycle lifeCycle = context.attribute( RoutingLifeCycle.class );
			lifeCycle.shutdown();
		} catch ( Throwable cause ) {
			log.severe( "Could not shutdown Layr." );
			cause.printStackTrace();
		}
	}
}
