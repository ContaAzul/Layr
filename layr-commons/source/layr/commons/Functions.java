package layr.commons;

import java.util.ArrayList;
import java.util.List;

public class Functions {

	@SafeVarargs
	public static <T> List<T> filter( List<T> self, Filter<T>...filter ) {
		List<T> newList = new ArrayList<T>();
		for ( T item : self )
			if ( shouldFilter(item, filter) )
				newList.add(item);
		return newList;
	}

	@SafeVarargs
	static <T> boolean shouldFilter( T item, Filter<T>...filters ){
		for ( Filter<T> filter : filters )
			if ( filter.shouldFilter(item) )
				return true;
		return false;
	}

	@SafeVarargs
	public static <T> boolean in( T self, T...list ){
		for ( T t : list )
			if ( t == self )
				return true;
		return false;
	}
}
