package layr.dispatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import layr.dispatcher.tree.DefaultNodeFactory;
import layr.dispatcher.tree.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// @Checkstyle:OFF Test propose only
public class SimpleTreePatternMatching {

	private static final int MANY_TIMES = 5000000;
	private Node<Object> node;

	@Test
	public void grantThatNodeWorks(){
		Object object = node.get("/company/blank/1234/editOrAdd");
		assertNotNull(object);
		assertEquals(100, object);
	}

	@Test
	public void grantThatCouldFindNodeWithPlaceholderAtTheEndOfString(){
		Object object = node.get("/pattern/with/placeholder/at-end");
		assertNotNull(object);
		assertEquals(5, object);
	}

	@Test
	public void grantThatCouldNotFound(){
		assertNull(node.get("/company/user/"));
	}

	@Test
	public void grantThatMatchesInReasonableTime(){
		for ( int i=0; i<MANY_TIMES; i++ )
			grantThatNodeWorks();
	}

	@Before
	public void oneTimeSetup(){
		printMemoryStatus("Before");
		node = DefaultNodeFactory.rootNode();
		for ( int i=0; i<600; i++ ) {
			node.set( url( "/user/list", i ), new Integer(0 + (i*5)));
			node.set( url( "/user/form", i ), new Integer(1 + (i*5)));
			node.set( url( "/company/blank/{id}/edit", i ), new Integer(2 + (i*5)));
			node.set( url( "/company/user/blankSlate", i ), new Integer(4 + (i*5)));
			node.set( url( "/tmp/user/blankSlate", i ), new Integer(5 + (i*5)));
			node.set( url( "/pattern/with/placeholder/{at-end}", i ), new Integer(5 + (i*5)));
		}
		node.set( "/company/blank/{id}/editOrAdd", new Integer(100));
	}

	String url( String url, int suffix ) {
		if ( suffix == 0 )
			return url;// + "/";
		return url + "/0" + suffix;
	}

	@After
	public void tearDown(){
		printMemoryStatus( "After" );
	}

	private void printMemoryStatus(String string) {
		System.out.println("-[" + string + "]--");
		System.out.println(MemoryUsage.report());
	}
}
