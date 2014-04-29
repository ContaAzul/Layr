package layr.injection.converter;

import java.math.BigInteger;

public class BigIntegerConverter extends AbstractConverter<BigInteger> {

	@Override
	public BigInteger convert(String value) throws ConversionException {
		return new BigInteger( value );
	}

}
