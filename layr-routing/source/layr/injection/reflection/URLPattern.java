package layr.injection.reflection;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLPattern {

	private static final String RE_IS_VALID_EXPRESSION = "\\{[\\w:]+?[\\w\\.]+?\\}";

	/**
	 * Extract the place holders value from URL based on URL pattern
	 * 
	 * @param pattern
	 * @param url
	 * @return
	 */
	public Map<String, String> extractMethodPlaceHoldersValueFromURL( final String pattern, final String url ) {
		final String regex = parseMethodUrlPatternToRegExp( pattern );
		final Matcher urlpatternMatcher = getMatcher( RE_IS_VALID_EXPRESSION, pattern );
		final Matcher urlMatcher = getMatcher( regex, url );
		return createPlaceHolderMapExtractingDataFromURL(
				urlpatternMatcher, urlMatcher );
	}

	/**
	 * Parse Method URL
	 * 
	 * @param pattern
	 * @return a Regular Expression that matches the URL pattern
	 */
	public String parseMethodUrlPatternToRegExp( final String pattern ) {
		final Matcher matcher = getMatcher( RE_IS_VALID_EXPRESSION, pattern );
		return matcher.replaceAll( "([^\\/]+)" );
	}

	/**
	 * @param urlpatternMatcher
	 * @param urlMatcher
	 * @return
	 */
	public Map<String, String> createPlaceHolderMapExtractingDataFromURL(
			final Matcher urlpatternMatcher,
			final Matcher urlMatcher ) {
		final Map<String, String> placeHolders = new HashMap<String, String>();

		int cursor = 1;
		if ( urlMatcher.find() )
			while ( cursor <= urlMatcher.groupCount() && urlpatternMatcher.find() ) {
				populateMapWithPlaceHoldersExtractedFromURL( placeHolders, urlpatternMatcher, urlMatcher, cursor );
				cursor++;
			}

		return placeHolders;
	}

	/**
	 * @param placeHolders
	 * @param urlpatternMatcher
	 * @param urlMatcher
	 * @param cursor
	 */
	public void populateMapWithPlaceHoldersExtractedFromURL(
			final Map<String, String> placeHolders,
			final Matcher urlpatternMatcher,
			final Matcher urlMatcher,
			final int cursor ) {
		final String value = urlMatcher.group( cursor );
		final String group = urlpatternMatcher.group();
		final String expression = group.substring( 1, group.length() - 1 );
		final String placeHolder = stripAttribute( expression )[0];
		placeHolders.put( placeHolder, value );
	}

	/**
	 * @param attribute
	 * @return
	 */
	public static String[] stripAttribute( final String attribute ) {
		if ( !attribute.contains( "." ) )
			return new String[] { attribute };
		return attribute.split( "\\.", 2 );
	}

	/**
	 * @param expression
	 * @param string
	 * @return
	 */
	public Matcher getMatcher( final String expression, final String string ) {
		final Pattern pattern = Pattern.compile( expression );
		return pattern.matcher( string );
	}
}
