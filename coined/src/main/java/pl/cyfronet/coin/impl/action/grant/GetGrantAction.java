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

package pl.cyfronet.coin.impl.action.grant;

import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class GetGrantAction extends ReadOnlyAirAction<Grant> {

	public GetGrantAction(AirClient air, String name) {
		super(air);
	}

	@Override
	public Grant execute() throws CloudFacadeException {
		Grant grant = new Grant();
		grant.setDelete("delete \"excaped\" regexp");
		grant.setGet("get regexp");
		grant.setPost("post regexp");
		grant.setPut("put regexp");
		
		return grant;
	}

}
