package layr.injection.core;

import java.io.IOException;

import layr.api.http.Connection;
import layr.api.routing.ContentType;
import layr.api.routing.OutputRenderer;
import layr.api.routing.Response;

@ContentType( "plain/text" )
public class PlainTextOutputRenderer implements OutputRenderer {

	@Override
	public void render(Connection connection, Response response ) throws IOException {
		Object object = response.parameterObject();
		String data = null;
		if ( object != null )
			data = object.toString();
		connection.response().write( data );
	}
}
