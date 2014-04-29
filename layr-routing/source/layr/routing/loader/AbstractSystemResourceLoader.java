package layr.routing.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSystemResourceLoader {

	public List<Class<?>> retrieveAvailableClasses () throws IOException, ClassNotFoundException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		for (String resource : retrieveAvailableResources ())
			try {
				if (resource.endsWith(".class") && !resource.contains("$")){
					Class<?> clazz = getClassLoader().loadClass(
							normalizeClassName(resource));
					classes.add(clazz);
				}
			} catch (Throwable e) {}

		return classes;
	}

	public abstract List<String> retrieveAvailableResources() throws IOException;

	public String normalizeClassName (String className) {
		return className
				.replace("/WEB-INF/classes/", "")
				.replace(".class", "")
				.replaceAll("^/+", "")
				.replace("/", ".");
	}

	abstract ClassLoader getClassLoader();

}
