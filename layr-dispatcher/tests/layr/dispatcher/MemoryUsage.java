package layr.dispatcher;

import java.text.DecimalFormat;

public class MemoryUsage {

	private static final double ONE_KILO_BYTES = 1024.0;

	public static String report() {
		Runtime runtime = Runtime.getRuntime();
		System.gc();
		System.gc();
		System.gc();
		return mb( runtime.freeMemory() ) + "/" + mb( runtime.totalMemory() );
	}

	private static String mb( long memory ) {
		double mb = (double)memory / ONE_KILO_BYTES / ONE_KILO_BYTES;
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(mb) + "Mb";
	}
	
}
