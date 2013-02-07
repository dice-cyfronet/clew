/*
 * Copyright 2013 ACC CYFRONET AGH
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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteAtomicServiceAction extends AirAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteAtomicServiceAction.class);

	private List<String> atomicServicesNames;

	public DeleteAtomicServiceAction(AirClient air, String atomicServiceName) {
		this(air, Arrays.asList(atomicServiceName));
	}

	public DeleteAtomicServiceAction(AirClient air,
			List<String> atomicServicesNames) {
		super(air);
		this.atomicServicesNames = atomicServicesNames;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		logger.debug("Removing {}", atomicServicesNames);

		for (String atomicServiceName : atomicServicesNames) {
			GetInitialConfigurationsAction initConfsAction = new GetInitialConfigurationsAction(
					getAir(), atomicServiceName);
			List<InitialConfiguration> initConfs = initConfsAction.execute();
			for (InitialConfiguration initConf : initConfs) {
				logger.debug("Removing initial configuration {}", initConf.getId());
				getAir().removeInitialConfiguration(initConf.getId());
			}
			logger.debug("Removing atomic service {}", atomicServiceName);
			getAir().deleteAtomicService(atomicServiceName);
		}
		return Void.TYPE;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

}
