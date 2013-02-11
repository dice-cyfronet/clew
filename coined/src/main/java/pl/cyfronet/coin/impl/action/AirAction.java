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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * 
 * @param <T> Result generated by the action.
 */
public abstract class AirAction<T> implements Action<T> {

	static final Logger logger = LoggerFactory.getLogger(AirAction.class);

	private AirClient air;

	AirAction(AirClient air) {
		this.air = air;
	}

	protected List<ApplianceType> getApplianceTypes() {
		return air.getApplianceTypes();
	}

	protected AirClient getAir() {
		return air;
	}

	protected ApplianceType getApplianceType(String applianceTypeId)
			throws AtomicServiceNotFoundException {
		List<ApplianceType> applianceTypes = getApplianceTypes();

		for (ApplianceType applianceType : applianceTypes) {
			String id = applianceType.get_id();
			if (id != null && id.equals(applianceTypeId)) {
				return applianceType;
			}
		}

		logger.debug("Atomic service {} not found", applianceTypeId);
		throw new AtomicServiceNotFoundException();
	}
	
	protected void loadEndpointDescriptors(ApplianceType applianceType) {
		if(applianceType.getEndpoints() != null) {
			for (ATEndpoint endpoint : applianceType.getEndpoints()) {
				endpoint.setDescriptor(air.getEndpointDescriptor(endpoint.getId()));
			}
		}
	}

}
