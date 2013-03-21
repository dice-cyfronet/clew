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
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.dyrealla.api.ApplianceNotFoundException;
import pl.cyfronet.dyrealla.api.DyReAllaException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceAction extends AtmosphereAndAirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(CreateAtomicServiceAction.class);

	private String defaultSiteId;
	private NewAtomicService newAtomicService;
	private Action<String> addASToAirAction;

	// private String templateId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param defaultSiteId Default cloud site id.
	 * @param atomicServiceInstanceId Atomic Service Instance id.
	 * @param newAtomicService Information about new Atomic Service.
	 */
	public CreateAtomicServiceAction(ActionFactory actionFactory,
			String username, String defaultSiteId,
			NewAtomicService newAtomicService) {
		super(actionFactory, username);

		this.defaultSiteId = defaultSiteId;
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

		logger.debug("Creating {} from {} on {}", new Object[] {
				newAtomicService, newAtomicService.getSourceAsiId(),
				defaultSiteId });

		ApplianceType at = getActionFactory().createGetASITypeAction(
				asInstanceId).execute();

		at.setName(newAtomicService.getName());
		at.setDescription(newAtomicService.getDescription());
		at.setDevelopment(false);
		at.setPublished(newAtomicService.getPublished());
		at.setScalable(newAtomicService.getScalable());
		at.setShared(newAtomicService.getShared());
		at.setProxy_conf_name(newAtomicService.getProxyConfigurationName());

		addASToAirAction = getActionFactory()
				.createCreateAtomicServiceInAirAction(getUsername(), at);

		String atomicServiceId = addASToAirAction.execute();

		try {
			// templateId =
			getAtmosphere().createTemplate(asInstanceId,
					newAtomicService.getName(), defaultSiteId, atomicServiceId);
			return atomicServiceId;
		} catch (ApplianceNotFoundException e) {
			addASToAirAction.rollback();
			logger.debug("Error while creating AS - ASI {} not found",
					asInstanceId);
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			addASToAirAction.rollback();
			logger.debug("Error while creating AS - atmosphere exception", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// atmosphere.deleteTemplate(templateId);
		addASToAirAction.rollback();
	}
}