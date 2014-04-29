package layr.injection.converter;



public class BooleanConverter extends AbstractConverter<Boolean> {

	@Override
	public Boolean convert(String value) throws ConversionException {
		return Boolean.valueOf( value );
	}

}
