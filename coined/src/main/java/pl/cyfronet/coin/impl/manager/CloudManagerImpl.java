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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.AtomicServiceInstanceStatus;
import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
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
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudManagerImpl implements CloudManager {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CloudManagerImpl.class);

	private DyReAllaManagerService atmosphere;

	private AirClient air;

	private Integer defaultPriority;

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServiceInstances(
	 * java.lang.String)
	 */
	@Override
	public List<AtomicServiceInstance> getAtomicServiceInstances(
			String contextId) throws CloudFacadeException {

		WorkflowDetail workflow = air.getWorkflow(contextId);

		throw new CloudFacadeException("Not impolemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
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
			String contextId) throws AtomicServiceNotFoundException,
			CloudFacadeException {
		logger.debug("Add atomic service [{} {}] into workflow [{}]",
				new Object[] { name, atomicServiceId, contextId });
		registerVms(contextId, Arrays.asList(atomicServiceId), defaultPriority);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServiceStatus(java
	 * .lang.String)
	 */
	@Override
	public AtomicServiceInstance getAtomicServiceStatus(
			String atomicServiceInstanceId) throws CloudFacadeException {
		throw new CloudFacadeException("Not impolemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#stopAtomicServiceInstance(
	 * java.lang.String)
	 */
	@Override
	public void stopAtomicServiceInstance(String atomicServiceInstance)
			throws CloudFacadeException {
		throw new CloudFacadeException("Not impolemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#createAtomicService(java.lang
	 * .String, pl.cyfronet.coin.api.beans.AtomicService)
	 */
	@Override
	public void createAtomicService(String atomicServiceInstanceId,
			AtomicService atomicService)
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
	public String startWorkflow(WorkflowStartRequest workflow, String username) {
		logger.debug("starting workflow {} for {} user", workflow, username);

		Integer priority = workflow.getPriority();
		if (priority != null) {
			priority = defaultPriority;
		}

		// FIXME error handling
		String workflowId = air.startWorkflow(workflow.getName(), username,
				workflow.getDescription(), priority, workflow.getType());
		List<String> ids = workflow.getRequiredIds();

		registerVms(workflowId, ids, priority);

		return workflowId;
	}

	@Override
	public void stopWorkflow(String contextId) {
		logger.debug("stopping workflow {}", contextId);
		// FIXME error handling
		atmosphere.removeRequiredAppliances(contextId);
		air.stopWorkflow(contextId);
	}

	@Override
	public WorkflowStatus getWorkflowStatus(String contextId) {
		WorkflowDetail detail = air.getWorkflow(contextId);

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
				String type = vm.getAppliance_type();
				if (type == null) {
					// get type from AIR once again if it is empty
					type = getAtomicServiceTypeName(vm.getConf_id());
				}
				AtomicServiceStatus asStatus = asStatuses.get(type);
				if (asStatus == null) {
					asStatus = new AtomicServiceStatus();
					asStatus.setId(type);
					asStatuses.put(type, asStatus);
				}
				AtomicServiceInstanceStatus asiStatus = new AtomicServiceInstanceStatus();
				asiStatus.setId(vm.getVms_id());
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

		List<AtomicServiceStatus> ases = new ArrayList<AtomicServiceStatus>(
				asStatuses.values());
		workflow.setAses(ases);

		return workflow;
	}

	private String getAtomicServiceTypeName(String configurationId) {
		return air.getTypeFromConfig(configurationId).getName();
	}

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

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getInitialConfiguration(java
	 * .lang.String)
	 */
	@Override
	public List<InitialConfiguration> getInitialConfiguration(
			String atomicServiceId) throws ApplianceTypeNotFound {

		ApplianceType type = getApplianceType(atomicServiceId);
		List<ApplianceConfiguration> typeConfigurations = type
				.getConfigurations();
		List<InitialConfiguration> configurations = new ArrayList<InitialConfiguration>();
		for (ApplianceConfiguration applianceConfiguration : typeConfigurations) {
			InitialConfiguration configuration = new InitialConfiguration();
			configuration.setId(applianceConfiguration.getId());
			configuration.setName(applianceConfiguration.getConfig_name());
			configurations.add(configuration);
		}

		return configurations;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * pl.cyfronet.coin.impl.manager.CloudManager#getWorkflows(java.lang.String)
	 */
	@Override
	public Map<String, String> getWorkflows(String username) {
		List<WorkflowDetail> workflowDetails = air.getUserWorkflows(username);
		Map<String, String> workflows = new HashMap<String, String>();
		for (WorkflowDetail workflowDetail : workflowDetails) {
			workflows.put(workflowDetail.getId(), workflowDetail.getName());
		}

		return workflows;
	}

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
	 * @param atmosphere the atmosphere to set
	 */
	public void setAtmosphere(DyReAllaManagerService atmosphere) {
		this.atmosphere = atmosphere;
	}

	/**
	 * @param defaultPriority the defaultPriority to set
	 */
	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	/**
	 * @param air the air to set
	 */
	public void setAir(AirClient air) {
		this.air = air;
	}
}
