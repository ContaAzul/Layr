package layr.api.routing;

import java.util.Map;

public interface Response {

	/**
	 * Retrieves a URL the client should redirect to.
	 * 
	 * @return
	 */
	String redirectTo();

	/**
	 * Retrieves defined response headers.
	 * 
	 * @return
	 */
	Map<String, String> headers();

	/**
	 * Retrieves the defined encoding.
	 * 
	 * @return
	 */
	String encoding();

	/**
	 * Retrieves the status code.
	 * 
	 * @return
	 */
	Integer statusCode();

	/**
	 * Retrieves the defined parameters. This parameters MAY be used
	 * by the chosen {@link OutputRenderer}.
	 * 
	 * @return
	 */
	Map<String, Object> parameters();

	/**
	 * Retrieves a object defined as parameters. This parameters MAY be used
	 * by the chosen {@link OutputRenderer}. Usually, JSON/Template renderer's
	 * used to use this object during its serialization process, then sending it
	 * to the client.
	 *  
	 * @return
	 */
	Object parameterObject();

	/**
	 * @return a template name when needed by its respective {@link OutputRenderer}. It's
	 * optional.
	 */
	String templateName();

	/**
	 * @return the response Content-Type. It is used to define which {@link OutputRenderer}
	 * will be used to render the response.
	 */
	String contentType();
}
