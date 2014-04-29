package layr.http.assertions.benchmarks;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;

import layr.http.assertions.HandleConnectionsThatThrowsExceptionTest.CountdownFailureHandler;

import org.junit.Test;

public class ClassReflectionTest {

	@Test
	public void grantIt(){
		Class<?> clazz = CountdownFailureHandler.class;
		ParameterizedType ptype = (ParameterizedType)clazz.getGenericSuperclass();
		assertEquals( 1, ptype.getActualTypeArguments().length );
	}
}
