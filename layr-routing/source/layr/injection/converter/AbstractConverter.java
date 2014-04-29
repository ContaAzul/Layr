package layr.injection.converter;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractConverter<T> {

	public abstract T convert( String value ) throws ConversionException;

	public T convert( InputStream value ) throws ConversionException {
		throw new ConversionException( "Impossible to convert data from InputStream." );
	}

	@SuppressWarnings( "unchecked" )
	public Class<T> getGenericClass() {
		return (Class<T>)( (ParameterizedType)getClass().getGenericSuperclass() )
				.getActualTypeArguments()[0];
	}
}