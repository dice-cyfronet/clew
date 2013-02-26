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

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.impl.action.grant.DeleteGrantAction;
import pl.cyfronet.coin.impl.action.grant.GetGrantAction;
import pl.cyfronet.coin.impl.action.grant.ListGrantsAction;
import pl.cyfronet.coin.impl.action.grant.UpdateGrantAction;
import pl.cyfronet.coin.impl.action.redirection.AddAsiRedirectionAction;
import pl.cyfronet.coin.impl.action.redirection.GetAsiRedirectionsAction;
import pl.cyfronet.coin.impl.action.redirection.RemoveAsiRedirectionAction;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;
import pl.cyfronet.dyrealla.api.dnat.DyReAllaDNATManagerService;
import pl.cyfronet.dyrealla.api.proxy.DyReAllaProxyManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ActionFactory {

	private AirClient air;
	private DyReAllaManagerService atmosphere;
	private String defaultSiteId;
	private Integer defaultPriority;
	
	private String proxyHost;	
	private int proxyPort;	
	
	private DyReAllaProxyManagerService httpRedirectionService;

	private DyReAllaDNATManagerService dnatRedirectionService;

	public Action<List<AtomicService>> createListAtomicServicesAction() {
		return new ListAtomicServicesAction(air);
	}

	public DeleteAtomicServiceAction createDeleteAtomicServiceAction(
			String atomicServiceName) {
		return new DeleteAtomicServiceAction(air, atomicServiceName);
	}

	public Action<String> createCreateAtomicServiceAction(String username,
			NewAtomicService newAtomicService) {
		return new CreateAtomicServiceAction(air, atmosphere, username,
				defaultSiteId, newAtomicService);
	}

	public Action<List<InitialConfiguration>> createListInitialConfigurationsAction(
			String atomicServiceId, boolean loadPayload) {
		return new ListInitialConfigurationsAction(air, atomicServiceId,
				loadPayload);
	}

	public Action<String> createAddInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration) {
		return new AddInitialConfigurationAction(air, atomicServiceId,
				initialConfiguration);
	}

	public Action<String> createGetEndpointPayloadAction(
			String atomicServiceId, int servicePort, String invocationPath) {
		return new GetEndpointPayloadAction(air, atomicServiceId, servicePort,
				invocationPath);
	}

	public Action<AtomicService> createGetAtomicServiceAction(
			String atomicServiceId) {
		return new GetAtomicServiceAction(air, atomicServiceId);
	}

	public Action<List<WorkflowBaseInfo>> createGetUserWorkflowsAction(
			String username) {
		return new GetUserWorkflowsAction(air, username);
	}

	public Action<Class<Void>> createStopWorkflowAction(String contextId,
			String username) {
		return new StopWorkflowAction(air, atmosphere, contextId, username);
	}

	public Action<String> createStartWorkflowAction(
			WorkflowStartRequest startRequest, String username) {
		return new StartWorkflowAction(air, atmosphere, defaultPriority,
				username, startRequest);
	}

	public Action<Workflow> createGetUserWorkflowAction(String workflowId,
			String username) {
		return new GetUserWorkflowAction(air, workflowId, username);
	}

	public Action<String> createStartAtomicServiceAction(
			String atomicServiceId, String asName, String contextId,
			String username, String keyName) {
		return new StartAtomicServiceAction(air, atmosphere, username,
				atomicServiceId, asName, contextId, defaultPriority, keyName);
	}

	// policy files

	public Action<String> createGetSecurityPolicyAction(String policyName) {
		return new GetSecurityPolicyAction(air, policyName);
	}

	public Action<List<String>> createListSecurityPoliciesAction() {
		return new ListSecurityPoliciesAction(air);
	}

	public Action<Class<Void>> createUploadSecurityPolicyAction(
			String policyName, String policyText, boolean overwrite) {
		return new UploadSecurityPolicyAction(air, policyName, policyText,
				overwrite);
	}

	public Action<Class<Void>> createDeleteSecurityPolicyAction(
			String policyName) {
		return new DeleteSecurityPolicyAction(air, policyName);
	}

	// keys

	public Action<List<PublicKeyInfo>> createListUserKeysAction(String username) {
		return new ListUserKeysAction(air, username);
	}

	public Action<String> createGetPublicKeyAction(String vphUsername,
			String keyId) {
		return new GetPublicKeyAction(air, vphUsername, keyId);
	}

	public Action<Class<Void>> createDeletePublicKeyAction(String username,
			String keyId) {
		return new DeletePublicKeyAction(air, atmosphere, username, keyId);
	}

	public Action<String> createAddPublicKeyAction(String username,
			String keyName, String publicKeyContent) {
		return new AddPublicKeyAction(air, atmosphere, username, keyName,
				publicKeyContent);
	}

	public Action<Class<Void>> createRemoveAtomicServiceFromWorkflowAction(
			String username, String contextId, String asConfId) {
		return new RemoveAtomicServiceFromWorkflowAction(air, atmosphere,
				username, contextId, asConfId);
	}

	public Action<Class<Void>> createRemoveASIFromWorkflowAction(
			String username, String contextId, String asiId) {
		return new RemoveASIFromWorkflowAction(air, atmosphere, username,
				contextId, asiId);
	}

	// redirections

	public Action<List<Redirection>> createGetAsiRedirectionsAction(
			String contextId, String username, String asiId) {
		return new GetAsiRedirectionsAction(contextId, username, asiId, proxyHost, proxyPort, air);
	}

	public Action<String> createAddAsiRedirectionAction(String username,
			String contextId, String asiId, String serviceName, int port,
			RedirectionType type) {
		AddAsiRedirectionAction action = new AddAsiRedirectionAction(air,
				atmosphere, httpRedirectionService, dnatRedirectionService,
				username, contextId, asiId);
		action.setRedirectionDetails(serviceName, port,
				type == RedirectionType.HTTP);
		return action;
	}

	public Action<Class<Void>> createRemoveAsiRedirectionAction(
			String username, String contextId, String asiId,
			String redirectionId) {
		return new RemoveAsiRedirectionAction(air, atmosphere,
				httpRedirectionService, dnatRedirectionService, username,
				contextId, asiId, redirectionId);
	}

	// grants

	public Action<List<String>> createListGrantsAction() {
		return new ListGrantsAction(air);
	}

	public Action<Grant> createGetGrantAction(String name) {
		return new GetGrantAction(air, name);
	}

	public Action<Class<Void>> createUpdateGrantAction(String name,
			Grant grant, boolean overwrite) {
		return new UpdateGrantAction(air, name, grant, overwrite);
	}

	public Action<Class<Void>> createDeleteGrantAction(String name) {
		return new DeleteGrantAction(air, name);
	}

	// setters

	public void setAir(AirClient air) {
		this.air = air;
	}

	public void setAtmosphere(DyReAllaManagerService atmosphere) {
		this.atmosphere = atmosphere;
	}

	public void setDefaultSiteId(String defaultSiteId) {
		this.defaultSiteId = defaultSiteId;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public void setHttpRedirectionService(
			DyReAllaProxyManagerService httpRedirectionService) {
		this.httpRedirectionService = httpRedirectionService;
	}

	public void setDnatRedirectionService(
			DyReAllaDNATManagerService dnatRedirectionService) {
		this.dnatRedirectionService = dnatRedirectionService;
	}
	
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
}
