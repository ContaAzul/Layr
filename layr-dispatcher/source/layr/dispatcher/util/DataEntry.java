package layr.dispatcher.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent=true)
@ToString
@RequiredArgsConstructor(staticName="with")
public class DataEntry<T> {

	final char ch;
	final T data;

}
