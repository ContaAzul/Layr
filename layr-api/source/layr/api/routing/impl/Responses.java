package layr.api.routing.impl;

final public class Responses {

	public static RedirectResponse redirectTo( final String url ){
		return new ResponseImpl().redirectTo( url );
	}

	public static OptionsResponse template( final String template ){
		return new ResponseImpl()
			.contentType("text/html")
			.templateName( template );
	}

	public static OptionsResponse template( final String template, final String contentType ){
		return new ResponseImpl()
			.contentType(contentType)
			.templateName( template );
	}

	public static OptionsResponse render( final Object object, final String contentType ){
		return new ResponseImpl()
			.contentType(contentType)
			.parameterObject( object );
	}

	public static HeaderResponse header( final String name, final String value ){
		return new ResponseImpl().header( name, value );
	}

	public static StatusCodeResponse statusCode( final int statusCode ) {
		return new ResponseImpl().statusCode(statusCode);
	}

	public static OptionsResponse json( final Object object) {
		return new ResponseImpl()
				.contentType("application/json")
				.parameterObject( object );
	}
}
