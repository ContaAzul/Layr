package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;

public class DefaultNodeFactory implements NodeFactory {
	
	public static <T> Node<T> rootNode() {
		DefaultNodeFactory nodeFactory = new DefaultNodeFactory();
		return new CharNode<>( nodeFactory, (char)0 );
	}

	@Override
	public <T> Node<T> nodeFrom(char key, CharCursor cursor) {
		if ( key == Patterns.BEGIN )
			return patternNode(cursor);
		return new CharNode<>(this, key);
	}

	private <T> Node<T> patternNode(CharCursor cursor) {
		assertValidPatternNode( cursor );
		if ( !cursor.hasNext() )
			return new PatternAtEndNode<>(this);
		else
			return new PatternNode<>(this);
	}

	private void assertValidPatternNode( CharCursor cursor ){
		if ( !cursor.moveTo( Patterns.END ) )
			throw new RuntimeException( "Invalid or duplicated pattern: " + cursor.toString() );
	}
}
