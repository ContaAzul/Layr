package layr.api.routing.impl;

import java.util.HashMap;
import java.util.Map;

import layr.api.routing.Response;

public class ResponseImpl
implements 
	OptionsResponse, RedirectResponse,
	HeaderResponse, StatusCodeResponse, Response {

	String contentType;
	String redirectTo;
	Integer statusCode;
	String encoding;
	Map<String, String> headers;
	Map<String, Object> parameters;
	Object parameterObject;
	String templateName;

	public ResponseImpl() {
		headers = new HashMap<String, String>();
		parameters = new HashMap<String, Object>();
	}

	public OptionsResponse templateName(final String object) {
		this.templateName = object;
		return this;
	}

	public String templateName(){
		return templateName;
	}

	public ResponseImpl redirectTo(final String url) {
		this.redirectTo = url;
		return this;
	}

	public String redirectTo() {
		return redirectTo;
	}

	public ResponseImpl header(final String name, final String value) {
		this.headers.put(name, value);
		return this;
	}

	public Map<String, String> headers() {
		return headers;
	}

	public ResponseImpl encoding(final String encoding) {
		this.encoding = encoding;
		return this;
	}

	public String encoding() {
		return encoding;
	}

	public ResponseImpl statusCode(final int statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public Integer statusCode() {
		return statusCode;
	}

	public ResponseImpl parameterObject(final Object parameters) {
		this.parameterObject = parameters;
		return this;
	}

	public ResponseImpl set(final String name, final Object value) {
		this.parameters.put(name, value);
		return this;
	}

	public Map<String, Object> parameters() {
		return parameters;
	}

	public Object parameterObject() {
		return parameterObject;
	}
	
	public ResponseImpl contentType(final String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	@Override
	public String contentType() {
		return contentType;
	}

	@Override
	public ResponseImpl build() {
		return this;
	}
}
