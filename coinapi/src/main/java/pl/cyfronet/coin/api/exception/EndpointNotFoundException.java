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
package pl.cyfronet.coin.api.exception;

import javax.ws.rs.core.Response;
import javax.xml.ws.WebFault;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@WebFault
public class EndpointNotFoundException extends CloudFacadeException {

	private static final long serialVersionUID = 3805274312799326244L;

	public static final String ERROR_MESSAGE = "Endpoint not found";
	
	public EndpointNotFoundException() {
		super(ERROR_MESSAGE, Response.Status.NOT_FOUND);
	}
}
