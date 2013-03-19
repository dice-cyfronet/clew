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

package pl.cyfronet.coin.impl.action.as;

import java.util.Arrays;
import java.util.List;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AirAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteAtomicServiceFromAirAction extends AirAction<Class<Void>> {

	private static final Logger logger = LoggerFactory
			.getLogger(DeleteAtomicServiceFromAirAction.class);

	private List<String> atomicServicesIds;

	public DeleteAtomicServiceFromAirAction(ActionFactory actionFactory,
			String atomicServiceId) {
		this(actionFactory, Arrays.asList(atomicServiceId));
	}

	public DeleteAtomicServiceFromAirAction(ActionFactory actionFactory,
			List<String> atomicServicesIds) {
		super(actionFactory);
		this.atomicServicesIds = atomicServicesIds;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		logger.debug("Removing {}", atomicServicesIds);

		for (String atomicServiceId : atomicServicesIds) {
			logger.debug(
					"Removing atomic service {} with force_cascade == true",
					atomicServiceId);
			try {
				getAir().deleteAtomicService(atomicServiceId, true);
			} catch (ServerWebApplicationException e) {
				if (e.getStatus() == 404) {
					throw new AtomicServiceNotFoundException();
				} else if (e.getStatus() == 400) {
					throw new NotAcceptableException(e.getMessage());
				}
				throw new CloudFacadeException(e.getMessage());
			}
		}
		return Void.TYPE;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}
}
