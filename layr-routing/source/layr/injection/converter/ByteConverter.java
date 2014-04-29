package layr.injection.converter;



public class ByteConverter extends AbstractConverter<Byte> {

	@Override
	public Byte convert(String value) throws ConversionException {
		return Byte.valueOf( value );
	}

}
