package layr.dispatcher;

import static org.junit.Assert.assertThat;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.Matcher;
import org.junit.Test;

public class MetaInfTest {

	@Test
	public void grantThatRetrieveSuperclassExceptionDataAsWell(){
		List<String> metaInf = MetaInf.from( SuperclassException.class ).extract();
		assertThat(metaInf.size(), is(3));
		assertThat( metaInf.get(0), represents(SuperclassException.class) );
	}

	@Test
	public void grantThatRetrieveEspecificExceptionDataAsWell(){
		List<String> metaInf = MetaInf.from( EspecificException.class ).extract();
		assertThat( metaInf.size(), is(4) );
		assertThat( metaInf.get(0), represents(EspecificException.class) );
		assertThat( metaInf.get(1), represents(SuperclassException.class) );
	}

	<T extends Throwable> Matcher<String> represents( Class<T> clazz ) {
		return equalTo( MetaInf.applyPrefix(clazz) );
	}

	class SuperclassException extends Exception {
		private static final long serialVersionUID = 1912660788246384768L;
	}

	class EspecificException extends SuperclassException {
		private static final long serialVersionUID = 1912660788243384768L;
	}
}
