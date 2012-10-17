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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.UserKeyInfo;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListUserKeysAction extends ReadOnlyAirAction<List<PublicKeyInfo>> {

	private String username;

	ListUserKeysAction(AirClient air, String username) {
		super(air);
		this.username = username;
	}

	@Override
	public List<PublicKeyInfo> execute() throws CloudFacadeException {
		List<UserKeyInfo> userKeys = getUserKeys();
		List<PublicKeyInfo> keys = new ArrayList<PublicKeyInfo>();
		for (UserKeyInfo userKey : userKeys) {
			PublicKeyInfo key = getKey(userKey);
			keys.add(key);
		}

		return keys;
	}

	private List<UserKeyInfo> getUserKeys() {
		try {
			return getAir().getUserKeys(username);
		} catch (ServerWebApplicationException e) {
			if (e.getResponse().getStatus() == 400) {
				return new ArrayList<UserKeyInfo>();
			}
			throw new CloudFacadeException("Unable to get user keys from AiR");
		}
	}

	private PublicKeyInfo getKey(UserKeyInfo userKey) {
		PublicKeyInfo key = new PublicKeyInfo();
		key.setId(userKey.get_id());
		key.setKeyName(userKey.getName());
		key.setFingerprint(userKey.getFingerprint());

		return key;
	}
}
