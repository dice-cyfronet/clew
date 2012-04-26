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

package pl.cyfronet.coin.impl.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Credential;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AddAtomicServiceRequest;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Specs;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.ApplianceNotFoundException;
import pl.cyfronet.dyrealla.DyReAllaException;
import pl.cyfronet.dyrealla.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.allocation.impl.ApplianceIdentityImpl;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * Set of methods allowing to manage Atmosphere and AIR.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudManagerImpl implements CloudManager {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudManagerImpl.class);

	/**
	 * Atmosphere client.
	 */
	private DyReAllaManagerService atmosphere;

	/**
	 * Air client
	 */
	private AirClient air;

	/**
	 * Default workflow priority.
	 */
	private Integer defaultPriority;

	private String defaultSiteId;

	private Properties credentialProperties;

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() {
		List<ApplianceType> applianceTypes = getApplianceTypes();
		List<AtomicService> atomicServices = new ArrayList<AtomicService>();
		for (ApplianceType applianceType : applianceTypes) {
			AtomicService atomicService = getAtomicService(applianceType);
			atomicServices.add(atomicService);
		}
		return atomicServices;
	}

	@Override
	public AtomicService getAtomicService(String atomicServiceId)
			throws AtomicServiceNotFoundException {
		ApplianceType applianceType = getApplianceType(atomicServiceId);
		return getAtomicService(applianceType);
	}

	private AtomicService getAtomicService(ApplianceType applianceType) {
		AtomicService atomicService = new AtomicService();
		atomicService.setAtomicServiceId(applianceType.getName());
		atomicService.setDescription(applianceType.getDescription());
		atomicService.setHttp(applianceType.isHttp()
				&& applianceType.isIn_proxy());
		atomicService.setName(applianceType.getName());
		atomicService.setShared(applianceType.isShared());
		atomicService.setScalable(applianceType.isScalable());
		atomicService.setVnc(applianceType.isVnc());
		atomicService.setPublished(applianceType.isPublished());
		atomicService.setActive(applianceType.getTemplates_count() > 0);
		atomicService.setEndpoints(getEndpoints(applianceType));

		return atomicService;
	}

	/**
	 * @param endpoints
	 * @return
	 */
	private List<Endpoint> getEndpoints(ApplianceType applianceType) {
		List<ATEndpoint> atEndpoints = applianceType.getEndpoints();
		List<Endpoint> asEndpoints = new ArrayList<Endpoint>();
		if (atEndpoints != null) {
			for (ATEndpoint atEndpoint : atEndpoints) {
				asEndpoints.add(getEndpoint(atEndpoint));
			}
		}

		return asEndpoints;
	}

	/**
	 * @param atEndpoint
	 * @return
	 */
	private Endpoint getEndpoint(ATEndpoint atEndpoint) {
		Endpoint asEndpoint = new Endpoint();
		asEndpoint.setDescription(atEndpoint.getDescription());
		asEndpoint.setDescriptor(atEndpoint.getDescriptor());
		asEndpoint.setInvocationPath(atEndpoint.getInvocation_path());
		asEndpoint.setPort(atEndpoint.getPort());
		asEndpoint.setServiceName(atEndpoint.getService_name());
		asEndpoint.setType(getEdnpointType(atEndpoint.getEndpoint_type()));

		return asEndpoint;
	}

	/**
	 * @param endpoint_type
	 * @return
	 */
	private EndpointType getEdnpointType(String endpoint_type) {
		return "WS".equalsIgnoreCase(endpoint_type) ? EndpointType.WS
				: EndpointType.REST;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#startAtomicService(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
	public String startAtomicService(String atomicServiceId, String name,
			String contextId, String username)
			throws AtomicServiceNotFoundException, CloudFacadeException,
			WorkflowNotFoundException {
		checkIfWorkflowBelongsToUser(contextId, username);
		logger.debug("Add atomic service [{} {}] into workflow [{}]",
				new Object[] { name, atomicServiceId, contextId });
		registerVms(contextId, Arrays.asList(atomicServiceId),
				Arrays.asList(name), defaultPriority);

		// TODO information from Atmosphere about atomis service instance id
		// needed!
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#createAtomicService(java.lang
	 * .String, pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public String createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService, String username)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
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

		String atomicServiceId = air.addAtomicService(addASRequest);
		try {
			atmosphere.createTemplate(atomicServiceInstanceId,
					atomicService.getName(), defaultSiteId, atomicServiceId);
			return atomicService.getName();
		} catch (ApplianceNotFoundException e) {
			// TODO remove added atomic service type
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			// TODO remove added atomic service type
			throw new CloudFacadeException(e.getMessage());
		}
	}

	/**
	 * @param endpoints
	 * @return
	 */
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
				asEndpoints.add(asEndpoint);
				asEndpoint
						.setEndpoint_type(endpoint.getType() == null ? EndpointType.REST
								.toString() : endpoint.getType().toString());
			}
			return asEndpoints;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#startWorkflow(pl.cyfronet.
	 * coin.api.beans.Workflow, java.lang.String)
	 */
	@Override
	public String startWorkflow(WorkflowStartRequest workflow, String username)
			throws WorkflowStartException {
		logger.debug("starting workflow {} for {} user", workflow, username);

		Integer priority = workflow.getPriority();
		if (priority == null) {
			priority = defaultPriority;
		}

		WorkflowType type = workflow.getType();
		if (type == WorkflowType.portal || type == WorkflowType.development) {
			try {
				List<WorkflowBaseInfo> workflows = getWorkflows(username);
				for (WorkflowBaseInfo wInfo : workflows) {
					if (wInfo.getType() == type) {
						throw new WorkflowStartException(String.format(
								"Cannot start two %s workflows", type));
					}
				}
			} catch (WebApplicationException e) {
				// 400 is thrown if user is not know by AIR. Most probably user
				// is starting workflow for the first time.
				if (e.getResponse().getStatus() != 400) {
					throw new WorkflowStartException(
							"Unable to register new workflow in AIR");
				}
			}
		}

		// FIXME error handling
		String workflowId = air.startWorkflow(workflow.getName(), username,
				workflow.getDescription(), priority, workflow.getType());
		List<String> ids = workflow.getAsConfigIds();

		try {
			registerVms(workflowId, ids, null, priority);
		} catch (CloudFacadeException e) {
			try {
				stopWorkflow(workflowId, username);
			} catch(Exception e2) {
				
			}
			throw new WorkflowStartException();
		}
			
		return workflowId;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#stopWorkflow(java.lang.String)
	 */
	@Override
	public void stopWorkflow(String contextId, String username)
			throws WorkflowNotFoundException, CloudFacadeException {
		logger.debug("stopping workflow {}", contextId);
		checkIfWorkflowBelongsToUser(contextId, username);
		ManagerResponse response = atmosphere
				.removeRequiredAppliances(contextId);
		parseResponseAndThrowExceptionsWhenNeeded(response);
		air.stopWorkflow(contextId);
	}

	/**
	 * 
	 */
	private void parseResponseAndThrowExceptionsWhenNeeded(
			ManagerResponse response) throws CloudFacadeException {
		if (response.getOperationStatus() == OperationStatus.FAILED) {
			String errorMessage = getErrorMessage(response);
			throw new CloudFacadeException(errorMessage);
		}
	}

	/**
	 * @param response
	 * @return
	 */
	private String getErrorMessage(ManagerResponse response) {
		if(response.getErrors() != null) {
			return response.getErrors().toString();
		}
		
		return null;
	}

	@Override
	public Workflow getWorkflow(String contextId, String username)
			throws WorkflowNotFoundException {
		WorkflowDetail detail = getUserWorkflow(contextId, username);

		Workflow workflow = new Workflow();
		workflow.setName(detail.getName());
		workflow.setType(detail.getWorkflow_type());

		List<Vms> vms = detail.getVms();
		if (vms != null) {
			List<AtomicServiceInstance> instances = new ArrayList<AtomicServiceInstance>();
			for (Vms vm : vms) {
				if (vm.getVms_id() == null) {
					// /AIR returns vms:[null] when workflow does not have VMs
					// and it is parsed by CXF as Vms object with all properties
					// set to null.
					continue;
				}

				AtomicServiceInstance instance = new AtomicServiceInstance();
				instance.setAtomicServiceId(vm.getAppliance_type());
				instance.setId(vm.getVms_id());
				instance.setStatus(vm.getState());
				instance.setName(vm.getName());
				instance.setMessage(""); // TODO

				// FIXME temporary
				addRedirections(instance, vm);

				if (detail.getWorkflow_type() == WorkflowType.development) {
					instance.setCredential(getCredential(vm.getAppliance_type()));
				}

				instances.add(instance);

			}
			workflow.setAtomicServiceInstances(instances);
		}

		return workflow;
	}

	/**
	 * @param instance
	 * @param vm
	 */
	private void addRedirections(AtomicServiceInstance instance, Vms vm) {

		Specs specs = vm.getSpecs();
		if (specs != null) {
			List<String> ips = specs.getIp();
			if (ips != null && ips.size() > 0) {
				String ip = ips.get(0);
				Redirection ssh = new Redirection();
				ssh.setHost(ip);
				ssh.setFromPort(22);
				ssh.setToPort(22);
				ssh.setHttp(false);
				ssh.setName("ssh");
				instance.setRedirections(Arrays.asList(ssh));
			}
		}

	}

	/**
	 * @param vms_id
	 * @return
	 */
	private Credential getCredential(String vms_id) {
		Credential cred = new Credential();
		if (credentialProperties != null) {
			cred.setUsername(credentialProperties.getProperty(vms_id
					+ ".username"));
			cred.setPassword(credentialProperties.getProperty(vms_id
					+ ".password"));
		}
		return cred;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getInitialConfiguration(java
	 * .lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) throws AtomicServiceNotFoundException {

		ApplianceType type = getApplianceType(atomicServiceId);
		List<ApplianceConfiguration> typeConfigurations = type
				.getConfigurations();
		List<InitialConfiguration> configurations = new ArrayList<InitialConfiguration>();
		if (typeConfigurations != null) {
			for (ApplianceConfiguration applianceConfiguration : typeConfigurations) {
				InitialConfiguration configuration = new InitialConfiguration();
				configuration.setId(applianceConfiguration.getId());
				configuration.setName(applianceConfiguration.getConfig_name());
				configurations.add(configuration);
			}
		}

		return configurations;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getWorkflows(java.lang.String)
	 */
	@Override
	public List<WorkflowBaseInfo> getWorkflows(String username) {
		List<WorkflowDetail> workflowDetails = air.getUserWorkflows(username);
		List<WorkflowBaseInfo> workflows = new ArrayList<WorkflowBaseInfo>();
		for (WorkflowDetail workflowDetail : workflowDetails) {
			if (workflowDetail.getState() == Status.running) {
				WorkflowBaseInfo info = new WorkflowBaseInfo();
				info.setId(workflowDetail.getId());
				info.setName(workflowDetail.getName());
				info.setType(workflowDetail.getWorkflow_type());
				workflows.add(info);
			}
		}

		return workflows;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#addInitialConfiguration(java
	 * .lang.String, pl.cyfronet.coin.api.beans.InitialConfiguration)
	 */
	@Override
	public String addInitialConfiguration(String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			InitialConfigurationAlreadyExistException, CloudFacadeException {
		try {
			String addedConfigurationId = air.addInitialConfiguration(
					initialConfiguration.getName(), atomicServiceId,
					initialConfiguration.getPayload());

			return addedConfigurationId;
		} catch (ServerWebApplicationException e) {
			if (e.getMessage() != null) {
				if (e.getMessage().contains("not found in AIR")) {
					throw new AtomicServiceNotFoundException();
				} else if (e.getMessage().contains("duplicated configuration")) {
					throw new InitialConfigurationAlreadyExistException();
				}
			}
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public String getEndpointPayload(String atomicServiceId, int servicePort,
			String invocationPath) throws AtomicServiceNotFoundException,
			EndpointNotFoundException {
		ATEndpoint endpoint = getEndpoint(atomicServiceId, servicePort,
				invocationPath);
		String endpointId = endpoint.getId();
		return air.getEndpointDescriptor(endpointId);
	}

	@Override
	public List<Endpoint> getEndpoints() {
		List<ApplianceType> types = getApplianceTypes();
		List<Endpoint> allEndpoints = new ArrayList<Endpoint>();
		for (ApplianceType type : types) {
			List<Endpoint> typeEndpoints = getEndpoints(type);
			if (typeEndpoints != null) {
				allEndpoints.addAll(typeEndpoints);
			}
		}
		return allEndpoints;
	}

	private ATEndpoint getEndpoint(String atomicServiceId, int servicePort,
			String invocationPath) throws AtomicServiceNotFoundException,
			EndpointNotFoundException {

		ApplianceType type = getApplianceType(atomicServiceId);

		for (ATEndpoint endpoint : type.getEndpoints()) {
			int endpointPort = endpoint.getPort();
			String endpointInvocationPath = endpoint.getInvocation_path();
			logger.debug("Comparing {}=={} and {}=={}", new Object[] {
					endpointPort, servicePort, endpointInvocationPath,
					invocationPath });
			if (endpointPort == servicePort
					&& endpointPathEquals(endpointInvocationPath,
							invocationPath)) {
				return endpoint;
			}
		}

		logger.debug("Endpoint {}:{}/{} not found", new Object[] {
				atomicServiceId, servicePort, invocationPath });
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

	private List<ApplianceType> getApplianceTypes() {
		return air.getApplianceTypes();
	}

	/**
	 * Get appliance type information.
	 * @param applianceTypeName Appliance type name.
	 * @return Appliance type information.
	 * @throws AtomicServiceInstanceNotFoundException Thrown, when appliance
	 *             type is not found in AIR.
	 */
	private ApplianceType getApplianceType(String applianceTypeName)
			throws AtomicServiceNotFoundException {
		List<ApplianceType> applianceTypes = getApplianceTypes();

		for (ApplianceType applianceType : applianceTypes) {
			String name = applianceType.getName();
			if (name != null && name.equals(applianceTypeName)) {
				return applianceType;
			}
		}

		logger.debug("Atomic service {} not found", applianceTypeName);
		throw new AtomicServiceNotFoundException();
	}

	/**
	 * Register appliance types (with specific configuration id) for workflow.
	 * @param contextId Context id (e.k.a. workflow id).
	 * @param configIds List of appliance types configurations ids.
	 * @param priority Workflow priority.
	 */
	private void registerVms(String contextId, List<String> configIds,
			List<String> names, Integer priority) throws CloudFacadeException {
		if (configIds != null && configIds.size() > 0) {
			String[] ids = configIds.toArray(new String[0]);
			logger.debug(
					"Registering required atomic services in atmosphere {}",
					Arrays.toString(ids));

			AddRequiredAppliancesRequestImpl request = new AddRequiredAppliancesRequestImpl();
			request.setImportanceLevel(priority);
			request.setCorrelationId(contextId);
			request.setApplianceIdentities(getApplianceIdentities(configIds,
					names));

			ManagerResponse response = atmosphere
					.addRequiredAppliances(request);
			parseResponseAndThrowExceptionsWhenNeeded(response);
		}
	}

	/**
	 * @param configIds
	 * @return
	 */
	private List<ApplianceIdentityImpl> getApplianceIdentities(
			List<String> configIds, List<String> names) {
		List<ApplianceIdentityImpl> identities = new ArrayList<ApplianceIdentityImpl>();
		for (int i = 0; i < configIds.size(); i++) {
			String asId = configIds.get(i);
			ApplianceIdentityImpl identity = new ApplianceIdentityImpl();
			identity.setInitConfId(asId);
			String name = null;
			if (names != null && names.size() > i) {
				name = names.get(i);
			}
			identity.setName(name);
			identities.add(identity);
		}
		return identities;
	}

	/**
	 * Get workflow identified by context id. It also checks if the workflow
	 * belongs to current user. If not than exception is thrown.
	 * @param contextId Workflow context id.
	 * @param username Workflow owner username.
	 * @return Workflow detail.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or it
	 *             does not belong to the user.
	 */
	private WorkflowDetail getUserWorkflow(String contextId, String username)
			throws WorkflowNotFoundException {

		try {
			WorkflowDetail detail = air.getWorkflow(contextId);
			if (detail != null && detail.getVph_username().equals(username)) {
				return detail;
			} else {
				// workflow is not found or it depends to other user.
				throw new WorkflowNotFoundException();
			}
		} catch (ServerWebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				throw new WorkflowNotFoundException();
			} else {
				throw new CloudFacadeException();
			}
		}
	}

	/**
	 * Check if workflow identified by context id belongs to defined user. If
	 * not than exception is throw.
	 * @param contextId Workflow context id.
	 * @param username Workflow owner username.
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or it
	 *             does not belong to the user.
	 */
	private void checkIfWorkflowBelongsToUser(String contextId, String username)
			throws WorkflowNotFoundException {
		getUserWorkflow(contextId, username);
	}

	/**
	 * Set atmosphere client.
	 * @param atmosphere Atmosphere client.
	 */
	public void setAtmosphere(DyReAllaManagerService atmosphere) {
		this.atmosphere = atmosphere;
	}

	/**
	 * Set default workflow priority.
	 * @param defaultPriority Default workflow priority.
	 */
	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	/**
	 * Set AIR client.
	 * @param AIR client.
	 */
	public void setAir(AirClient air) {
		this.air = air;
	}

	/**
	 * @param defaultSiteId the defaultSiteId to set
	 */
	public void setDefaultSiteId(String defaultSiteId) {
		this.defaultSiteId = defaultSiteId;
	}

	/**
	 * @param credentialProperties
	 */
	public void setCredentialProperties(Properties credentialProperties) {
		this.credentialProperties = credentialProperties;
	}
}
