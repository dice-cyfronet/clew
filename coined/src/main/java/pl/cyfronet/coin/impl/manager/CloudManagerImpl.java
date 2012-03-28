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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstanceStatus;
import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;
import pl.cyfronet.dyrealla.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.allocation.impl.AddRequiredAppliancesRequestImpl;
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

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() {
		List<ApplianceType> applianceTypes = air.getApplianceTypes();
		List<AtomicService> atomicServices = new ArrayList<AtomicService>();
		for (ApplianceType applianceType : applianceTypes) {
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
			atomicServices.add(atomicService);
		}
		return atomicServices;
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
		registerVms(contextId, Arrays.asList(atomicServiceId), defaultPriority);

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
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService, String username)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException {
		throw new CloudFacadeException("Not impolemented yet");
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
			List<WorkflowBaseInfo> workflows = getWorkflows(username);
			for (WorkflowBaseInfo wInfo : workflows) {
				if (wInfo.getType() == type) {
					throw new WorkflowStartException(String.format(
							"Cannot start two %s workflows", type));
				}
			}
		}

		// FIXME error handling
		String workflowId = air.startWorkflow(workflow.getName(), username,
				workflow.getDescription(), priority, workflow.getType());
		List<String> ids = workflow.getAsConfigIds();

		registerVms(workflowId, ids, priority);

		return workflowId;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#stopWorkflow(java.lang.String)
	 */
	@Override
	public void stopWorkflow(String contextId, String username)
			throws WorkflowNotFoundException {
		logger.debug("stopping workflow {}", contextId);
		checkIfWorkflowBelongsToUser(contextId, username);
		try {
			atmosphere.removeRequiredAppliances(contextId);
		} catch (Exception e) {
			logger.error("error in atmosphere");
		}
		air.stopWorkflow(contextId);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getWorkflowStatus(java.lang
	 * .String)
	 */
	@Override
	public WorkflowStatus getWorkflowStatus(String contextId, String username)
			throws WorkflowNotFoundException {
		WorkflowDetail detail = getUserWorkflow(contextId, username);

		WorkflowStatus workflow = new WorkflowStatus();
		workflow.setName(detail.getName());

		List<Vms> vms = detail.getVms();
		Map<String, AtomicServiceStatus> asStatuses = new HashMap<String, AtomicServiceStatus>();
		if (vms != null) {
			for (Vms vm : vms) {
				if (vm.getVms_id() == null) {
					// /AIR returns vms:[null] when workflow does not have VMs
					// and it is parsed by CXF as Vms object with all properties
					// set to null.
					continue;
				}
				// but in AIR type internal id is returned instead of type name.
				// String type = vm.getAppliance_type();
				// if (type == null) {
				// // get type from AIR once again if it is empty
				// type = getAtomicServiceTypeName(vm.getConf_id());
				// }
				String type = getAtomicServiceTypeName(vm.getConf_id());
				AtomicServiceStatus asStatus = asStatuses.get(type);
				if (asStatus == null) {
					asStatus = new AtomicServiceStatus();
					asStatus.setId(type);
					asStatus.setName(type);
					asStatuses.put(type, asStatus);
				}
				AtomicServiceInstanceStatus asiStatus = new AtomicServiceInstanceStatus();
				asiStatus.setId(vm.getVms_id());
				asiStatus.setName(vm.getName());
				asiStatus.setStatus(vm.getState());
				asiStatus.setMessage(""); // TODO

				List<AtomicServiceInstanceStatus> asiStatuses = asStatus
						.getInstances();
				if (asiStatuses == null) {
					asiStatuses = new ArrayList<AtomicServiceInstanceStatus>();
					asStatus.setInstances(asiStatuses);
				}

				asiStatuses.add(asiStatus);
			}
		}

		for (AtomicServiceStatus asStatus : asStatuses.values()) {
			Status status = null;
			for (AtomicServiceInstanceStatus asiStatus : asStatus
					.getInstances()) {
				status = getHigherState(status, asiStatus.getStatus());
			}
			asStatus.setStatus(status);
		}

		List<AtomicServiceStatus> ases = new ArrayList<AtomicServiceStatus>(
				asStatuses.values());
		workflow.setAses(ases);

		return workflow;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getInitialConfiguration(java
	 * .lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfigurations(
			String atomicServiceId) throws ApplianceTypeNotFound {

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

	/**
	 * Get appliance type information.
	 * @param applianceTypeName Appliance type name.
	 * @return Appliance type information.
	 * @throws ApplianceTypeNotFound Thrown, when appliance type is not found in
	 *             AIR.
	 */
	private ApplianceType getApplianceType(String applianceTypeName)
			throws ApplianceTypeNotFound {
		List<ApplianceType> applianceTypes = air.getApplianceTypes();

		for (ApplianceType applianceType : applianceTypes) {
			String name = applianceType.getName();
			if (name != null && name.equals(applianceTypeName)) {
				return applianceType;
			}
		}

		throw new ApplianceTypeNotFound(applianceTypeName);
	}

	/**
	 * Get atomic service type name.
	 * @param configurationId Atomic service type configuration id.
	 * @return Atomic service type name.
	 */
	private String getAtomicServiceTypeName(String configurationId) {
		return air.getTypeFromConfig(configurationId).getName();
	}

	/**
	 * Register appliance types (with specific configuration id) for workflow.
	 * @param contextId Context id (e.k.a. workflow id).
	 * @param configIds List of appliance types configurations ids.
	 * @param priority Workflow priority.
	 */
	private void registerVms(String contextId, List<String> configIds,
			Integer priority) {
		if (configIds != null && configIds.size() > 0) {
			String[] ids = configIds.toArray(new String[0]);
			logger.debug(
					"Registering required atomic services in atmosphere {}",
					Arrays.toString(ids));

			AddRequiredAppliancesRequest request = new AddRequiredAppliancesRequestImpl();
			request.setImportanceLevel(priority);
			request.setCorrelationId(contextId);
			request.setApplianceInitConfigIds(ids);

			atmosphere.addRequiredAppliances(request);
		}
	}

	/**
	 * Get higher state, the order is as follow (starting from the lower one):
	 * running, paused, booting, stopping, stopped.
	 * @param currentState Current state.
	 * @param newState New state.
	 * @return Higher state.
	 */
	private Status getHigherState(Status currentState, Status newState) {
		return currentState != null
				&& currentState.ordinal() > newState.ordinal() ? currentState
				: newState;
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
				// workflow is found, but it does not depend to selected user.
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
}
