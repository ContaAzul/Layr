package layr.dispatcher.util;

public interface Wrapper<F,T> {

	T unwrap( F from );

}
