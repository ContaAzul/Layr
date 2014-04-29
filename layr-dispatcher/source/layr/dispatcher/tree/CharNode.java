package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;
import layr.dispatcher.util.CharacterKeyMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
@ToString
@RequiredArgsConstructor
public class CharNode<T> implements Node<T> {

	final NodeFactory factory;
	final char key;

	T data;
	CharacterKeyMap<Node<T>> children = new CharacterKeyMap<>();

	/* (non-Javadoc)
	 * @see layr.http.handler.matcher.Node#get(java.lang.String)
	 */
	@Override
	public T get( String key ) {
		CharCursor cursor = CharCursor.from(key);
		Node<T> node = get(cursor);
		if ( node == null )
			return null;
		return node.data();
	}

	/* (non-Javadoc)
	 * @see layr.http.handler.matcher.tree.Node#get(layr.http.handler.matcher.tree.CharCursor)
	 */
	@Override
	public Node<T> get( CharCursor cursor ) {
		Node<T> node = this;
		while ( node != null && cursor.hasNext() )
			node = getNextNode(cursor, node);
		return node;
	}

	/**
	 * Retrieves the next node.
	 * 
	 * @param cursor
	 * @param node
	 * @return
	 */
	protected Node<T> getNextNode( CharCursor cursor, Node<T> node) {
		return node.get(cursor.next(), cursor);
	}

	/* (non-Javadoc)
	 * @see layr.http.handler.matcher.tree.Node#get(java.lang.Character, layr.http.handler.matcher.tree.CharCursor)
	 */
	public Node<T> get( Character character, CharCursor cursor ) {
		Node<T> node = children.get(character);
		if ( node == null )
			return children.get('*');
		return node;
	}

	/* (non-Javadoc)
	 * @see layr.http.handler.matcher.Node#set(java.lang.String, T)
	 */
	@Override
	public void set( String key, T data ) {
		set( CharCursor.from(key), data );
	}

	/* (non-Javadoc)
	 * @see layr.http.handler.matcher.tree.Node#set(layr.http.handler.matcher.tree.CharCursor, java.lang.Object)
	 */
	@Override
	public void set( CharCursor key, T data ) {
		if ( !key.hasNext() ){
			this.data = data;
			return;
		}

		Character ch = key.next();
		Node<T> node = getOrCreateNode( ch, key );
		node.set( key, data );
	}

	/**
	 * Retrieves a node associated to the 'ch' parameter. It no node was
	 * found it will create one, memorize it, and then returns. Internally,
	 * it analyzes the 'key' parameter to identify if there's a best key
	 * to memorize the node instead of current 'ch' argument.
	 * 
	 * @param ch
	 * @param key
	 * @return
	 */
	protected Node<T> getOrCreateNode( Character ch, CharCursor key ) {
		Node<T> node = retrieveNodeFromCharOrAsPattern(ch, key);
		if ( node == null )
			node = createNode(ch, key);
		return node;
	}

	/**
	 * Retrieve node from char or from wildcard, if is parsing a pattern.
	 * 
	 * @param ch
	 * @param key
	 * @return
	 */
	private Node<T> retrieveNodeFromCharOrAsPattern(Character ch, CharCursor key) {
		if ( ch == Patterns.BEGIN )
			return retrieveNodeFromWildcard(key);
		else
			return children.get(ch);
	}

	/**
	 * Retrieve node from wildcard.
	 * 
	 * @param key
	 * @return
	 */
	private Node<T> retrieveNodeFromWildcard(CharCursor key) {
		Node<T> node = children.get(Patterns.WILDCARD);
		if ( node != null )
			key.moveTo(Patterns.END);
		return node;
	}

	/**
	 * Create a node based on key. It uses the NodeFactory to instantiate
	 * the new Node.
	 * 
	 * @param ch
	 * @param key2 
	 * @return
	 */
	protected Node<T> createNode( Character ch, CharCursor key ) {
		Node<T> node = factory.nodeFrom( ch, key );
		children.put( node.key(), node );
		return node;
	}
}
