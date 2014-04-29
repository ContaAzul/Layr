package layr.dispatcher.util;

import java.util.Iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( staticName="from" )
public class CharCursor implements Cursor<Character> {

	@NonNull char[] chars;
	int cursor;
	
	public static CharCursor from( String string ) {
		return CharCursor.from( string.toCharArray() );
	}

	@Override
	public boolean hasNext() {
		return cursor < chars.length;
	}

	@Override
	public Character next() {
		if ( !hasNext() )
			return null;
		return chars[ cursor++ ];
	}

	@Override
	public void remove() {
	}

	@Override
	public Iterator<Character> iterator() {
		cursor=0;
		return this;
	}

	public Character prev() {
		return chars[cursor-1];
	}
	
	public Character current(){
		return chars[cursor];
	}

	public boolean moveTo( char ch ) {
		int oldCursor = cursor;
		
		//@Checkstyle:OFF This loop is auto-iterable. Nothing to do here.
		while ( next() != ch && hasNext() );
		//@Checkstyle:ON

		if ( prev() != ch ){
			cursor = oldCursor;
			return false;
		}

		return true;
	}
	
	@Override
	public String toString() {
		return new String( chars );
	}
}
