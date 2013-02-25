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

import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetAsiRedirectionsAction extends AirAction<List<Redirection>> {

	private String asiId;
	private String contextId;

	GetAsiRedirectionsAction(String contextId, String asiId, AirClient air) {
		super(air);
		this.contextId = contextId;
		this.asiId = asiId;
	}

	@Override
	public List<Redirection> execute() throws CloudFacadeException {
		// TODO Auto-generated method stub
		// uzyc 2 metod AIR
		// 1) pobrac workflow (patrz na klase workflow action) wf detail, wyciagnac vm, wyciagnac info o przekierowaniach tcp/ip
		// 2) dla vm pobrac appliance type i z niego przekirowania http, patrz na getApplianceType z workflow action 
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
	}
}
