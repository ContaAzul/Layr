package layr.dispatcher.util;

import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WrapperCursor<F,T> implements Cursor<T> {
	
	final F[] entries;
	final Wrapper<F,T> wrapper;
	int cursor;

	@Override
	public Iterator<T> iterator() {
		cursor = 0;
		return this;
	}

	@Override
	public boolean hasNext() {
		return cursor < entries.length;
	}

	@Override
	public T next() {
		F original = entries[cursor++];
		return wrapper.unwrap(original);
	}

	@Override
	public void remove() {
	}
}
