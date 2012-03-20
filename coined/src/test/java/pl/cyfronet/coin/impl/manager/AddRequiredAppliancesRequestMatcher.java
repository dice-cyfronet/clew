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

package pl.cyfronet.coin.impl.manager;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import pl.cyfronet.dyrealla.allocation.AddRequiredAppliancesRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddRequiredAppliancesRequestMatcher extends
		BaseMatcher<AddRequiredAppliancesRequest> {

	private String contextId;

	private String[] atomicServiceIds;

	public AddRequiredAppliancesRequestMatcher(String contextId,
			String... atomicServiceIds) {
		this.contextId = contextId;
		this.atomicServiceIds = atomicServiceIds;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object arg0) {
		AddRequiredAppliancesRequest request = (AddRequiredAppliancesRequest) arg0;
		return request.getCorrelationId().equals(contextId)
				&& Arrays.equals(request.getApplianceInitConfigIds(),
						atomicServiceIds);
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description arg0) {

	}

}
