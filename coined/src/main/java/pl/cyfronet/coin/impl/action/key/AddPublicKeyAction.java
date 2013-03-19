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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.AtmosphereAndAirAction;
import pl.cyfronet.coin.impl.utils.PublicKeyUtils;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddPublicKeyAction extends AtmosphereAndAirAction<String> {

	private static final Logger logger = LoggerFactory
			.getLogger(AddPublicKeyAction.class);

	private String keyName;
	private String publicKeyContent;

	private String addedKeyId;

	public AddPublicKeyAction(ActionFactory actionFactory, String username,
			String keyName, String publicKeyContent) {
		super(actionFactory, username);
		this.keyName = keyName;
		this.publicKeyContent = publicKeyContent.trim();
	}

	@Override
	public String execute() throws CloudFacadeException {
		PublicKeyUtils.validate(publicKeyContent);
		try {
			addedKeyId = getAir().addKey(getUsername(), keyName,
					publicKeyContent,
					PublicKeyUtils.getFingerprint(publicKeyContent));
			return addedKeyId;
		} catch (ServerWebApplicationException e) {
			if (e.getResponse().getStatus() == 409) {
				throw new KeyAlreadyExistsException(keyName);
			}
			logger.warn("Error while contacting AiR", e);
			throw new CloudFacadeException("Error while contacting AiR");
		} catch (Exception e) {
			logger.warn("Error while contacting AiR", e);
			throw new CloudFacadeException("Error while contacting AiR");
		}
	}

	@Override
	public void rollback() {
		if (addedKeyId != null) {
			try {
				DeletePublicKeyAction action = new DeletePublicKeyAction(
						getActionFactory(), getUsername(), addedKeyId);
				action.execute();
			} catch (Exception e) {
				logger.warn("Unable to rollback", e);
				// best efford
			}
		}
	}

}
