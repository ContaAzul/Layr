package layr.routing.loader;

import java.io.IOException;
import java.util.List;

import layr.api.http.ConnectionHandler;
import layr.api.routing.DeploymentException;
import layr.api.routing.DeploymentHook;
import layr.api.routing.InjectableData;
import layr.commons.Functions;
import layr.commons.StringMatcherFilter;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod( Functions.class )
public abstract class AbstractTemplateDeploymentHook implements DeploymentHook {

	public List<String> getTemplateNamesByExtension( String extension ) throws IOException {
		return getTemplateNamesByRegExp( ".*\\." + extension + "$" );
	}

	public List<String> getTemplateNamesByRegExp( String regexp ) throws IOException {
		val contextClassLoader = Thread.currentThread().getContextClassLoader();
		AbstractSystemResourceLoader resourceLoader = ClassPathResourceLoader.from( contextClassLoader );
		return resourceLoader.retrieveAvailableResources()
				.filter( StringMatcherFilter.fromRegExp( regexp ) );
	}

	@Override
	public void onUndeploy(ConnectionHandler connectionHandler,
			InjectableData injectableData) throws DeploymentException {}

}
