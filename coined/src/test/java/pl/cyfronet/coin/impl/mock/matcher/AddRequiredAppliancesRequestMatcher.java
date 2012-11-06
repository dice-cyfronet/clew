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
package pl.cyfronet.coin.impl.mock.matcher;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.dyrealla.api.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.api.allocation.ApplianceIdentity;
import pl.cyfronet.dyrealla.api.allocation.RunMode;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddRequiredAppliancesRequestMatcher extends
		BaseMatcher<AddRequiredAppliancesRequest> {

	private String contextId;

	private String[] atomicServiceIds;

	private boolean checkName;

	private Integer importanceLevel;

	private String username;

	private WorkflowType workflowType;

	private String givenKeyId;

	public AddRequiredAppliancesRequestMatcher(String contextId,
			Integer importanceLevel, String username,
			WorkflowType workflowType, String... atomicServiceIds) {
		this(contextId, false, importanceLevel, username, workflowType,
				atomicServiceIds);
	}

	public AddRequiredAppliancesRequestMatcher(String contextId,
			boolean checkName, Integer importanceLevel, String username,
			WorkflowType workflowType, String... atomicServiceIds) {
		this.contextId = contextId;
		this.atomicServiceIds = atomicServiceIds;
		this.checkName = checkName;
		this.username = username;
		this.workflowType = workflowType;
		this.importanceLevel = importanceLevel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object arg0) {
		AddRequiredAppliancesRequest request = (AddRequiredAppliancesRequest) arg0;
		return request.getCorrelationId().equals(contextId)
				&& username.equals(request.getUsername())
				&& getRunMode() == request.getRunMode()
				&& importanceLevel.equals(request.getImportanceLevel())
				&& validKey(request.getKeyPairId())
				&& equals(request.getApplianceIdentities(), atomicServiceIds);
	}

	/**
	 * @param keyPairId
	 * @return True if run mode is production and key is null or when
	 *         development mode and key equals to given key.
	 */
	private boolean validKey(String keyPairId) {
		if (getRunMode() == RunMode.PRODUCTION || givenKeyId == null) {
			return keyPairId == null;
		} else {
			return givenKeyId.equals(keyPairId);
		}
	}

	private RunMode getRunMode() {
		return workflowType == WorkflowType.development ? RunMode.DEVELOPMENT
				: RunMode.PRODUCTION;
	}

	private boolean equals(List<ApplianceIdentity> identites,
			String[] atomicServiceIds) {
		if (identites.size() == atomicServiceIds.length) {
			for (int i = 0; i < identites.size(); i++) {
				ApplianceIdentity identity = identites.get(i);
				String id = atomicServiceIds[i];
				String name = checkName ? id + "Name" : null;
				if (!equals(identity.getInitConfId(), id)
						|| !equals(identity.getName(), name)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private boolean equals(String a, String b) {
		return a != null ? a.equals(b) : b == null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description arg0) {

	}

	public void setGivenKeyId(String givenKeyId) {
		this.givenKeyId = givenKeyId;
	}

}
