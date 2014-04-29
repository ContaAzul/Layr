package layr.api.http;

import layr.api.dispatcher.GenericEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent=true)
@RequiredArgsConstructor
public class Connection extends GenericEvent {

	final ConnectionRequest request;
	final ConnectionResponse response;

}
