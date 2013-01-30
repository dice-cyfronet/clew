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

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetInitialConfigurationsAction extends AirAction<List<InitialConfiguration>> {

	private String atomicServiceName;
	
	/**
	 * @param air Air client.
	 * @param atomicServiceName Atomic Service id.
	 */
	public GetInitialConfigurationsAction(AirClient air,
			String atomicServiceName) {
		super(air);
		this.atomicServiceName = atomicServiceName;
	}

	/**
	 * Get Atomic Service initial configurations.
	 * @return List of Atomic Service configurations.
	 */
	@Override
	public List<InitialConfiguration> execute() throws CloudFacadeException {
		ApplianceType type = getApplianceType(atomicServiceName);
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

	@Override
	public void rollback() {
		// read only action, no rollback needed.
	}
}
