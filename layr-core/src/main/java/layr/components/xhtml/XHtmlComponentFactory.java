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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layr.RequestContext;
import layr.annotation.TagLib;
import layr.components.IComponent;
import layr.components.IComponentFactory;


@TagLib("http://www.w3.org/1999/xhtml")
public class XHtmlComponentFactory implements IComponentFactory {
	
	private Map<String, Class<? extends IComponent>> components;
	private List<String> selfCloseableComponents;
	
	public XHtmlComponentFactory() {
		components = new HashMap<String, Class<? extends IComponent>>();
		components.put("input", Input.class);
		components.put("select", Select.class);
		components.put("option", Option.class);
		components.put("textarea", TextArea.class);
		components.put("html", Html.class);
		components.put("link", Link.class);
		components.put("img", Image.class);
		components.put("a", Anchor.class);
		components.put("form", Form.class);
		components.put("script", Script.class);

		selfCloseableComponents = new ArrayList<String>();
		selfCloseableComponents.add("br");
		selfCloseableComponents.add("meta");
	}

	@Override
	public IComponent newComponent(String name, String qName, RequestContext context) throws InstantiationException, IllegalAccessException {
		if (components.containsKey(name)) {
			IComponent newInstance = components.get(name).newInstance();
			newInstance.setLayrContext(context);
			return newInstance;
		}

		XHtmlComponent component = new XHtmlComponent();
		component.setComponentName(name);
		component.setQualifiedName(qName);
		component.setLayrContext(context);
		component.setSelfCloseable(
			selfCloseableComponents.contains(name)
		);

		return component;
	}

}
