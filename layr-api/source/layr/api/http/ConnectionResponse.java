package layr.api.http;

import java.io.IOException;

public interface ConnectionResponse {

	ConnectionResponse encoding(String encoding);

	ConnectionResponse contentType(String contentType);

	ConnectionResponse contentLength(Long contentLength);

	ConnectionResponse statusCode(Integer statusCode);

	ConnectionResponse addCookie(Cookie cookie);

	ConnectionResponse setHeader(Header header);

	ConnectionResponse write(byte[] bytes) throws IOException;

	ConnectionResponse write(String bytes) throws IOException;

	void flush();
}