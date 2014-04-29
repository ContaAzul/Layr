package layr.api.http;

import layr.api.dispatcher.AsyncEventHandler;

public interface ConnectionAsyncEvent extends AsyncEventHandler {

	@Override
	void run();

	void run( Connection connection ) throws Exception;

	ConnectionAsyncEvent connection( Connection connection );

}