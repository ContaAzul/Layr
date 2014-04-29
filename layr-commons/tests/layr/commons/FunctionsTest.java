package layr.commons;

import static org.junit.Assert.assertTrue;
import lombok.experimental.ExtensionMethod;

import org.junit.Test;

@ExtensionMethod( Functions.class )
public class FunctionsTest {

	@Test
	public void grantThatHelloIsPresentInList(){
		assertTrue( "Hello".in( "Hello", "World" ) );
	}
}
