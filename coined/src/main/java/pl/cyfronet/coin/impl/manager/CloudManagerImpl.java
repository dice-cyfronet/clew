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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
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
		throw new CloudFacadeException("Not impolemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see pl.cyfronet.coin.impl.manager.CloudManager#getAtomicServices()
	 */
	@Override
	public List<AtomicService> getAtomicServices() throws CloudFacadeException {
		throw new CloudFacadeException("Not impolemented yet");
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
	public String startWorkflow(Workflow workflow, String username) {
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
