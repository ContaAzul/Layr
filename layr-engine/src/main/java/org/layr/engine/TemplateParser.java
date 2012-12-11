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
package org.layr.engine;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.layr.commons.StringUtil;
import org.layr.engine.components.DefaultComponentFactory;
import org.layr.engine.components.IComponent;
import org.layr.engine.components.IComponentFactory;
import org.layr.engine.components.TemplateParsingException;
import org.layr.engine.components.TextNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @since Layr 1.0
 */
public class TemplateParser extends DefaultHandler {

	private IComponent rootComponent;
	private IComponent currentComponent;
	private StringBuffer textContent;
	private IRequestContext layrContext;
	private String doctype;
	private String currentTemplateName;

	public TemplateParser(IRequestContext context) {
		this.textContent = new StringBuffer();
		setLayrContext(context);
	}

	public IComponent compile(String templateName) throws TemplateParsingException {
		try{
			IComponent component = layrContext.getResourceFromCache( templateName );
			if (component != null)
				return (IComponent) component.clone(layrContext);
			component = parse(templateName);
			putInTheCacheCompiledResource(templateName, component);
			return component;
		} catch (Throwable e){
			throw new TemplateParsingException("Can't parse '" + templateName + "' as XHTML.", e);
		}
	}

	public IComponent parse(String templateName) throws ParserConfigurationException, SAXException, IOException {
		setCurrentTemplateName(templateName);
		IComponent parsedComponent = parse( layrContext.getResourceAsStream(templateName) );
		if ( parsedComponent != null )
			parsedComponent.setDocTypeDefinition(getDoctype());
		return parsedComponent;
	}

	/**
	 * @param template
	 * @param templateName 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public IComponent parse(InputStream template) throws ParserConfigurationException, SAXException, IOException {
		if ( template == null )
			return null;

		SAXParserFactory sax = SAXParserFactory.newInstance();
		sax.setValidating(false);
		sax.setNamespaceAware(true);

		SAXParser parser = sax.newSAXParser();
		parser.parse(template, this);

		return rootComponent;
	}

	/**
	 * @param templateName
	 * @param component
	 */
	public void putInTheCacheCompiledResource(String templateName, IComponent component) {
		if ( component != null )
			layrContext.putInCacheTheCompiledResource(templateName, component);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#resolveEntity(java.lang.String, java.lang.String)
	 */
	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws IOException, SAXException {
		if (publicId == null || systemId == null)
			setDoctype("<!DOCTYPE %s>");
		else
			setDoctype("<!DOCTYPE %s PUBLIC \"" + publicId + "\" \""
					+ systemId + "\">");
		return new org.xml.sax.InputSource(new java.io.StringReader(""));
	}

	@Override
	/**
	 * Stores the namespace for future component initialization.
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		if (layrContext.isRegisteredNamespace(uri))
			return;

		DefaultComponentFactory factory = new DefaultComponentFactory();
		factory.setRootDir(StringUtil.join(uri.replace("urn:", "").split(":"), "/"));
		layrContext.registerNamespace(uri, factory);
	}

	@Override
	/**
	 * Creates the current element and set its attributes. At same time it
	 * stores the current component at child hierarchy.
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		extractTextContentBeforeNesting();

		try {
			IComponent newComponent = createNewComponent(uri, localName, qName);
			if (newComponent == null)
				throw new SAXException("Can't parse the unknown element '" + localName + "'");

			setComponentAttributes(newComponent, attributes);
			defineCurrentParsedComponent(newComponent);
		} catch (InstantiationException e) {
			throw new SAXException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new SAXException(e.getMessage(), e);
		}
	}

	public void defineCurrentParsedComponent(IComponent newComponent) {
		if (currentComponent != null) {
			newComponent.setParent(currentComponent);
			currentComponent.addChild(newComponent);
		} else
			rootComponent = newComponent;
		currentComponent = newComponent;
	}

	public void setComponentAttributes(IComponent newComponent, Attributes attributes) {
		for (int i = 0; i < attributes.getLength(); i++)
			newComponent.setAttribute(attributes.getQName(i),
					attributes.getValue(i));
	}

	public IComponent createNewComponent(String uri, String localName, String qName)
			throws InstantiationException, IllegalAccessException {
		IComponentFactory factory = layrContext.getComponentFactory(uri);
		IComponent newComponent = factory.newComponent(
				localName, qName, layrContext);
		
		if ( newComponent != null )
			newComponent.setSnippetName(currentTemplateName);
		return newComponent;
	}

	/**
	 * Before nesting the next components, it's needed to save some text nodes
	 * to be rendered correctly in the HTML web page.
	 */
	public void extractTextContentBeforeNesting() {
		String string = textContent.toString();
		if (currentComponent != null
				&& !string.replaceAll("[\\n\\r]", "").trim().isEmpty()){
			currentComponent.addChild( createTextNode(string) );
			currentComponent.setTextContent(string);
		}
		textContent.delete(0, textContent.length());
	}

	public TextNode createTextNode( String string ) {
		TextNode textNode = new TextNode(string);
		textNode.setRequestContext(getLayrContext());
		return textNode;
	}

	@Override
	/**
	 * After parse a child element it stores the parentChild to the currentComponent
	 * and sets the textContent, if any.
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			extractTextContentBeforeNesting();
			currentComponent.configure();
			currentComponent = currentComponent.getParent();
		} catch (ServletException e) {
			throw new SAXException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SAXException(e.getMessage(), e);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		textContent.append(ch, start, length);
	}

	public void setRootComponent(IComponent rootComponent) {
		this.rootComponent = rootComponent;
	}

	public IComponent getRootComponent() {
		return rootComponent;
	}

	public IRequestContext getLayrContext() {
		return layrContext;
	}

	public void setLayrContext(IRequestContext layrContext) {
		this.layrContext = layrContext;
	}

    /**
     * @return the doctype
     */
    public String getDoctype() {
        return doctype;
    }

    /**
     * @param doctype the doctype to set
     */
    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

	public String getCurrentTemplateName() {
		return currentTemplateName;
	}

	public void setCurrentTemplateName(String currentTemplateName) {
		this.currentTemplateName = currentTemplateName;
	}

}
