package layr.routing.handler;

import layr.api.http.ConnectionHandler;
import layr.api.routing.DeploymentException;
import layr.api.routing.DeploymentHook;
import layr.api.routing.InjectableData;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RoutingClass;
import layr.injection.reflection.RoutingClassParser;
import layr.injection.reflection.RoutingMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class RoutingDeploymentHook implements DeploymentHook {

	@Override
	public void onDeploy( ConnectionHandler connectionHandler, InjectableData injectableData ) throws DeploymentException {
		log.info( "There is " + injectableData.webResources().size() + " WebResources to deploy" );
		for ( Class<?> webResourceClass : injectableData.webResources() )
			deploy( connectionHandler, injectableData, webResourceClass );
	}

	void deploy( ConnectionHandler connectionHandler, InjectableData injectableData,
			Class<?> webResourceClass ) throws DeploymentException {
		try {
			RoutingClassParser routingClassParser = new RoutingClassParser( injectableData, webResourceClass );
			RoutingClass routingClass = routingClassParser.parse();
			for ( RoutingMethod method : routingClass.methods() )
				deploy( connectionHandler, injectableData, webResourceClass, method );
		} catch ( ConversionException | InstantiationException | IllegalAccessException cause ) {
			throw new DeploymentException( cause );
		}
	}

	void deploy( ConnectionHandler connectionHandler, InjectableData injectableData,
			Class<?> webResourceClass, RoutingMethod method ) {
		RoutingAsyncOperation asyncOperation = new RoutingAsyncOperation( method, injectableData, webResourceClass );
		connectionHandler.register( asyncOperation );
	}

	@Override
	public void onUndeploy(
			ConnectionHandler connectionHandler,
			InjectableData injectableData )
			throws DeploymentException {
	}
}
