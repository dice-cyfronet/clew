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

import java.util.List;
import java.util.Properties;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ActionFactory {

	private AirClient air;
	private DyReAllaManagerService atmosphere;
	private String defaultSiteId;
	private Integer defaultPriority;
	private Properties credentialProperties;

	public ListAtomicServicesAction createListAtomicServicesAction() {
		return new ListAtomicServicesAction(air);
	}

	public CreateAtomicServiceAction createCreateAtomicServiceAction(
			String asInstanceId, AtomicService atomicService) {
		return new CreateAtomicServiceAction(air, atmosphere, defaultSiteId,
				asInstanceId, atomicService);
	}

	public AirAction<List<InitialConfiguration>> createGetInitialConfigurationsAction(
			String atomicServiceId) {
		return new GetInitialConfigurationsAction(air, atomicServiceId);
	}

	public AddInitialConfigurationAction createAddInitialConfiguration(
			String atomicServiceId, InitialConfiguration initialConfiguration) {
		return new AddInitialConfigurationAction(air, atomicServiceId,
				initialConfiguration);
	}

	public GetEndpointPayloadAction createGetEndpointPayloadAction(
			String atomicServiceId, int servicePort, String invocationPath) {
		return new GetEndpointPayloadAction(air, atomicServiceId, servicePort,
				invocationPath);
	}

	public GetAtomicServiceAction createGetAtomicServiceAction(
			String atomicServiceId) {
		return new GetAtomicServiceAction(air, atomicServiceId);
	}

	public GetUserWorkflowsAction createGetUserWorkflowsAction(String username) {
		return new GetUserWorkflowsAction(air, username);
	}

	public StopWorkflowAction createStopWorkflowAction(String contextId,
			String username) {
		return new StopWorkflowAction(air, atmosphere, contextId, username);
	}

	public StartWorkflowAction createStartWorkflowAction(
			WorkflowStartRequest startRequest, String username) {
		return new StartWorkflowAction(air, atmosphere, defaultPriority,
				username, startRequest);
	}

	public GetUserWorkflowAction createGetUserWorkflowAction(String workflowId,
			String username) {
		return new GetUserWorkflowAction(air, credentialProperties, workflowId,
				username);		
	}

	public void setAir(AirClient air) {
		this.air = air;
	}

	public void setAtmosphere(DyReAllaManagerService atmosphere) {
		this.atmosphere = atmosphere;
	}

	public void setDefaultSiteId(String defaultSiteId) {
		this.defaultSiteId = defaultSiteId;
	}

	public void setCredentialProperties(Properties credentialProperties) {
		this.credentialProperties = credentialProperties;
	}
}