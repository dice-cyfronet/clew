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

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class BeanConverter {

	public static AtomicService getAtomicService(ApplianceType applianceType) {
		AtomicService atomicService = new AtomicService();
		atomicService.setAtomicServiceId(applianceType.getId());
		atomicService.setDescription(applianceType.getDescription());
		atomicService.setName(applianceType.getName());
		atomicService.setShared(applianceType.isShared());
		atomicService.setScalable(applianceType.isScalable());
		atomicService.setPublished(applianceType.isPublished());
		atomicService.setActive(applianceType.getTemplates_count() > 0);
		atomicService.setEndpoints(getEndpoints(applianceType));
		atomicService.setDevelopment(applianceType.isDevelopment());
		atomicService.setProxyConfigurationName(applianceType
				.getProxy_conf_name());

		// #1433
		atomicService.setOwner(applianceType.getAuthor());

		return atomicService;
	}

	public static List<Endpoint> getEndpoints(ApplianceType applianceType) {
		List<ATEndpoint> atEndpoints = applianceType.getEndpoints();
		List<Endpoint> asEndpoints = new ArrayList<Endpoint>();
		if (atEndpoints != null) {
			for (ATEndpoint atEndpoint : atEndpoints) {
				asEndpoints.add(getEndpoint(atEndpoint));
			}
		}

		return asEndpoints;
	}

	private static Endpoint getEndpoint(ATEndpoint atEndpoint) {
		Endpoint asEndpoint = new Endpoint();
		asEndpoint.setDescription(atEndpoint.getDescription());
		asEndpoint.setDescriptor(atEndpoint.getDescriptor());
		asEndpoint.setInvocationPath(atEndpoint.getInvocation_path());
		asEndpoint.setPort(atEndpoint.getPort());
		asEndpoint.setType(getEdnpointType(atEndpoint.getEndpoint_type()));
		asEndpoint.setId(atEndpoint.getId());

		return asEndpoint;
	}

	private static EndpointType getEdnpointType(String endpoint_type) {
		if ("WS".equalsIgnoreCase(endpoint_type)) {
			return EndpointType.WS;
		} else if ("WEBAPP".equalsIgnoreCase(endpoint_type)) {
			return EndpointType.WEBAPP;
		} else {
			return EndpointType.REST;
		}
	}
}
