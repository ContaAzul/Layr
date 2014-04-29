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
public class SimpleTreeMatching {

	private static final int MANY_TIMES = 4500000;

	private Node<Object> node;

	@Test
	public void grantThatNodeWorks(){
		Object object = node.get("/company/user/blankSlate/0100");
		assertNotNull(object);
		assertEquals(100, object);
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
		for ( int i=0; i<1000; i++ ) {
			node.set( url( "/user/list", i ), new Integer(0 + (i*5)));
			node.set( url( "/user/form", i ), new Integer(1 + (i*5)));
			node.set( url( "/log/user/edit", i ), new Integer(2 + (i*5)));
			node.set( url( "/company/user/blankSlate", i ), new Integer(4 + (i*5)));
			node.set( url( "/tmp/user/blankSlate", i ), new Integer(5 + (i*5)));
			node.set( url( "/company/blank/blank/blankSlate", i ), new Integer(6 + (i*6)));
			node.set( url( "/company/notso/blank/blank/blankSlate", i ), new Integer(6 + (i*7)));
			node.set( url( "/notso/blank/blank/blankSlate", i ), new Integer(6 + (i*7)));
			node.set( url( "/blankSlate/notso/blank/blank/blankSlate", i ), new Integer(6 + (i*8)));
		}
		node.set( url( "/company/user/blankSlate", 100 ), new Integer(100));
	}

	String url( String url, int suffix ) {
		if ( suffix == 0 )
			return url;
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
