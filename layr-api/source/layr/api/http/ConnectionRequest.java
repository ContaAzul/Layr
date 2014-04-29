package layr.api.http;

import java.io.InputStream;
import java.util.Map;

public interface ConnectionRequest {

	InputStream readAsStream();

	Map<String, String> cookies();

	Map<String, String[]> headers();

	String headers(String headerName);

	String headers(String headerName, String defaultValue);

	String method();

	Map<String, String[]> parameters();

	String url();

	String contentType();
	
	Long contentLength();

	String encoding();

}