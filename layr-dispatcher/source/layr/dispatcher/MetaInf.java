package layr.dispatcher;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * The class meta-information extractor. Internally used
 * to measure the exception class hierarchy.
 *
 * @param <T>
 */
@RequiredArgsConstructor(staticName="from")
public class MetaInf<T extends Throwable> {

	static final Character THROWABLE_PREFIX = '@';

	final List<String> metaInf = new ArrayList<>();
	final Class<T> targetClass;

	public List<String> extract(){
		if ( metaInf.isEmpty() )
			extractAndPopulateClassesMetaInfList();
		return metaInf;
	}

	private void extractAndPopulateClassesMetaInfList() {
		Class<?> clazz = targetClass;
		while ( isAnalyzableClass(clazz) ) {
			populateWith( clazz );
			clazz = clazz.getSuperclass();
		}
	}

	private boolean isAnalyzableClass(Class<?> clazz) {
		return !Object.class.equals( clazz );
	}

	private void populateWith( Class<?> clazz ) {
		metaInf.add( THROWABLE_PREFIX + clazz.getCanonicalName() );
	}

	public static <T extends Throwable> String applyPrefix( Class<T> throwableClass ) {
		return THROWABLE_PREFIX + throwableClass.getCanonicalName();
	}
}
