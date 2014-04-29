package layr.routing.loader.assertions;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import layr.commons.Functions;
import layr.commons.StringMatcherFilter;
import layr.routing.loader.AbstractSystemResourceLoader;
import layr.routing.loader.ClassPathResourceLoader;
import lombok.experimental.ExtensionMethod;

import org.junit.Test;

@ExtensionMethod( Functions.class )
public class GrantThatCanFindAvailablesTemplatesInClassPath {

	@Test
	public void grantIt() throws IOException{
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		AbstractSystemResourceLoader resourceLoader = ClassPathResourceLoader.from( contextClassLoader );
		List<String> templates = resourceLoader.retrieveAvailableResources()
				.filter( StringMatcherFilter.fromRegExp(".*.xhtml$") );
		assertEquals(1, templates.size());
	}
}
