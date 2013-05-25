package layr.engine;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import layr.engine.components.ComponentFactory;
import layr.engine.components.template.TemplateComponentFactory;
import layr.engine.components.xhtml.XHtmlComponentFactory;


public class StubRequestContext extends AbstractRequestContext {
	
	StringWriter writer;
	String requestURI;
	Map<String, String> requestParameters;
	String requestHttpMethod;
	String redirectedURL;
	int statusCode;
	boolean isAsyncRequest = false;

	public StubRequestContext() {
		writer = new StringWriter();
		populateWithDefaultTagLibs();
		createCache();
	}

	public void createCache() {
		Cache cache = new Cache();
		setCache(cache);
	}

	public void populateWithDefaultTagLibs() {
		HashMap<String, ComponentFactory> registeredTagLibs = new HashMap<String, ComponentFactory>();
		XHtmlComponentFactory xHtmlComponentFactory = new XHtmlComponentFactory();
		registeredTagLibs.put("", xHtmlComponentFactory);
		registeredTagLibs.put("http://www.w3.org/1999/xhtml", xHtmlComponentFactory);
		registeredTagLibs.put("urn:layr:template", new TemplateComponentFactory());
		setRegisteredTagLibs( registeredTagLibs );
	}

	public String getBuffedWroteContentToOutput(){
		return writer.getBuffer().toString();
	}

	@Override
	public void setContentType(String contentType) {
		
	}

	@Override
	public void setCharacterEncoding(String encoding) {}

	@Override
	public Writer getWriter() {
		return writer;
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	@Override
	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}

	@Override
	public Object convert(String value, Class<?> targetClass) {
		return value;
	}

	@Override
	public String getApplicationRootPath() {
		return "/";
	}

	@Override
	public String getRequestHttpMethod() {
		return requestHttpMethod;
	}
	
	public void setRequestHttpMethod(String requestHttpMethod) {
		this.requestHttpMethod = requestHttpMethod;
	}

	@Override
	public void redirectTo(String redirectTo) {
		this.redirectedURL = redirectTo;
	}
	
	public String getRedirectedURL() {
		return redirectedURL;
	}

	@Override
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public boolean isAsyncRequest() {
		return isAsyncRequest;
	}
}