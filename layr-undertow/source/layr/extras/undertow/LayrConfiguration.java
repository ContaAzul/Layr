package layr.extras.undertow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors( fluent=true )
public class LayrConfiguration {

	private final static LayrConfiguration instance = new LayrConfiguration();

	@Getter(lazy=true)
	private final ExecutorService defaultExecutorService = Executors.newFixedThreadPool( numberOfThreads() );

	@Getter(lazy=true)
	private final Integer numberOfThreads = getNumberOfThreads();

	protected Integer getNumberOfThreads() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		try {
			String numberOfThreads = System.getProperty( "layr.executor.threads",
					String.valueOf( availableProcessors ) );
			return Integer.valueOf( numberOfThreads );
		} catch ( NumberFormatException cause ) {
			cause.printStackTrace();
			return availableProcessors;
		}
	}

	public static LayrConfiguration instance(){
		return instance;
	}
}
