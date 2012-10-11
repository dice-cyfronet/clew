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
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class DeleteSecurityPolicyAction extends AirAction<Class<Void>> {

	private String policyName;
	private String policyText;

	DeleteSecurityPolicyAction(AirClient air, String policyName) {
		super(air);
		this.policyName = policyName;
	}

	@Override
	public Class<Void> execute() throws CloudFacadeException {
		loadOldPolicyPayload();
		try {
			getAir().deleteSecurityPolicy(policyName);
		} catch (ServerWebApplicationException e) {
			if (e.getStatus() == 400) {
				throw new SecurityPolicyNotFoundException();
			}
			throw new CloudFacadeException(
					"Error while deleting security policy from Air, response code"
							+ e.getStatus());
		}

		return Void.TYPE;
	}

	@Override
	public void rollback() {
		try {
			if (policyText != null) {
				UploadSecurityPolicyAction action = new UploadSecurityPolicyAction(
						getAir(), policyName, policyText, false);
				action.execute();
			}
		} catch (Exception e) {
			// best effort.
		}
	}

	private void loadOldPolicyPayload() throws SecurityPolicyNotFoundException {
		GetSecurityPolicyAction getPolicyAction = new GetSecurityPolicyAction(
				getAir(), policyName);
		policyText = getPolicyAction.execute();
	}
}
