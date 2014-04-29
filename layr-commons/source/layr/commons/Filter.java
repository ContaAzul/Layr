package layr.commons;

public interface Filter<T> {

	boolean shouldFilter( T item );

}
