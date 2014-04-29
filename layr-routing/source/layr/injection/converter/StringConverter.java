package layr.injection.converter;

public class StringConverter extends AbstractConverter<String> {

	@Override
	public String convert(String value) throws ConversionException {
		return value;
	}

}
