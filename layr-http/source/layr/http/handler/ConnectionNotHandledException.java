package layr.http.handler;

import layr.api.http.Connection;

public class ConnectionNotHandledException extends Throwable {
	
	private static final long serialVersionUID = 1095535691054080633L;

	final Connection data;

	public ConnectionNotHandledException(Connection data) {
		this.data = data;
	}
}
