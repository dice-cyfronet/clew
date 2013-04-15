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

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddInitialConfigurationAction extends AirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddInitialConfigurationAction.class);

	private String atomicServiceId;
	private InitialConfiguration initialConfiguration;

	public AddInitialConfigurationAction(ActionFactory actionFactory,
			String atomicServiceId, InitialConfiguration initialConfiguration) {
		super(actionFactory);
		this.atomicServiceId = atomicServiceId;
		this.initialConfiguration = initialConfiguration;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			logger.debug(
					"Adding initial configuration {} for {} atomic service",
					initialConfiguration.getName(), atomicServiceId);
			String addedConfigurationId = getAir().addInitialConfiguration(
					initialConfiguration.getName(), atomicServiceId,
					initialConfiguration.getPayload());

			return addedConfigurationId;
		} catch (WebApplicationException e) {
			if (e.getResponse().getEntity() != null) {
				String msg = e.getResponse().getEntity().toString();
				if (msg.contains("not found in AIR")) {
					logger.warn("Atomic service {} not found in AIR",
							atomicServiceId);
					throw new AtomicServiceNotFoundException();
				} else if (msg.contains("duplicated configuration")) {
					logger.warn(
							"Initial configuration {} for {} AS already exist in AIR",
							initialConfiguration.getName(), atomicServiceId);
					throw new InitialConfigurationAlreadyExistException();
				}
			}
			logger.error("Exception thrown from AIR", e);
			throw new CloudFacadeException(e.getMessage());
		}
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
