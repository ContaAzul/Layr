package layr.injection.converter;

import java.math.BigDecimal;

public class BigDecimalConverter extends AbstractConverter<BigDecimal> {

	@Override
	public BigDecimal convert(String value) throws ConversionException {
		return new BigDecimal( value );
	}

}
