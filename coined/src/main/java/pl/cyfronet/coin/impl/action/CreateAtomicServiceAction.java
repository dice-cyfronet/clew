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

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.ApplianceNotFoundException;
import pl.cyfronet.dyrealla.DyReAllaException;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceAction implements Action<String> {

	private DyReAllaManagerService atmosphere;
	private String defaultSiteId;
	private String asInstanceId;
	private AtomicService atomicService;
	private CreateAtomicServiceInAirAction addASToAirAction;

	// private String templateId;

	/**
	 * @param air Air client.
	 * @param atmosphere Atmosphere client.
	 * @param defaultSiteId Default cloud site id.
	 * @param atomicServiceInstanceId Atomic Service Instance id.
	 * @param atomicService Information about new Atomic Service.
	 */
	CreateAtomicServiceAction(AirClient air, DyReAllaManagerService atmosphere,
			String defaultSiteId, String asInstanceId,
			AtomicService atomicService) {
		this.atmosphere = atmosphere;
		this.defaultSiteId = defaultSiteId;
		this.asInstanceId = asInstanceId;
		this.atomicService = atomicService;

		addASToAirAction = new CreateAtomicServiceInAirAction(air,
				atomicService);
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
		String atomicServiceId = addASToAirAction.execute();
		try {
			// templateId =
			atmosphere.createTemplate(asInstanceId, atomicService.getName(),
					defaultSiteId, atomicServiceId);
			return atomicService.getName();
		} catch (ApplianceNotFoundException e) {
			addASToAirAction.rollback();
			throw new AtomicServiceInstanceNotFoundException();
		} catch (DyReAllaException e) {
			addASToAirAction.rollback();
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// atmosphere.deleteTemplate(templateId);
		addASToAirAction.rollback();
	}
}