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

package pl.cyfronet.coin.impl.action.key;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetPublicKeyAction extends ReadOnlyAirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(GetPublicKeyAction.class);

	private String username;
	private String keyId;

	/**
	 * @param air
	 */
	public GetPublicKeyAction(ActionFactory actionFactory, String username,
			String keyId) {
		super(actionFactory);
		this.username = username;
		this.keyId = keyId;
	}

	@Override
	public String execute() throws CloudFacadeException {
		try {
			return getAir().getPublicKey(username, keyId);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 400) {
				throw new KeyNotFoundException();
			}
			logger.warn("Error while contacting AiR", e);
			throw new CloudFacadeException("Error while contacting AiR");
		} catch (Exception e) {
			logger.warn("Error while contacting AiR", e);
			throw new CloudFacadeException("Error while contacting AiR");
		}
	}
}
