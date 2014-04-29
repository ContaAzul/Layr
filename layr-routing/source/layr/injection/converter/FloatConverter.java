package layr.injection.converter;



public class FloatConverter extends AbstractConverter<Float> {

	@Override
	public Float convert(String value) throws ConversionException {
		return Float.valueOf( value );
	}

}
