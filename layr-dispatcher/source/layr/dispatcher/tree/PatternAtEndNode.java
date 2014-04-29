package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;
import lombok.ToString;

@ToString
public class PatternAtEndNode<T> extends CharNode<T> {
	
	private static final char END_OF_PATTERN = '*';
	public static final char PATTERN_BEGIN = '{';
	public static final char PATTERN_END = '}';

	Character nextChar = END_OF_PATTERN;

	public PatternAtEndNode(NodeFactory factory) {
		super(factory, END_OF_PATTERN);
	}

	@Override
	public Node<T> get(Character character, CharCursor cursor) {
		moveCursorToTheEnd(cursor);
		return this;
	}

	private void moveCursorToTheEnd(CharCursor cursor) {
		while ( cursor.hasNext() )
			cursor.next();
	}

	@Override
	protected Node<T> getOrCreateNode(Character ch, CharCursor key) {
		return super.getOrCreateNode(nextChar, key);
	}
}
