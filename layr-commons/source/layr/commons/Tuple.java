package layr.commons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent=true )
@RequiredArgsConstructor( staticName="from" )
public class Tuple<T,V> {

	final T value1;
	final V value2;

}
