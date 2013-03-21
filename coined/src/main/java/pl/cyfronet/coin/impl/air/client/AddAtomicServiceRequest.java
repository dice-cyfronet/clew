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
package pl.cyfronet.coin.impl.air.client;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AddAtomicServiceRequest extends ApplianceTypeRequest {

	private String client;

	private List<ATEndpoint> endpoints;

	/**
	 * #1331
	 * @since 1.2.0
	 */
	private List<ATPortMapping> port_mappings;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public List<ATEndpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<ATEndpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<ATPortMapping> getPort_mappings() {
		return port_mappings;
	}

	public void setPort_mappings(List<ATPortMapping> port_mappings) {
		this.port_mappings = port_mappings;
	}
}