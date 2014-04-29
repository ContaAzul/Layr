package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;

public interface NodeFactory {

	<T> Node<T> nodeFrom( char key, CharCursor cursor );

}
