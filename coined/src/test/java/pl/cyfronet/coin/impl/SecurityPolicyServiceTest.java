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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.test.context.ContextConfiguration;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.Action;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration(locations={
		"classpath:securitypolicy/properties.xml",
		"classpath:securitypolicy/client.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",		
		"classpath:META-INF/spring/rest-services.xml"
	})
//@formatter:on
public class SecurityPolicyServiceTest extends AbstractOwnedPayloadServiceTest {

	@Override
	protected void mockListAction(Action<List<String>> action) {
		when(actionFactory.createListSecurityPoliciesAction()).thenReturn(
				action);
	}

	@Override
	protected void mockGetAction(String policyName,
			Action<NamedOwnedPayload> action) {
		when(actionFactory.createGetSecurityPolicyAction(policyName))
				.thenReturn(action);
	}

	@Override
	protected void mockGetPayloadAction(String name, Action<String> action) {
		when(actionFactory.createGetSecurityPolicyPayloadAction(name))
				.thenReturn(action);
	}

	@Override
	protected void mockNewAction(String username, NamedOwnedPayload payload,
			Action<Class<Void>> action) {
		when(actionFactory.createNewSecurityPolicyAction(username, payload))
				.thenReturn(action);
	}

	@Override
	protected void mockDeleteAction(String username, String name,
			Action<Class<Void>> action) {
		when(actionFactory.createDeleteSecurityPolicyAction(username, name))
				.thenReturn(action);
	}

	@Override
	protected void verifyNewAction(String username, NamedOwnedPayload payload) {
		verify(actionFactory, times(1)).createNewSecurityPolicyAction(username,
				payload);
	}

	@Override
	protected void verifyDeleteAction(String username, String name) {
		verify(actionFactory, times(1)).createDeleteSecurityPolicyAction(
				username, name);
	}
}