package layr.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName="fromRegExp")
public class StringMatcherFilter implements Filter<String> {

	@NonNull final String regexp;

	@Override
	public boolean shouldFilter(String item) {
		return item.matches(regexp);
	}
}
