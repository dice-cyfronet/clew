/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.impl;

import pl.cyfronet.coin.api.DocumentationService;
import pl.cyfronet.coin.impl.utils.FileUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DocumentationServiceImpl implements DocumentationService {

	private static final String DOCUMENTATION_LOCATION = "www/index.html";

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.api.CloudFacade#getDocumentation()
	 */
	@Override
	public String getDocumentation() {
		String documentationContent = FileUtils
				.getFileContent(DOCUMENTATION_LOCATION);
		return removeCorruptedChars(documentationContent);
	}

	/**
	 * Strange behavior of the OSGI loading file content. It changes ( into {.
	 * @param content Loaded documentation content.
	 * @return Corrected documentation content.
	 */
	private String removeCorruptedChars(String content) {
		return content.replace("${'", "$('").replaceAll("'}", "')")
				.replace("${init};", "$(init);");
	}
}
