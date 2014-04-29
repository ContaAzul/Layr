package layr.extras.undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import layr.api.http.ConnectionRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import org.xnio.channels.StreamSourceChannel;

@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor
public class UndertowRequest implements ConnectionRequest {

	@Getter( lazy=true )
	private final Map<String, String> cookies = readCookies();
	
	@Getter( lazy=true )
	private final Map<String, String[]> headers = readHeaders();
	
	@Getter( lazy=true )
	private final String method = exchange().getRequestMethod().toString();
	
	@Getter( lazy=true )
	private final String url = exchange().getRequestPath();
	
	@Getter( lazy=true )
	private final String contentType = headers( Headers.CONTENT_TYPE_STRING );
	
	@Getter( lazy=true )
	private final String encoding = headers( Headers.CONTENT_ENCODING_STRING, Charset.defaultCharset().toString() );
	
	@Getter( lazy=true )
	private final Map<String, String[]> parameters = readParameters();
	
	@Getter( lazy=true )
	private final Long contentLength = exchange().getResponseContentLength();

	final HttpServerExchange exchange;

	public StreamSourceChannel requestChannel(){
		return exchange.getRequestChannel();
	}

	@Override
	public InputStream readAsStream() {
		if ( exchange.isBlocking() )
			exchange.startBlocking();
		return exchange.getInputStream();
	}

	public Map<String, String> readCookies() {
		Map<String, String> cookies = new HashMap<>();
		Map<String, Cookie> requestCookies = exchange.getRequestCookies();
		for ( Cookie cookie : requestCookies.values() )
			cookies.put( cookie.getName() ,cookie.getValue() );
		return cookies;
	}

	public Map<String, String[]> readHeaders() {
		Map<String, String[]> headers = new HashMap<>();
		HeaderMap headerMap = exchange.getRequestHeaders();
		for ( HeaderValues values : headerMap )
			headers.put( values.getHeaderName().toString(), values.toArray() );
		return headers;
	}
	
	@Override
	public String headers( String headerName ) {
		String[] headers = headers().get( headerName );
		if ( headers != null && headers.length > 0 )
			return headers[0];
		return null;
	}
	
	public String headers( String headerName, String defaultValue ) {
		String header = headers( headerName );
		if ( header == null || header.isEmpty() )
			return defaultValue;
		return header;
	}

	public Map<String, String[]> readParameters() {
		Map<String, String[]> parameters = new HashMap<>();
		Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
		for ( String key : queryParameters.keySet() ) {
			Deque<String> deque = queryParameters.get( key );
			parameters.put(key, deque.toArray( new String[]{} ) );
		}
		return parameters;
	}
}
