package layr.injection.reflection;

import java.util.Map;

import layr.api.http.ConnectionRequest;
import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class RouteableRequest implements ConnectionRequest {

	@Delegate
	final ConnectionRequest request;
	final Map<String, String> placeHolders;

}
