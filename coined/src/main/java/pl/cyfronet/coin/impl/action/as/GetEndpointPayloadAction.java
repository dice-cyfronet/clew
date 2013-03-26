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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetEndpointPayloadAction extends ReadOnlyAirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(GetEndpointPayloadAction.class);

	private String atomicServiceId;
	private String serviceName;
	private String invocationPath;

	public GetEndpointPayloadAction(ActionFactory actionFactory,
			String atomicServiceId, String serviceName, String invocationPath) {
		super(actionFactory);
		this.atomicServiceId = atomicServiceId;
		this.serviceName = serviceName;
		this.invocationPath = invocationPath;
	}

	@Override
	public String execute() throws CloudFacadeException {
		ATEndpoint endpoint = getEndpoint(atomicServiceId, serviceName,
				invocationPath);
		String descriptor = endpoint.getDescriptor();
		return descriptor != null ? descriptor.trim() : descriptor;
	}

	private ATEndpoint getEndpoint(String atomicServiceId, String serviceName,
			String invocationPath) throws AtomicServiceNotFoundException,
			EndpointNotFoundException {

		ApplianceType type = getApplianceType(atomicServiceId, true);

		Map<String, Integer> name2port = new HashMap<>();
		for (ATPortMapping portMapping : type.getPort_mappings()) {
			name2port.put(portMapping.getService_name(), portMapping.getPort());
		}

		for (ATEndpoint endpoint : type.getEndpoints()) {
			int endpointPort = endpoint.getPort();
			String endpointInvocationPath = endpoint.getInvocation_path();
			logger.debug("Comparing {}=={} and {}=={}", new Object[] {
					endpointPort, serviceName, endpointInvocationPath,
					invocationPath });
			if (name2port.containsKey(serviceName)
					&& endpointPort == name2port.get(serviceName)
					&& endpointPathEquals(endpointInvocationPath,
							invocationPath)) {
				return endpoint;
			}
		}

		logger.debug("Endpoint {}:{}/{} not found", new Object[] {
				atomicServiceId, serviceName, invocationPath });
		throw new EndpointNotFoundException();
	}

	private boolean endpointPathEquals(String endpointPath,
			String invocationPath) {
		String endpointPathWithouldSlashAtTheBeginning = removeSlashesFromTheBeginning(endpointPath);
		String invocationPathWithouldSlashAtTheBeginning = removeSlashesFromTheBeginning(invocationPath);
		return endpointPathWithouldSlashAtTheBeginning
				.equalsIgnoreCase(invocationPathWithouldSlashAtTheBeginning);
	}

	private String removeSlashesFromTheBeginning(String str) {
		return str.replaceAll("^/+", "");
	}
}
