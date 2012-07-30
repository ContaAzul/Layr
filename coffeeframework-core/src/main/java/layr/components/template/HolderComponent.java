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
package layr.components.template;

import java.io.IOException;

import layr.components.GenericComponent;
import layr.components.IComponent;


public class HolderComponent extends GenericComponent {

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		for (IComponent child : getChildren()) {
			child.setLayrContext(getLayrContext());
			child.render();
		}
	}

}
