package layr.extras.undertow;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.xnio.channels.StreamSinkChannel;

import layr.api.http.ConnectionResponse;
import layr.api.http.Cookie;
import layr.api.http.Header;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors( fluent=true )
@RequiredArgsConstructor
public class UndertowResponse implements ConnectionResponse {

	final HttpServerExchange exchange;

	@Override
	public ConnectionResponse statusCode(Integer statusCode) {
		exchange.setResponseCode(statusCode);
		return this;
	}

	@Override
	public ConnectionResponse addCookie(Cookie cookie) {
		String name = cookie.name();
		exchange.getResponseCookies().put(name, new CookieImpl(name, cookie.value()));
		return this;
	}

	@Override
	public ConnectionResponse setHeader(Header header) {
		String value = header.value();
		String name = header.name();
		return setHeader(value, name);
	}

	protected ConnectionResponse setHeader(String value, String name) {
		HttpString headerName = new HttpString( name );
		exchange.getResponseHeaders().put( headerName, value );
		return this;
	}

	@Override
	public ConnectionResponse write(byte[] bytes) throws IOException {
		if ( exchange.isComplete() ) {
			throw new IOException("Exchange já estava fechado. =/");
		}
		StreamSinkChannel channel = exchange.getResponseChannel();
		channel.write( ByteBuffer.wrap(bytes) );
		return this;
	}

	@Override
	public ConnectionResponse write( String string ) throws IOException {
		write( string .getBytes() );
		return this;
	}

	@Override
	public ConnectionResponse encoding(String encoding) {
		return setHeader( Headers.CONTENT_ENCODING_STRING, encoding );
	}

	@Override
	public ConnectionResponse contentType(String contentType) {
		return setHeader( Headers.CONTENT_TYPE_STRING, contentType );
	}

	@Override
	public ConnectionResponse contentLength(Long contentLength){
		exchange.setResponseContentLength(contentLength);
		return this;
	}

	@Override
	public void flush() {
		if ( !exchange.isComplete() )
			exchange.endExchange();
	}
}
