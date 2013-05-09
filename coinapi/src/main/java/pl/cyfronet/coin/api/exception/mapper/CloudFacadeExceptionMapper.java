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

package pl.cyfronet.coin.api.exception.mapper;

import java.io.InputStream;
import java.util.Scanner;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class CloudFacadeExceptionMapper implements
		ResponseExceptionMapper<Throwable> {

	protected String getMessage(Response r) {
		if (r.getEntity() == null) {
			return null;
		} else {
			if (r.getEntity() instanceof InputStream) {
				return convertStreamToString((InputStream) r.getEntity());
			}
			return r.getEntity().toString();
		}
	}

	private String convertStreamToString(InputStream is) {
		// from http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
		Scanner s = new Scanner(is);
		Scanner sWithDelimiter = s.useDelimiter("\\A");
		String str = s.hasNext() ? s.next() : "";
		s.close();
		sWithDelimiter.close();
		return str;
	}
}
