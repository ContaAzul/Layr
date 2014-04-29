package layr.extras.undertow;

import io.undertow.server.HttpServerExchange;

import com.texoit.undertow.standalone.api.RequestHookChain;

import layr.api.http.Connection;
import layr.api.http.ConnectionRequest;
import layr.api.http.ConnectionResponse;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
public class UndertowConnection extends Connection {
	
	final RequestHookChain chain;

	public UndertowConnection(ConnectionRequest request, ConnectionResponse response, RequestHookChain chain) {
		super(request, response);
		this.chain = chain;
	}

	public static UndertowConnection wrap( HttpServerExchange exchange, RequestHookChain chain ) {
		UndertowRequest request = new UndertowRequest( exchange );
		UndertowResponse response = new UndertowResponse( exchange );
		return new UndertowConnection( request, response, chain );
	}
}
