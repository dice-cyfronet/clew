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
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.InvocationPathInfo;
import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.impl.action.as.AddInitialConfigurationAction;
import pl.cyfronet.coin.impl.action.as.CreateAtomicServiceAction;
import pl.cyfronet.coin.impl.action.as.DeleteAtomicServiceAction;
import pl.cyfronet.coin.impl.action.as.DeleteAtomicServiceFromAirAction;
import pl.cyfronet.coin.impl.action.as.GetAtomicServiceAction;
import pl.cyfronet.coin.impl.action.as.GetEndpointPayloadAction;
import pl.cyfronet.coin.impl.action.as.GetInvocationPathInfo;
import pl.cyfronet.coin.impl.action.as.GetServicesSetAction;
import pl.cyfronet.coin.impl.action.as.ListAtomicServicesAction;
import pl.cyfronet.coin.impl.action.as.ListInitialConfigurationsAction;
import pl.cyfronet.coin.impl.action.endpoint.AddAsiEndpointAction;
import pl.cyfronet.coin.impl.action.endpoint.ListAsiEndpointsAction;
import pl.cyfronet.coin.impl.action.endpoint.RemoveAsiEndpointAction;
import pl.cyfronet.coin.impl.action.key.AddPublicKeyAction;
import pl.cyfronet.coin.impl.action.key.DeletePublicKeyAction;
import pl.cyfronet.coin.impl.action.key.GetPublicKeyAction;
import pl.cyfronet.coin.impl.action.key.ListUserKeysAction;
import pl.cyfronet.coin.impl.action.ownedpayload.DeleteOwnedPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.GetOwnedPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.GetOwnedPayloadPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.ListOwnedPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.NewOwnedPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.UpdateOwnedPayloadAction;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.SecurityPolicyActions;
import pl.cyfronet.coin.impl.action.ownedpayload.provider.SecurityProxyActions;
import pl.cyfronet.coin.impl.action.redirection.AddAsiRedirectionAction;
import pl.cyfronet.coin.impl.action.redirection.GetAsiRedirectionsAction;
import pl.cyfronet.coin.impl.action.redirection.RemoveAsiRedirectionAction;
import pl.cyfronet.coin.impl.action.workflow.GetUserWorkflowAction;
import pl.cyfronet.coin.impl.action.workflow.GetUserWorkflowsAction;
import pl.cyfronet.coin.impl.action.workflow.GetWorkflowDetailAction;
import pl.cyfronet.coin.impl.action.workflow.RemoveASIFromWorkflowAction;
import pl.cyfronet.coin.impl.action.workflow.RemoveAtomicServiceFromWorkflowAction;
import pl.cyfronet.coin.impl.action.workflow.StartAtomicServiceAction;
import pl.cyfronet.coin.impl.action.workflow.StartWorkflowAction;
import pl.cyfronet.coin.impl.action.workflow.StopWorkflowAction;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
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

	private String coinBaseUrl;

	public Action<List<AtomicService>> createListAtomicServicesAction(
			String username) {
		return new ListAtomicServicesAction(this, username);
	}

	public Action<Class<Void>> createDeleteAtomicServiceAction(String username,
			String asId, boolean admin) {
		return new DeleteAtomicServiceAction(this, username, asId, admin);
	}

	public Action<Class<Void>> createDeleteAtomicServiceFromAirAction(
			String atomicServiceId) {
		return new DeleteAtomicServiceFromAirAction(this, atomicServiceId);
	}

	public Action<String> createCreateAtomicServiceAction(String username,
			NewAtomicService newAtomicService) {
		return new CreateAtomicServiceAction(this, username, defaultSiteId,
				newAtomicService);
	}

	public Action<List<InitialConfiguration>> createListInitialConfigurationsAction(
			String atomicServiceId, boolean loadPayload) {
		return new ListInitialConfigurationsAction(this, atomicServiceId,
				loadPayload);
	}

	public Action<String> createAddInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration) {
		return new AddInitialConfigurationAction(this, atomicServiceId,
				initialConfiguration);
	}

	public Action<String> createGetEndpointPayloadAction(
			String atomicServiceId, String serviceName, String invocationPath) {
		return new GetEndpointPayloadAction(this, atomicServiceId, serviceName,
				invocationPath);
	}

	public Action<AtomicService> createGetAtomicServiceAction(
			String atomicServiceId) {
		return new GetAtomicServiceAction(this, atomicServiceId);
	}

	public Action<List<WorkflowBaseInfo>> createGetUserWorkflowsAction(
			String username) {
		return new GetUserWorkflowsAction(this, username);
	}

	public Action<WorkflowDetail> createGetWorkflowDetailAction(
			String contextId, String username) {
		return new GetWorkflowDetailAction(this, contextId, username);
	}

	public Action<Class<Void>> createStopWorkflowAction(String contextId,
			String username) {
		return new StopWorkflowAction(this, contextId, username);
	}

	public Action<String> createStartWorkflowAction(
			WorkflowStartRequest startRequest, String username) {
		return new StartWorkflowAction(this, defaultPriority, username,
				startRequest);
	}

	public Action<Workflow> createGetUserWorkflowAction(String workflowId,
			String username) {
		return new GetUserWorkflowAction(this, workflowId, username);
	}

	public Action<String> createStartAtomicServiceAction(String username,
			String atomicServiceId, String asName, String contextId,
			String keyName) {
		return new StartAtomicServiceAction(this, username, atomicServiceId,
				asName, contextId, defaultPriority, keyName);
	}

	public Action<String> createStartAtomicServiceAction(
			String username, List<String> ids, List<String> names,
			String contextId, Integer priority, String keyId) {
		return new StartAtomicServiceAction(this, username, ids, names,
				contextId, priority, keyId);
	}

	// policy files

	public Action<List<String>> createListSecurityPoliciesAction() {
		return new ListOwnedPayloadAction(this, new SecurityPolicyActions(air));
	}

	public Action<NamedOwnedPayload> createGetSecurityPolicyAction(String name) {
		return new GetOwnedPayloadAction(this, name, new SecurityPolicyActions(
				air));
	}

	public Action<String> createGetSecurityPolicyPayloadAction(String name) {
		return new GetOwnedPayloadPayloadAction(this, name,
				new SecurityPolicyActions(air));
	}

	public Action<Class<Void>> createNewSecurityPolicyAction(String username,
			NamedOwnedPayload securityPolicy) {
		return new NewOwnedPayloadAction(this, username, securityPolicy,
				new SecurityPolicyActions(air));
	}

	public Action<Class<Void>> createUpdateSecurityPolicyAction(
			String username, String name, OwnedPayload ownedPayload) {
		return new UpdateOwnedPayloadAction(this, username, name, ownedPayload,
				new SecurityPolicyActions(air));
	}

	public Action<Class<Void>> createDeleteSecurityPolicyAction(
			String username, String name) {
		return new DeleteOwnedPayloadAction(this, username, name,
				new SecurityPolicyActions(air));
	}

	// security proxy configurations

	public Action<List<String>> createListSecurityProxiesAction() {
		return new ListOwnedPayloadAction(this, new SecurityProxyActions(air));
	}

	public Action<NamedOwnedPayload> createGetSecurityProxyAction(String name) {
		return new GetOwnedPayloadAction(this, name, new SecurityProxyActions(
				air));
	}

	public Action<String> createGetSecurityProxyPayloadAction(String name) {
		return new GetOwnedPayloadPayloadAction(this, name,
				new SecurityProxyActions(air));
	}

	public Action<Class<Void>> createNewSecurityProxyAction(String username,
			NamedOwnedPayload ownedPayload) {
		return new NewOwnedPayloadAction(this, username, ownedPayload,
				new SecurityProxyActions(air));
	}

	public Action<Class<Void>> createUpdateSecurityProxyAction(String username,
			String name, OwnedPayload ownedPayload) {
		return new UpdateOwnedPayloadAction(this, username, name, ownedPayload,
				new SecurityProxyActions(air));
	}

	public Action<Class<Void>> createDeleteSecurityProxyAction(String username,
			String name) {
		return new DeleteOwnedPayloadAction(this, username, name,
				new SecurityProxyActions(air));
	}

	// keys

	public Action<List<PublicKeyInfo>> createListUserKeysAction(String username) {
		return new ListUserKeysAction(this, username);
	}

	public Action<String> createGetPublicKeyAction(String vphUsername,
			String keyId) {
		return new GetPublicKeyAction(this, vphUsername, keyId);
	}

	public Action<Class<Void>> createDeletePublicKeyAction(String username,
			String keyId) {
		return new DeletePublicKeyAction(this, username, keyId);
	}

	public Action<String> createAddPublicKeyAction(String username,
			String keyName, String publicKeyContent) {
		return new AddPublicKeyAction(this, username, keyName, publicKeyContent);
	}

	public Action<Class<Void>> createRemoveAtomicServiceFromWorkflowAction(
			String username, String contextId, String asConfId) {
		return new RemoveAtomicServiceFromWorkflowAction(this, username,
				contextId, asConfId);
	}

	public Action<Class<Void>> createRemoveASIFromWorkflowAction(
			String username, String contextId, String asiId) {
		return new RemoveASIFromWorkflowAction(this, username, contextId, asiId);
	}

	// redirections

	public Action<List<Redirection>> createGetAsiRedirectionsAction(
			String contextId, String username, String asiId) {
		return new GetAsiRedirectionsAction(this, contextId, username, asiId,
				proxyHost, proxyPort);
	}

	public Action<String> createAddAsiRedirectionAction(String username,
			String contextId, String asiId, String serviceName, int port,
			RedirectionType type) {
		AddAsiRedirectionAction action = new AddAsiRedirectionAction(this,
				httpRedirectionService, dnatRedirectionService, username,
				contextId, asiId);
		action.setRedirectionDetails(serviceName, port,
				type == RedirectionType.HTTP);
		return action;
	}

	public Action<Class<Void>> createRemoveAsiRedirectionAction(
			String username, String contextId, String asiId,
			String redirectionId) {
		return new RemoveAsiRedirectionAction(this, httpRedirectionService,
				dnatRedirectionService, username, contextId, asiId,
				redirectionId);
	}

	// endpoints
	public Action<List<Endpoint>> createListAsiEndpointsAction(String username,
			String contextId, String asiId) {
		return new ListAsiEndpointsAction(this, username, contextId, asiId);
	}

	public Action<String> createAddAsiEndpointAction(String username,
			String contextId, String asiId, Endpoint endpoint) {
		return new AddAsiEndpointAction(this, username, contextId, asiId,
				endpoint);
	}

	public Action<Class<Void>> createRemoveAsiEndpointAction(String username,
			String contextId, String asiId, String endpointId) {
		return new RemoveAsiEndpointAction(this, username, contextId, asiId,
				endpointId);
	}

	// taverna

	public Action<String> createGetServicesSetAction() {
		return new GetServicesSetAction(this, coinBaseUrl);
	}

	public Action<InvocationPathInfo> createGetInvocationPathInfo(
			String atomicServiceId, String serviceName) {
		return new GetInvocationPathInfo(this, atomicServiceId, serviceName);
	}

	// setters

	public void setAir(AirClient air) {
		this.air = air;
	}

	public void setAtmosphere(DyReAllaManagerService atmosphere) {
		this.atmosphere = atmosphere;
	}

	public AirClient getAir() {
		return air;
	}

	public DyReAllaManagerService getAtmosphere() {
		return atmosphere;
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

	public void setCoinBaseUrl(String coinBaseUrl) {
		this.coinBaseUrl = coinBaseUrl;
	}
}
