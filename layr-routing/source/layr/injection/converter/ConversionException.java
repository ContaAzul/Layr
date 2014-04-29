package layr.injection.converter;

import java.io.IOException;

public class ConversionException extends IOException {

	private static final long serialVersionUID = 2695056089411684745L;
	
	public ConversionException(String message, Throwable cause) {
		super( message, cause );
	}

	public ConversionException(String message) {
		super( message );
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}
}
