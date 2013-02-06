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

package pl.cyfronet.coin.impl;

import java.util.Arrays;
import java.util.List;

import pl.cyfronet.coin.api.GrantService;
import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantAlreadyExistException;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GrantServiceImpl implements GrantService {

	@Override
	public List<String> listGrants() {
		return Arrays.asList("grant1", "grant2", "grant3");
	}

	@Override
	public Grant getGrant(String grantName) {
		Grant grant = new Grant();
		grant.setDelete("delete \"excaped\" regexp");
		grant.setGet("get regexp");
		grant.setPost("post regexp");
		grant.setPut("put regexp");

		return grant;
	}

	@Override
	public void updateGrant(String grantName, String get, String post,
			String put, String delete, boolean overwrite)
			throws GrantAlreadyExistException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteGrant(String grantName) throws GrantNotFoundException {
		// TODO Auto-generated method stub

	}
}
