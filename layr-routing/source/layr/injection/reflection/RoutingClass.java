package layr.injection.reflection;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors( fluent = true )
@RequiredArgsConstructor
public class RoutingClass {

	final List<RoutingMethod> methods;

}
