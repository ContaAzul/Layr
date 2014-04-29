package layr.http.assertions.benchmarks;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

import lombok.NoArgsConstructor;

import org.junit.Test;

public class NewVsReflectionBenchMarkTest {

	static final int MANY_TIMES = 1000000;
	static final String TWELVE = "12";
	static final char[] PRINTABLE = "Hello World".toCharArray();

	@Test
	public void runNewBenchmark() throws Exception{
		Callable<Printable> new1 = new New();
		doBenchmark(new1);
	}

	@Test
	public void runNewInstanceReflectionBenchmark() throws Exception{
		Callable<Printable> reflection = new NewInstanceReflection();
		doBenchmark(reflection);
	}

	public void doBenchmark( Callable<Printable> creator ) throws Exception {
		for(int i = 0; i < MANY_TIMES; i++){
	        Printable printable = creator.call();
	        printable.setPrintableMessage(PRINTABLE);
			printable.toString();
	    }
	}

	static class New implements Callable<Printable> {

		@Override
		public Printable call() throws Exception {
			return new Stringable();
		}
	}

	static class NewInstanceReflection implements Callable<Printable> {

		final Constructor<Stringable> constructor;
		
		public NewInstanceReflection() throws NoSuchMethodException, SecurityException {
			constructor = Stringable.class.getConstructor();
		}

		@Override
		public Printable call() throws Exception {
			return constructor.newInstance();
		}
	}
	
	@NoArgsConstructor
	static class Stringable implements Printable {

		char[] chars;

		@Override
		public void setPrintableMessage(char[] chars) {
			this.chars = chars;
		}

		@Override
		public String toString() {
			return new String( chars );
		}
	}

	interface Printable {
		void setPrintableMessage( char[] chars );
		String toString();
	}
}
