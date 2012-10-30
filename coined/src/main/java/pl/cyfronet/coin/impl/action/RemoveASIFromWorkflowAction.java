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

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class RemoveASIFromWorkflowAction extends WorkflowAction<Class<Void>> {

	/**
	 * @param air
	 * @param atmosphere
	 * @param username
	 */
	RemoveASIFromWorkflowAction(AirClient air,
			DyReAllaManagerService atmosphere, String username) {
		super(air, atmosphere, username);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
