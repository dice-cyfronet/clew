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
package pl.cyfronet.coin.impl.action.as;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceAlreadyExistsException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.BaseAction;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AddAtomicServiceRequest;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceInAirAction extends BaseAction<String> {

	private static Logger logger = LoggerFactory
			.getLogger(CreateAtomicServiceInAirAction.class);

	private ApplianceType applianceType;
	private String createdAtomicServiceId;

	/**
	 * #1021
	 * @since 1.1.0
	 */
	private String username;

	private String parentId;

	public CreateAtomicServiceInAirAction(ActionFactory actionFactory,
			String username, ApplianceType atomicService) {
		this(actionFactory, username, atomicService, null);
	}

	public CreateAtomicServiceInAirAction(ActionFactory actionFactory,
			String username, ApplianceType applianceType, String parentId) {
		super(actionFactory);
		this.username = username;
		this.applianceType = applianceType;
		this.parentId = parentId;
	}

	@Override
	public String execute() throws CloudFacadeException {
		AddAtomicServiceRequest addASRequest = new AddAtomicServiceRequest();
		addASRequest.setClient("rest");
		addASRequest.setDescription(applianceType.getDescription());
		addASRequest.setEndpoints(getEndpoints(applianceType.getEndpoints()));
		addASRequest.setPort_mappings(getPortMapping(applianceType
				.getPort_mappings()));
		addASRequest.setProxy_conf_name(applianceType.getProxy_conf_name());
		addASRequest.setName(applianceType.getName());
		addASRequest.setPublished(applianceType.isPublished());
		addASRequest.setScalable(applianceType.isScalable());
		addASRequest.setShared(applianceType.isShared());
		addASRequest.setDevelopment(applianceType.isDevelopment());

		addASRequest.setAuthor(username);
		addASRequest.setOriginal_appliance(parentId);

		try {
			logger.debug("Creating new appliance type in AIR {}", addASRequest);
			createdAtomicServiceId = getAir().addAtomicService(addASRequest);
			logger.debug("New appliance type created {}",
					createdAtomicServiceId);
			return createdAtomicServiceId;
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 302) {
				throw new AtomicServiceAlreadyExistsException();
			}
			logger.warn("Error received from AiR", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private List<ATEndpoint> getEndpoints(List<ATEndpoint> endpoints) {
		if (endpoints != null) {
			List<ATEndpoint> asEndpoints = new ArrayList<ATEndpoint>();
			for (ATEndpoint endpoint : endpoints) {
				ATEndpoint asEndpoint = new ATEndpoint();
				asEndpoint.setDescription(endpoint.getDescription());
				asEndpoint.setDescriptor(endpoint.getDescriptor());
				asEndpoint.setInvocation_path(endpoint.getInvocation_path());
				asEndpoint.setPort(endpoint.getPort());
				asEndpoint.setEndpoint_type(endpoint.getEndpoint_type());

				asEndpoints.add(asEndpoint);
			}
			return asEndpoints;
		}
		return null;
	}

	private List<ATPortMapping> getPortMapping(List<ATPortMapping> portMappings) {
		if (portMappings != null) {
			List<ATPortMapping> asPortMapping = new ArrayList<>();
			for (ATPortMapping atPortMapping : portMappings) {
				ATPortMapping portMapping = new ATPortMapping();
				portMapping.setHttp(atPortMapping.isHttp());
				portMapping.setPort(atPortMapping.getPort());
				portMapping.setService_name(atPortMapping.getService_name());
				asPortMapping.add(portMapping);
			}
			return asPortMapping;
		}

		return null;
	}

	@Override
	public void rollback() {
		try {
			getAir().deleteAtomicService(createdAtomicServiceId, true);
		} catch (Exception e) {
			// best effort
			logger.warn("Unable to rollback", e);
		}
	}
}
