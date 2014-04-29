package layr.injection.reflection.parameters;

import java.lang.annotation.Annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent=true)
@RequiredArgsConstructor
public class Parameter {
	final Class<?> clazz;
	final Annotation[] annotations;
}