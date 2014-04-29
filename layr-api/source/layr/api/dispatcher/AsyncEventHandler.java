package layr.api.dispatcher;

public interface AsyncEventHandler extends Runnable {

	@Override
	void run();

}
