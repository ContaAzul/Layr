package layr.extras.undertow;

import io.undertow.server.HttpServerExchange;
import layr.api.http.Connection;
import layr.api.http.ConnectionHandler;

import com.texoit.undertow.standalone.api.RequestHook;
import com.texoit.undertow.standalone.api.RequestHookChain;
import com.texoit.undertow.standalone.api.UndertowStandaloneException;

public class LayrRequestHook implements RequestHook {

	@Override
	public void execute( RequestHookChain chain, HttpServerExchange exchange ) throws UndertowStandaloneException {
		ConnectionHandler connectionHandler = chain.context().attribute( ConnectionHandler.class );
		Connection connection = UndertowConnection.wrap(exchange, chain);
		connectionHandler.handle(connection);
	}
}
