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
package pl.cyfronet.coin.impl.utils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class FileUtils {

	/**
	 * Line separator.
	 */
	private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$
	
	public static String getFileContent(String path) {
		InputStream fileIs = getFileInputStream(path);
		return getFileContent(fileIs);
	}

	public static InputStream getFileInputStream(String path) {
		ClassLoader cl = FileUtils.class.getClassLoader();
		return cl.getResourceAsStream(path);
	}

	public static String getFileContent(InputStream is) {
		StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(is);
		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}
		return text.toString();
	}
}
