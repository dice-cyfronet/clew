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

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UploadSecurityPolicyAction extends AirAction<Class<Void>> {

	private String policyName;
	private boolean overwrite;
	private String policyText;

	private String oldPolicyPayload = null;

	UploadSecurityPolicyAction(AirClient air, String policyName,
			String policyText, boolean overwrite) {
		super(air);
		this.policyName = policyName;
		this.overwrite = overwrite;
		this.policyText = policyText;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		if (overwrite) {
			loadOldPolicyPayload();
		}

		try {
			getAir().uploadSecurityPolicy(policyName, policyText, overwrite);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new SecurityPolicyAlreadyExistException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getStatus());
		}

		return Void.TYPE;
	}

	private void loadOldPolicyPayload() {
		GetSecurityPolicyAction getPolicyAction = new GetSecurityPolicyAction(
				getAir(), policyName);
		try {
			oldPolicyPayload = getPolicyAction.execute();
		} catch (SecurityPolicyNotFoundException e) {
			// ok policy does not exist
		}
	}

	@Override
	public void rollback() {
		try {
			if (oldPolicyPayload != null) {
				UploadSecurityPolicyAction action = new UploadSecurityPolicyAction(
						getAir(), policyName, oldPolicyPayload, true);
				action.execute();
			} else {
				DeleteSecurityPolicyAction action = new DeleteSecurityPolicyAction(
						getAir(), policyName);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

}
