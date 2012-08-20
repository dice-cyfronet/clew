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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.BeanConverter;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
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
		WorkflowDetail workflow = getUserWorkflow(contextId, username);
		logger.debug("Add atomic service [{} {}] into workflow [{}]",
				new Object[] { name, atomicServiceId, contextId });
		registerVms(contextId, Arrays.asList(atomicServiceId),
				Arrays.asList(name), defaultPriority,
				workflow.getWorkflow_type());

		// TODO information from Atmosphere about atomis service instance id
		// needed!
		return null;
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
		if (response.getErrors() != null) {
			return response.getErrors().toString();
		}

		return null;
	}

	@Override
	public List<Endpoint> getEndpoints() {
		List<ApplianceType> types = getApplianceTypes();
		List<Endpoint> allEndpoints = new ArrayList<Endpoint>();
		for (ApplianceType type : types) {
			List<Endpoint> typeEndpoints = BeanConverter.getEndpoints(type);
			if (typeEndpoints != null) {
				allEndpoints.addAll(typeEndpoints);
			}
		}
		return allEndpoints;
	}

	private List<ApplianceType> getApplianceTypes() {
		return air.getApplianceTypes();
	}

	/**
	 * Register appliance types (with specific configuration id) for workflow.
	 * @param contextId Context id (e.k.a. workflow id).
	 * @param configIds List of appliance types configurations ids.
	 * @param priority Workflow priority.
	 */
	private void registerVms(String contextId, List<String> configIds,
			List<String> names, Integer priority, WorkflowType workflowType)
			throws CloudFacadeException {
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
			// FIXME
			// request.setRunMode()

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
