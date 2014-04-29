package layr.injection.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends AbstractConverter<Date> {

	@Override
	public Date convert( String value ) throws ConversionException {
		Date convertedDate = tryToConvertFromLong( value );
		if ( convertedDate == null )
			convertedDate = tryToConvertFromInternationDateFormat( value );
		if ( convertedDate == null )
			throwCantConvertValueToDate( value );
		return convertedDate;
	}

	private void throwCantConvertValueToDate( String value ) throws ConversionException {
		throw new ConversionException( String.format( "Can't convert '%s' to java.util.Date.", value ) );
	}

	private Date tryToConvertFromLong( String value ) {
		try {
			long parsedLong = Long.parseLong( value );
			return new Date( parsedLong );
		} catch ( NumberFormatException e ) {
			return null;
		}
	}

	private Date tryToConvertFromInternationDateFormat( String value ) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			return dateFormat.parse( value );
		} catch ( ParseException e ) {
			return null;
		}
	}
}
