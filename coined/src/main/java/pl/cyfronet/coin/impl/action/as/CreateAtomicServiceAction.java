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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AtmosphereAndAirAction;
import pl.cyfronet.coin.impl.air.client.AppliancePreferences;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.dyrealla.api.ApplianceNotFoundException;
import pl.cyfronet.dyrealla.api.DyReAllaException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceAction extends AtmosphereAndAirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(CreateAtomicServiceAction.class);

	private NewAtomicService newAtomicService;
	private Action<String> addASToAirAction;

	// private String templateId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param atomicServiceInstanceId Atomic Service Instance id.
	 * @param newAtomicService Information about new Atomic Service.
	 */
	public CreateAtomicServiceAction(ActionFactory actionFactory,
			String username, NewAtomicService newAtomicService) {
		super(actionFactory, username);

		this.newAtomicService = newAtomicService;
	}

	/**
	 * Create Atomic Service from running Atomic Service Instance.
	 * @param atomicServiceInstanceId Atomic Service Instance id.
	 * @param atomicService Information about new Atomic Service.
	 * @return New created atomic service id.
	 * @throws AtomicServiceInstanceNotFoundException
	 * @throws CloudFacadeException
	 * @throws WorkflowNotFoundException Thrown when workflow is not found or
	 *             workflow with defined context id belongs to other user.
	 */
	public String execute() throws AtomicServiceInstanceNotFoundException,
			CloudFacadeException {
		String asInstanceId = newAtomicService.getSourceAsiId();

		logger.debug("Creating {} from {}", new Object[] {
				newAtomicService, newAtomicService });

		ApplianceType at = getActionFactory().createGetASITypeAction(
				asInstanceId).execute();

		at.setName(newAtomicService.getName());
		at.setDescription(newAtomicService.getDescription());
		at.setDevelopment(false);
		at.setPublished(getRawBoolean(newAtomicService.getPublished()));
		at.setScalable(getRawBoolean(newAtomicService.getScalable()));
		at.setShared(getRawBoolean(newAtomicService.getShared()));
		at.setProxy_conf_name(newAtomicService.getProxyConfigurationName());

		AppliancePreferences prefs = at.getAppliance_preferences();

		if (prefs == null) {
			prefs = new AppliancePreferences();
		}

		boolean prefsModified = false;
		if (newAtomicService.getCpu() != null) {
			prefs.setCpu(newAtomicService.getCpu());
			prefsModified = true;
		}

		if (newAtomicService.getDisk() != null) {
			prefs.setDisk(newAtomicService.getDisk());
			prefsModified = true;
		}

		if (newAtomicService.getMemory() != null) {
			prefs.setMemory(newAtomicService.getMemory());
			prefsModified = true;
		}

		if (prefsModified) {
			at.setAppliance_preferences(prefs);
		}

		addASToAirAction = getActionFactory()
				.createCreateAtomicServiceInAirAction(getUsername(), at);

		String atomicServiceId = addASToAirAction.execute();

		try {
			// templateId =
			getAtmosphere().createTemplate(asInstanceId,
					newAtomicService.getName(), atomicServiceId);
			return atomicServiceId;
		} catch (ApplianceNotFoundException e) {
			addASToAirAction.rollback();
			logger.error("Error while creating AS - ASI {} not found",
					asInstanceId);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			addASToAirAction.rollback();
			logger.error("Error while creating AS - atmosphere exception", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	private boolean getRawBoolean(Boolean b) {
		return b == null ? false : b.booleanValue();
	}

	@Override
	public void rollback() {
		// atmosphere.deleteTemplate(templateId);
		addASToAirAction.rollback();
	}
}