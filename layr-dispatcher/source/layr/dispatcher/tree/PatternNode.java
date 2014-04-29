package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;
import lombok.ToString;

@ToString
public class PatternNode<T> extends CharNode<T> {

	Character nextChar;

	public PatternNode(NodeFactory factory) {
		super(factory, Patterns.WILDCARD);
	}

	@Override
	public Node<T> get(Character character, CharCursor cursor) {
		if ( cursor.moveTo( nextChar ) )
			return children().get(nextChar);
		return null;
	}

	@Override
	protected Node<T> getOrCreateNode(Character ch, CharCursor key) {
		if ( nextChar == null )
			nextChar = ch;
		return super.getOrCreateNode(nextChar, key);
	}
}
