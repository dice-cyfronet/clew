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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.manager.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.manager.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.allocation.impl.ManagerResponseImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AtomicServiceInstanceCloudManagerTest extends
		AbstractCloudManagerTest {

	@Test
	public void shouldStartNewAtomicService() throws Exception {
		// given
		final String atomicServiceId = "asId";
		final String name = "asIdName";
		final String contextId = "contextId";
		String username = "user";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);

		AddRequiredAppliancesRequestMatcher matcher = new AddRequiredAppliancesRequestMatcher(
				contextId, true, atomicServiceId);

		// when
		mockGetWorkflow();
		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(OperationStatus.SUCCESSFUL));

		String id = manager.startAtomicService(atomicServiceId, name,
				contextId, username);

		// then
		verify(atmosphere, times(1)).addRequiredAppliances(argThat(matcher));

		verify(air, times(1)).getWorkflow(contextId);

		// TODO Atmosphere should return ASI instance in response.
		assertNull(id);
	}

	@Test(expectedExceptions = WorkflowNotFoundException.class)
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotBelongToTheUser()
			throws Exception {
		// when
		mockGetWorkflow();
		manager.startAtomicService("1", "name", contextId, "otherUser");

		// then
	}

	@Test(expectedExceptions = WorkflowNotFoundException.class)
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotExist()
			throws Exception {
		// given
		String contextId = "contextId";

		// when
		mockGetNonExistingWorkflow(air, contextId);
		manager.startAtomicService("1", "name", contextId, "user");

		// then
	}

	// FIXME
	@Test(enabled = false)
	public void shouldCreateExceptionWhileAtmosphereActionFailed()
			throws Exception {
		// given
		AddRequiredAppliancesRequestImpl request = new AddRequiredAppliancesRequestImpl();

		ManagerResponse response = new ManagerResponseImpl();
		response.setOperationStatus(OperationStatus.FAILED);

		// when
		when(atmosphere.addRequiredAppliances(request)).thenReturn(response);

		try {
			manager.startAtomicService("asId", "name", "contextId", "username");
			fail();
		} catch (CloudFacadeException e) {
			e.getMessage();
		}

		// then

	}
}
