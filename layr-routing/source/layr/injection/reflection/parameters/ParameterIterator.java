package layr.injection.reflection.parameters;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParameterIterator implements Iterator<Parameter> {
	final Class<?>[] parameterTypes;
	final Annotation[][] parameterAnnotations;
	int cursor = 0;
	
	public boolean hasNext(){
		return cursor < parameterTypes.length;
	}

	public Parameter next(){
		return new Parameter(
				parameterTypes[cursor],
				parameterAnnotations[cursor++]);
	}

	@Override
	public void remove() {}

}

