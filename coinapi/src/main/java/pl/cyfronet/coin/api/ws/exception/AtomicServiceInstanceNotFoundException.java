/*
 * Copyright 2011 ACC CYFRONET AGH
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

package pl.cyfronet.coin.api.ws.exception;

import javax.xml.ws.WebFault;

/**
 * Exception which should be thrown when user tries to find atomic service
 * instance which is not instantiated on the cloud infrastructure.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@WebFault
public class AtomicServiceInstanceNotFoundException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7028013118481656340L;

}
