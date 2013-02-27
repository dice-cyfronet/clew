/*
 * Copyright 2013 ACC CYFRONET AGH
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

package pl.cyfronet.coin.api.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
@XmlRootElement
public class InvocationPathInfo {

	private String atomicServiceId;
	
	private String redirectionName;

	public String getAtomicServiceId() {
		return atomicServiceId;
	}

	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}

	public String getRedirectionName() {
		return redirectionName;
	}

	public void setRedirectionName(String redirectionName) {
		this.redirectionName = redirectionName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvocationPathInfo [atomicServiceId=" + atomicServiceId
				+ ", redirectionName=" + redirectionName + "]";
	}
}
