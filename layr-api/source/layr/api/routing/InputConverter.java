package layr.api.routing;

import java.io.InputStream;

public interface InputConverter {

	Object convert( InputStream body, Class<?> targetClass );

}
