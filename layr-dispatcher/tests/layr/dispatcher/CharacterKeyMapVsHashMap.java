package layr.dispatcher;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import layr.dispatcher.util.CharacterKeyMap;

import org.junit.Before;
import org.junit.Test;

public class CharacterKeyMapVsHashMap {

	private static final int MANY_TIMES = 100000000;
	private static final int A_LETTER = 47;
	private static final int MAXIMUM_SIZE = 6;

	private Map<Character, Integer> charKeyMap = new CharacterKeyMap<>();
	private Map<Character, Integer> hashMap = new HashMap<Character, Integer>();

	@Before
	public void populateMaps(){
		populateMap(hashMap);
		populateMap(charKeyMap);
	}

	private void populateMap(Map<Character, Integer> map) {
		int i=0;
		char ch = A_LETTER;
		for ( ; i<MAXIMUM_SIZE; i++, ch++ )
			map.put(ch, i);
		assertEquals(MAXIMUM_SIZE, map.size());
	}

	@Test
	public void grantThatFindZManyTimesInCharKeyMap(){
		for ( int i=0; i<MANY_TIMES; i++) {
			for ( int j=0; j<MAXIMUM_SIZE; j++ ) {
				Integer entry = charKeyMap.get((char)(A_LETTER+j));
				assertEquals( j, (int)entry );
			}
		}
	}

	@Test
	public void grantThatFindZManyTimesInHashMap(){
		for ( int i=0; i<MANY_TIMES; i++) {
			for ( int j=0; j<MAXIMUM_SIZE; j++ ) {
				int integer = hashMap.get((char)(A_LETTER+j));
				assertEquals( j, integer );
			}
		}
	}
}
