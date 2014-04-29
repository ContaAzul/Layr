package layr.injection.converter;

import java.util.Date;
import java.util.GregorianCalendar;

public class GregorianCalendarConverter extends AbstractConverter<GregorianCalendar> {

	@Override
	public GregorianCalendar convert( String value ) throws ConversionException {
		Date date = new DateConverter().convert( value );
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime( date );
		return calendar;
	}

}
