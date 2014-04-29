package layr.dispatcher.tree;

import layr.dispatcher.util.CharCursor;

/**
 * A tree node that holds a data. It is used to find
 * data related to a String pattern set as key.
 *
 * @param <T>
 */
public interface Node<T> {

	/**
	 * Get the respective node from String.
	 * 
	 * @param key
	 * @return
	 */
	T get(String key);

	/**
	 * Get the respective node from character.
	 * 
	 * @param key
	 * @return
	 */
	Node<T> get( CharCursor key );

	/**
	 * Get the respective node from character. Implementations
	 * could change or analyze current cursor to identify with
	 * is the correctly node it should returns.
	 * 
	 * @param character
	 * @param cursor
	 * @return
	 */
	Node<T> get( Character character, CharCursor cursor );

	/**
	 * Set data into a key.
	 * 
	 * @param key
	 * @param data
	 */
	void set( String key, T data );
	
	/**
	 * Utilizes the cursor to position the data in the correctly node.
	 * 
	 * @param key
	 * @param data
	 */
	void set( CharCursor key, T data );

	/**
	 * Retrieves the data.
	 * 
	 * @return
	 */
	T data();

	/**
	 * Retrieves the identifier key of this node.
	 * 
	 * @return
	 */
	char key();

	/**
	 * Retries a child iterator.
	 * @return
	 */
	Iterable<Node<T>> children();

}