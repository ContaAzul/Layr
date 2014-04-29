package layr.routing.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import layr.api.http.ConnectionHandler;
import layr.api.routing.DeploymentException;
import layr.api.routing.DeploymentHook;
import layr.api.routing.InjectableData;
import layr.api.routing.InjectionException;
import layr.http.handler.AbstractConnectionEventHandler;
import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;

@Log
@Getter
@Accessors( fluent = true )
public class RoutingLifeCycle {

	final List<AbstractConnectionEventHandler> foundRoutes;
	final ConnectionHandler connectionHandler;
	final AvailableClassPathClassesAnalysis classesAnalysis;
	final InjectableData injectableData;

	public RoutingLifeCycle(
			final Collection<Class<?>> classes,
			final ConnectionHandler connectionHandler ) {
		this.foundRoutes = new ArrayList<>();
		this.connectionHandler = connectionHandler;
		this.classesAnalysis = new AvailableClassPathClassesAnalysis( classes );
		val defaultInjectableDataFactory = new DefaultInjectableDataFactory( this.classesAnalysis );
		this.injectableData = defaultInjectableDataFactory.newInstance();
	}

	public ConnectionHandler initialize() {
		try {
			executeDeployHooks();
			return this.connectionHandler;
		} catch ( InjectionException | DeploymentException cause ) {
			throw new RoutingInitializationException( cause );
		}
	}

	private void executeDeployHooks() throws InjectionException, DeploymentException {
		for ( Class<DeploymentHook> deploymentHookClass : this.classesAnalysis.deploymentHooks() ) {
			DeploymentHook deploymentHook = this.injectableData.instantiate( deploymentHookClass );
			log.fine( "Layr deployment of " + deploymentHook.getClass().getCanonicalName() );
			deploymentHook.onDeploy( this.connectionHandler, this.injectableData );
		}
	}

	public void shutdown() throws Throwable {
		try {
			executeUndeployHooks();
		} catch ( InjectionException | DeploymentException cause ) {
			throw new RoutingInitializationException( cause );
		}
	}

	private void executeUndeployHooks() throws InjectionException, DeploymentException {
		for ( Class<DeploymentHook> deploymentHookClass : this.classesAnalysis.deploymentHooks() ) {
			DeploymentHook deploymentHook = this.injectableData.instantiate( deploymentHookClass );
			log.fine( "Layr undeployment of " + deploymentHook.getClass().getCanonicalName() );
			deploymentHook.onUndeploy( this.connectionHandler, this.injectableData );
		}
	}
}
