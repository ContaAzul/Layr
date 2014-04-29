package layr.api.dispatcher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors( fluent=true )
@AllArgsConstructor
@NoArgsConstructor
public class GenericEvent implements Event {

	DispatcherContext context;

}
