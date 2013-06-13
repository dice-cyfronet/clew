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

import static pl.cyfronet.coin.impl.BeanConverter.getAtomicService;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListAtomicServicesAction extends
		ReadOnlyAirAction<List<AtomicService>> {

	private String username;
	private boolean loadDescriptors;

	public ListAtomicServicesAction(ActionFactory actionFactory,
			String username, boolean loadDescriptors) {
		super(actionFactory);
		this.username = username;
		this.loadDescriptors = loadDescriptors;
	}

	/**
	 * Get list of all registered atomic services.
	 * @return List of all registered atomic services.
	 * @throws CloudFacadeException
	 */
	@Override
	public List<AtomicService> execute() {
		List<ApplianceType> applianceTypes = getApplianceTypes(loadDescriptors);
		List<AtomicService> atomicServices = new ArrayList<AtomicService>();
		for (ApplianceType applianceType : applianceTypes) {
			if (!applianceType.isDevelopment()
					|| username.equals(applianceType.getAuthor())) {
				AtomicService atomicService = getAtomicService(applianceType);
				atomicServices.add(atomicService);
			}
		}
		return atomicServices;
	}
}
