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
package pl.cyfronet.coin.impl;

import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.InvocationPathInfo;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.auth.annotation.Role;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * Web service which exposes functionality given by the cloud manager.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudFacadeImpl extends UsernameAwareService implements
		CloudFacade {

	private static final String ADMIN_ROLE = "admin";

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudFacadeImpl.class);

	private ActionFactory actionFactory;

	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		logger.debug("Get atomic services");
		Action<List<AtomicService>> action = actionFactory
				.createListAtomicServicesAction(getUsername());

		return action.execute();
	}

	@Role(values = "developer")
	@Override
	public String createAtomicService(NewAtomicService newAtomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		logger.debug("Create atomic service from {}",
				newAtomicService.getSourceAsiId());
		try {
			Action<String> action = actionFactory
					.createCreateAtomicServiceAction(getUsername(),
							newAtomicService);
			return action.execute();
		} catch (WorkflowNotFoundException e) {
			throw new WebApplicationException(404);
		}
	}

	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId, boolean loadPayload)
			throws AtomicServiceNotFoundException {
		logger.debug("Get initial configurations for: {}", atomicServiceId);
		Action<List<InitialConfiguration>> action = actionFactory
				.createListInitialConfigurationsAction(atomicServiceId,
						loadPayload);
		return action.execute();
	}

	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			AtomicServiceNotFoundException, CloudFacadeException,
			InitialConfigurationAlreadyExistException {

		logger.debug("Creating new atomic service from {}, metadata: {}",
				atomicServiceId, initialConfiguration);

		Action<String> action = actionFactory.createAddInitialConfiguration(
				atomicServiceId, initialConfiguration);

		return action.execute();
	}

	@Override
	public String getServicesSet() {
		Action<String> action = actionFactory.createGetServicesSetAction();
		return action.execute();
	}

	@Override
	public String getEndpointDescriptor(String atomicServiceId,
			String serviceName, String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		logger.debug("Getting endpoint descriptor for {}:{}/{}", new Object[] {
				atomicServiceId, serviceName, invocationPath });

		Action<String> action = actionFactory.createGetEndpointPayloadAction(
				atomicServiceId, serviceName, invocationPath);

		String descriptor = action.execute();

		logger.debug("Descriptor value: {}", descriptor);

		return descriptor;
	}

	@Override
	public InvocationPathInfo getInvocationPathInfo(String atomicServiceId,
			String serviceName, String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException {
		// check if atomic service with given id is registered in Atmosphere.
		Action<InvocationPathInfo> action = actionFactory
				.createGetInvocationPathInfo(atomicServiceId, serviceName);
		return action.execute();
	}

	public void setActionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

	@Override
	public void deleteAtomicService(String atomicServiceId)
			throws AtomicServiceNotFoundException, NotAcceptableException,
			NotAllowedException {
		Action<Class<Void>> action = actionFactory
				.createDeleteAtomicServiceAction(getUsername(),
						atomicServiceId, hasRole(ADMIN_ROLE));
		action.execute();
	}
}
