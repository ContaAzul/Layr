package layr.api.routing;

import layr.api.http.ConnectionHandler;

public interface DeploymentHook {

	void onDeploy( ConnectionHandler connectionHandler, InjectableData injectableData ) throws DeploymentException;

	void onUndeploy( ConnectionHandler connectionHandler, InjectableData injectableData ) throws DeploymentException;
}