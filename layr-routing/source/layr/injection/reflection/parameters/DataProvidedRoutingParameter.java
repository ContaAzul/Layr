package layr.injection.reflection.parameters;

import layr.api.routing.DataProvider;
import layr.api.routing.InjectableData;
import layr.api.routing.InjectionException;
import layr.injection.converter.ConversionException;
import layr.injection.reflection.RouteableRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DataProvidedRoutingParameter implements RoutingParameter {

	final Class<?> targetClass;
	final InjectableData injectableData;

	@Override
	public Object extractValueFromRequest( RouteableRequest request )
			throws ConversionException {
		try {
			DataProvider<?> dataProvider = this.injectableData.dataProviderFor( this.targetClass );
			return dataProvider.newInstance( request );
		} catch ( InjectionException cause ) {
			throw new ConversionException( cause );
		}
	}
}
