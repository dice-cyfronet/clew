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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddInitialConfigurationAction extends AirAction<String> {

	private String atomicServiceId;
	private InitialConfiguration initialConfiguration;

	AddInitialConfigurationAction(AirClient air, String atomicServiceId,
			InitialConfiguration initialConfiguration) {
		super(air);
		this.atomicServiceId = atomicServiceId;
		this.initialConfiguration = initialConfiguration;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			String addedConfigurationId = getAir().addInitialConfiguration(
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
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
