package layr.injection.converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterFactory {

	final Map<String, Class<? extends AbstractConverter<?>>> converters = new HashMap<String, Class<? extends AbstractConverter<?>>>();

	public ConverterFactory() throws ConversionException {
		registerKnownConverters();
	}

	@SuppressWarnings( "unchecked" )
	public void registerKnownConverters() throws ConversionException {
		register(
				ByteConverter.class, ShortConverter.class, IntegerConverter.class,
				FloatConverter.class, DoubleConverter.class, BooleanConverter.class,
				LongConverter.class, BigDecimalConverter.class, BigIntegerConverter.class,
				DateConverter.class, GregorianCalendarConverter.class, StringConverter.class );
	}

	@SuppressWarnings("unchecked")
	public void register( Class<? extends AbstractConverter<?>>... converters ) throws ConversionException {
		try {
		for ( Class<? extends AbstractConverter<?>> converter : converters )
			register( converter.newInstance() );
		} catch ( IllegalAccessException | InstantiationException cause ) {
			throw new ConversionException(cause);
		}
	}

	@SuppressWarnings( "unchecked" )
	public void register( AbstractConverter<?> newInstance ) {
		register( newInstance.getGenericClass(),
				(Class<? extends AbstractConverter<?>>)newInstance.getClass() );
	}

	public void register( Class<?> targetClass, Class<? extends AbstractConverter<?>> converter ) {
		this.converters.put( targetClass.getCanonicalName(), converter );
	}

	@SuppressWarnings( "unchecked" )
	public <T> T decode( String value, Class<T> clazz ) throws ConversionException {
		try {
			if ( value == null || String.class.equals( clazz ) )
				return (T)value;
			AbstractConverter<T> converter = getConverterFor( clazz );
			return converter.convert( value );
		} catch ( InstantiationException| IllegalAccessException e ) {
			throw new ConversionException( String.format(
					"Can't convert '%s' to '%s'", value, clazz.getCanonicalName() ), e );
		}
	}

	@SuppressWarnings( "unchecked" )
	public <T> AbstractConverter<T> getConverterFor( Class<T> clazz )
			throws InstantiationException, IllegalAccessException, ConversionException {
		String canonicalName = clazz.getCanonicalName();
		Class<? extends AbstractConverter<T>> converterClass =
				(Class<? extends AbstractConverter<T>>)this.converters.get( canonicalName );
		if ( converterClass == null )
			throw new ConversionException("No converters defined to " + canonicalName);
		return converterClass.newInstance();
	}

}
