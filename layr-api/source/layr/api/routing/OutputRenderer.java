package layr.api.routing;

import java.io.IOException;

import layr.api.http.Connection;

public interface OutputRenderer {

	void render( Connection connection, Response response ) throws IOException;

}
