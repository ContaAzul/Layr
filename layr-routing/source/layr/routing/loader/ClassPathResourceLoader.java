package layr.routing.loader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter( AccessLevel.PROTECTED )
@RequiredArgsConstructor( staticName="from" )
public class ClassPathResourceLoader extends AbstractSystemResourceLoader {

	private static final String DEFAULT_RESOURCE_PATH = "";
	final ClassLoader classLoader;

	public List<String> retrieveAvailableResources() throws IOException {
		ArrayList<String> list = new ArrayList<String>();

		Enumeration<URL> resources = getClassLoader().getResources(DEFAULT_RESOURCE_PATH);
		while (resources.hasMoreElements()) {
			File file = openFileFrom( resources.nextElement() );
			if (file.exists())
				list.addAll(
					retrieveAvailableResources(file, DEFAULT_RESOURCE_PATH));
		}

		return list;
	}

	private File openFileFrom(URL resource) throws UnsupportedEncodingException {
		String fileName = resource.getFile();
		String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
		File file = new File(fileNameDecoded);
		return file;
	}

	public List<String> retrieveAvailableResources(File directory, String path) {
		ArrayList<String> list = new ArrayList<String>();

		File[] listOfFiles = directory.listFiles();
		if ( listOfFiles != null )
			for (File file : listOfFiles) {
	        	String fileName = path + "/" + file.getName();
	            if (file.isDirectory())
	            	list.addAll(
	        			retrieveAvailableResources(file, fileName));
	            else
	            	list.add(fileName);
	        }

		return list;
	}
}
