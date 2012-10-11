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

import static pl.cyfronet.coin.impl.BeanConverter.getAtomicService;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetAtomicServiceAction extends AirAction<AtomicService> {

	private String atomicServiceId;

	/**
	 * @param air Air rest client.
	 * @param atomicServiceId Atomic service instance. Right now it is equals
	 *            into atomic service name.
	 */
	GetAtomicServiceAction(AirClient air, String atomicServiceId) {
		super(air);
		this.atomicServiceId = atomicServiceId;
	}

	/**
	 * Get atomic service.
	 * @return Atomic service
	 * @throws AtomicServiceNotFoundException Thrown if atomic service with
	 *             given id is not registered in atmosphere.
	 */
	@Override
	public AtomicService execute() throws CloudFacadeException {
		ApplianceType applianceType = getApplianceType(atomicServiceId);
		return getAtomicService(applianceType);
	}

	@Override
	public void rollback() {
		// read only action, no rollback needed
	}

}
