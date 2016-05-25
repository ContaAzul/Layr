/**
 * Copyright 2012 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.layr.jee.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.layr.commons.Cache;
import org.layr.engine.AbstractRequestContext;
import org.layr.engine.components.IComponentFactory;

public class JEERequestContext extends AbstractRequestContext {

	private JEEConfiguration configuration;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public JEERequestContext(
			HttpServletRequest request, HttpServletResponse response,
			JEEConfiguration configuration ) {
		super();
		setConfiguration(configuration);
		setRequest(request);
		setResponse(response);
	}

	/* (non-Javadoc)
	 * @see layr.core.IRequestContext#getParameter(java.lang.String)
	 */
	@Override
	public Object getParameter(String paramName) throws ServletException,
			IOException {
		return getRequest().getParameter(paramName);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ServletContext getServletContext() {
		return configuration.getServletContext();
	}

	/* (non-Javadoc)
	 * @see layr.core.IRequestContext#getRelativePath()
	 */
	@Override
	public String getRelativePath() {
		String relativePath = super.getRelativePath();
		if ( relativePath == null ) {
			String uri = request
							.getRequestURI()
							.replaceFirst(getApplicationRootPath(), "");
			if ( uri.equals("/") )
				 uri = getDefaultResourcePath();
			relativePath = uri;
		}

		return relativePath;
	}

	public String getDefaultResourcePath() {
		return configuration.getDefaultResource();
	}

	/**
	 * Retrieves the current layrContext path. For more info see
	 * {@link HttpServletRequest#getContextPath()}
	 *
	 * @return contextPath
	 */
	public String getApplicationRootPath() {
		return request.getContextPath();
	}

	/* (non-Javadoc)
	 * @see layr.core.IRequestContext#setCharacterEncoding(java.lang.String)
	 */
    @Override
	public void setCharacterEncoding(String encoding) {
        try {
			getRequest().setCharacterEncoding(encoding);
			getResponse().setCharacterEncoding(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

    /* (non-Javadoc)
	 * @see layr.core.IRequestContext#setContentType(java.lang.String)
	 */
    @Override
	public void setContentType(String contentType) {
        getResponse().setContentType(contentType);
    }

	/* (non-Javadoc)
	 * @see layr.core.IRequestContext#log(java.lang.String)
	 */
	@Override
	public void log(String text) {
		getServletContext().log(text);
	}

	@Override
	public Writer getWriter() {
		try {
			return getResponse().getWriter();
		} catch (IOException e) {
			throw new RuntimeException("Can't retrieve the response writer.", e);
		}
	}

	@Override
	public InputStream openStream(String url) {
		InputStream stream = getClassLoader().getResourceAsStream(url);
		if (stream == null && getServletContext() != null)
			stream = getServletContext().getResourceAsStream("/" + url);
		return stream;
	}

	public JEEConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JEEConfiguration configuration) {
		this.configuration = configuration;
	}

	public static JEERequestContext createRequestContext(ServletRequest request,
			ServletResponse response, ServletContext context) {
		JEEConfiguration configuration = (JEEConfiguration)context.getAttribute( JEEConfiguration.class.getCanonicalName() );
		JEERequestContext requestContext = new JEERequestContext(
				(HttpServletRequest)request, (HttpServletResponse)response, configuration);
		return configureRequestContext( requestContext );
	}

	public static JEERequestContext configureRequestContext( JEERequestContext context ) {
		JEEConfiguration configuration = context.getConfiguration();
		context.setCache( createCache( configuration ) );
        context.setRegisteredTagLibs( configuration.getRegisteredTagLibs() );

		HttpServletRequest httpRequest = context.getRequest();
		if (httpRequest  != null) {
			context.put("contextPath", httpRequest.getContextPath());
			context.put("path", httpRequest.getRequestURI());
		}

		return context;
	}

	public static Cache createCache(JEEConfiguration configuration) {
		if ( !configuration.isCacheEnabled() )
			return null;
		return configuration.getCache();
	}

	@Override
	public Map<String, IComponentFactory> getRegisteredTagLibs() {
		return configuration.getRegisteredTagLibs();
	}
}
