package layr.injection.converter;



public class DoubleConverter extends AbstractConverter<Double> {

	@Override
	public Double convert(String value) throws ConversionException {
		return Double.valueOf( value );
	}

}
