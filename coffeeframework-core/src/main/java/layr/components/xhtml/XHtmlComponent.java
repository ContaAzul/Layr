/**
 * Copyright 2011 Miere Liniel Teixeira
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
package layr.components.xhtml;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import layr.components.GenericComponent;
import layr.components.IComponent;
import layr.util.StringUtil;




/**
 * Default implementation for HTML Components.
 */
public class XHtmlComponent extends GenericComponent {
	
	private boolean selfCloseable;
	private List<String> ignoredAttributes;
	private String componentName;
	
	public XHtmlComponent() {
		ignoredAttributes = new ArrayList<String>();
	}

	public XHtmlComponent(String name) {
		super();
		setComponentName(name);
	}
	
	@Override
	protected void flush() {
		ignoredAttributes = new ArrayList<String>();
		super.flush();
	}
	
	@Override
	public void configure() throws ServletException, IOException {
		super.configure();
		ignoreAttribute("styles");
		ignoreAttribute("eventsAsJSON");
	}

	@Override
/**
 * Renders the component to the browser.
 * @throws IOException
 */
	public void render() throws IOException {
		String componentName = getComponentName();
		PrintWriter writer = layrContext.getResponse().getWriter();
		writer.append("<").append(componentName);

		for (String attr : getAttributes().keySet()) {
			if (ignoredAttributes.contains(attr))
				continue;
			String attributeValue = getAttributeAsString(attr);
			if (!StringUtil.isEmpty(attributeValue))
				writer.append(' ')
					  .append(attr)
					  .append("=\"")
					  .append(attributeValue)
					  .append("\"");
		}

		if (isSelfCloseable()) {
			writer.append(" />");
			return;
		} else
			writer.append('>');

		renderChildren();

		String textContent = getTextContent();
		if (textContent != null)
			writer.append(textContent);

		writer.append("</").append(componentName).append('>');
	}

/**
 * Renders the custom child elements. Software developers should
 * use this method to create custom child elements to their components.
 * 
 * @throws IOException 
 */
	public void renderChildren() throws IOException {
		for (IComponent child : children) {
			child.setLayrContext(getLayrContext());
			child.render();
		}
	}

/**
 * Define if the component is Self Closeable Tag Component or not. 
 * @param selfCloseable
 */
	public void setSelfCloseable(boolean selfCloseable) {
		this.selfCloseable = selfCloseable;
	}

/**
 * @see XHtmlComponent#setSelfCloseable(boolean)
 * @return
 */
	public boolean isSelfCloseable() {
		return selfCloseable;
	}
	
	public void ignoreAttribute(String attribute) {
		ignoredAttributes.add(attribute);
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentName() {
		return componentName;
	}
}
