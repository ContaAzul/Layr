package layr.dispatcher.util;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.val;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent=true)
public class CharacterKeyMap<T> implements Map<Character,T>, Iterable<T> {

	public static final int NOT_FOUND = -1;

	@SuppressWarnings("unchecked")
	DataEntry<T>[] entries = new DataEntry[0];

	public void put( char ch, T data ) {
		int pos = size();
		entries = Arrays.copyOf( entries, pos + 1 );
		entries[pos] = DataEntry.with( ch, data );
	}
	
	public T get( char ch ) {
		if ( size() == 0 )
			return null;
		int index = indexOf(ch);
		if ( index != NOT_FOUND )
			return entries[index].data();
		return null;
	}
	
	public int indexOf( final Character ch ) {
		int index = 0;
		for ( DataEntry<T> entry : entries ){
			if ( entry.ch() == ch )
				return index;
			index++;
		}
		return NOT_FOUND;
	}

	@Override
	public int size() {
		return entries.length;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T get(Object key) {
		assertKeyIsACharacter(key);
		return get( (char)key );
	}

	@Override
	public T put(Character key, T value) {
		put( (char)key, value );
		return value;
	}

	private void assertKeyIsACharacter(Object key) {
		if ( !Character.class.isInstance( key ) )
			throw new InvalidParameterException( "The 'key' argument must be a character" );
	}

	@Override
	public T remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends Character, ? extends T> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Character> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<T> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<Character, T>> entrySet() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " with " + size() + " entries.";
	}

	@Override
	public Iterator<T> iterator() {
		val wrapper = new CharacterKeyWrapper<T>();
		val iterator = new WrapperCursor<>(entries, wrapper);
		return iterator;
	}

	private static class CharacterKeyWrapper<T> implements Wrapper<DataEntry<T>, T> {

		@Override
		public T unwrap(DataEntry<T> from) {
			return from.data();
		}
	}
}
