package layr.injection.reflection.parameters;

import java.lang.annotation.Annotation;

import layr.api.routing.CookieParameter;
import layr.api.routing.Data;
import layr.api.routing.HeaderParameter;
import layr.api.routing.InjectableData;
import layr.api.routing.PathParameter;
import layr.api.routing.QueryParameter;
import layr.injection.converter.AbstractConverter;
import layr.injection.converter.ConversionException;
import layr.injection.converter.ConverterFactory;

public class RoutingParameters {

	final ConverterFactory converters;
	final InjectableData injectableData;

	public RoutingParameters( final InjectableData injectableData ) throws ConversionException {
		this.converters = new ConverterFactory();
		this.injectableData = injectableData;
	}

	/**
	 * Parse method parameter, and returns its RoutingParameter implementation.
	 * 
	 * @param parameterType
	 * @param annotations
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ConversionException 
	 * @throws IllegalArgumentException
	 *             when a method is annotated only with unknown annotations
	 */
	// @Checkstyle:OFF Many lines because it is a factory method.
	public RoutingParameter parse( Class<?> parameterType, Annotation[] annotations ) throws InstantiationException, IllegalAccessException, ConversionException {
		if ( annotations.length == 0 )
			return this.newBodyRoutingParameter( parameterType );

		if ( retrieve( annotations, Data.class ) != null )
			return this.newDataPrividedParameter( parameterType );

		QueryParameter queryParameter = retrieve( annotations, QueryParameter.class );
		if ( queryParameter != null )
			return this.newQueryRoutingParameter( queryParameter.value(), parameterType );

		PathParameter pathParameter = retrieve( annotations, PathParameter.class );
		if ( pathParameter != null )
			return this.newPathRoutingParameter( pathParameter.value(), parameterType );

		HeaderParameter headerParameter = retrieve( annotations, HeaderParameter.class );
		if ( headerParameter != null )
			return this.newHeaderRoutingParameter( headerParameter.value(), parameterType );

		CookieParameter cookieParameter = retrieve( annotations, CookieParameter.class );
		if ( cookieParameter != null )
			return this.newCookieRoutingParameter( cookieParameter.value(), parameterType );

		throw new IllegalArgumentException( "Could not find a valid RoutingParameter for this method." );
	}

	// @Checkstyle:ON

	public <T> RoutingParameter newPathRoutingParameter( final String name, final Class<T> parameterType )
			throws InstantiationException, IllegalAccessException, ConversionException {
		final AbstractConverter<T> converter = this.converters.getConverterFor( parameterType );
		return PathRoutingParameter.from( name, converter );
	}

	public <T> RoutingParameter newQueryRoutingParameter( final String name, final Class<T> parameterType )
			throws InstantiationException, IllegalAccessException, ConversionException {
		final AbstractConverter<T> converter = this.converters.getConverterFor( parameterType );
		return QueryRoutingParameter.from( name, converter );
	}

	public <T> RoutingParameter newHeaderRoutingParameter( final String name, final Class<T> parameterType )
			throws InstantiationException, IllegalAccessException, ConversionException {
		final AbstractConverter<T> converter = this.converters.getConverterFor( parameterType );
		return HeaderRoutingParameter.from( name, converter );
	}

	public <T> RoutingParameter newCookieRoutingParameter( final String name, final Class<T> parameterType )
			throws InstantiationException, IllegalAccessException, ConversionException {
		final AbstractConverter<T> converter = this.converters.getConverterFor( parameterType );
		return CookieRoutingParameter.from( name, converter );
	}

	public RoutingParameter newBodyRoutingParameter( final Class<?> parameterType ) {
		return new BodyRoutingParameter( parameterType, this.injectableData );
	}

	public RoutingParameter newDataPrividedParameter( final Class<?> parameterType ) {
		return new DataProvidedRoutingParameter( parameterType, this.injectableData );
	}

	@SuppressWarnings( "unchecked" )
	private <T> T retrieve( final Object[] objects, final Class<T> value ) {
		for ( Object object : objects )
			if ( value.isInstance( object ) )
				return (T)object;
		return null;
	}
}
