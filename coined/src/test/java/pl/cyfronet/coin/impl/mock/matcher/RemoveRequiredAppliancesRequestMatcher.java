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

import pl.cyfronet.dyrealla.api.allocation.RemoveRequiredAppliancesRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveRequiredAppliancesRequestMatcher extends
		BaseMatcher<RemoveRequiredAppliancesRequest> {

	private String contextId;
	private String asInitConf;

	public RemoveRequiredAppliancesRequestMatcher(String contextId,
			String asInitConf) {
		this.contextId = contextId;
		this.asInitConf = asInitConf;
	}

	@Override
	public boolean matches(Object arg0) {
		RemoveRequiredAppliancesRequest request = (RemoveRequiredAppliancesRequest) arg0;
		List<String> initConfs = request.getInitConfigIds();
		return request.getApplicationId().equals(contextId)
				&& initConfs != null && initConfs.size() == 1
				&& initConfs.get(0).equals(asInitConf);
	}

	@Override
	public void describeTo(Description arg0) {

	}

}
