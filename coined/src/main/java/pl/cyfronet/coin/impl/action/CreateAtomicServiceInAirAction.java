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
package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.exception.AtomicServiceAlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AddAtomicServiceRequest;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceInAirAction implements Action<String> {

	private static Logger logger = LoggerFactory
			.getLogger(CreateAtomicServiceInAirAction.class);

	private AirClient air;
	private AtomicService atomicService;
	private String createdAtomicServiceId;

	CreateAtomicServiceInAirAction(AirClient air, AtomicService atomicService) {
		this.air = air;
		this.atomicService = atomicService;
	}

	@Override
	public String execute() throws CloudFacadeException {
		AddAtomicServiceRequest addASRequest = new AddAtomicServiceRequest();
		addASRequest.setClient("rest");
		addASRequest.setDescription(atomicService.getDescription());
		addASRequest.setEndpoints(getAsEndpoints(atomicService.getEndpoints()));
		addASRequest.setHttp(atomicService.isHttp());
		addASRequest.setIn_proxy(atomicService.isInProxy());
		addASRequest.setName(atomicService.getName());
		addASRequest.setPublished(atomicService.isPublished());
		addASRequest.setScalable(atomicService.isScalable());
		addASRequest.setShared(atomicService.isShared());
		addASRequest.setVnc(atomicService.isShared());

		try {
			createdAtomicServiceId = air.addAtomicService(addASRequest);
			return createdAtomicServiceId;
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 302) {
				throw new AtomicServiceAlreadyExistsException();
			}
			logger.warn("Error received from AiR", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private List<ATEndpoint> getAsEndpoints(List<Endpoint> endpoints) {
		if (endpoints != null) {
			List<ATEndpoint> asEndpoints = new ArrayList<ATEndpoint>();
			for (Endpoint endpoint : endpoints) {
				ATEndpoint asEndpoint = new ATEndpoint();
				asEndpoint.setDescription(endpoint.getDescription());
				asEndpoint.setDescriptor(endpoint.getDescriptor());
				asEndpoint.setInvocation_path(endpoint.getInvocationPath());
				asEndpoint.setPort(endpoint.getPort());
				asEndpoint.setService_name(endpoint.getServiceName());
				asEndpoint
						.setEndpoint_type(endpoint.getType() == null ? EndpointType.REST
								.toString() : endpoint.getType().toString());

				asEndpoints.add(asEndpoint);
			}
			return asEndpoints;
		}
		return null;
	}

	@Override
	public void rollback() {
		try {
			air.deleteAtomicService(createdAtomicServiceId);
		} catch (Exception e) {
			// best effort
			logger.warn("Unable to rollback", e);
		}
	}
}
