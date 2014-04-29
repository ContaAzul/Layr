package layr.routing.http.assertions;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import layr.api.http.ConnectionRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
@ToString
public class StubRequest implements ConnectionRequest {
	
	final String url;
	final String method;

	final InputStream body = null;
	final Map<String, String> cookies = new HashMap<>();
	final Map<String, String[]> headers = new HashMap<>();
	final Map<String, String[]> parameters = new HashMap<>();
	final String contentType = "text/html";
	final String encoding = "utf-8";

	@Override
	public InputStream readAsStream() {
		return body;
	}

	@Override
	public String headers(String headerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String headers(String headerName, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long contentLength() {
		// TODO Auto-generated method stub
		return null;
	}
}