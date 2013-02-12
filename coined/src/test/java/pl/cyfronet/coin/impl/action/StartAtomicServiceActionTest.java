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

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.mock.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;
import pl.cyfronet.dyrealla.api.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.api.allocation.impl.ManagerResponseImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartAtomicServiceActionTest extends WorkflowActionTest {

	private String atomicServiceId = "asId";
	private String name = "asIdName";

	private String id;
	private String keyId = "myKey";
	private AddRequiredAppliancesRequestMatcher matcher;
	private AddRequiredAppliancesRequestImpl request;

	@Test
	public void shouldStartWithoutKeyWhenProductionWorkflow() throws Exception {
		givenAtomicServiceRequestAndWorkflowAlreadyStarted(WorkflowType.portal);
		whenStartAtomicService();
		thenCheckIfAtomicServiceWasStarted();
	}

	private void givenAtomicServiceRequestAndWorkflowAlreadyStarted(
			WorkflowType workflowType) {
		givenASInAir(atomicServiceId, false);
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(workflowType);

		matcher = new AddRequiredAppliancesRequestMatcher(contextId, true,
				defaultPriority, username, workflowType, atomicServiceId);
		matcher.setGivenKeyId(keyId);
		givenWorkflowStarted(wd);
		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(OperationStatus.SUCCESSFUL));
	}

	private void whenStartAtomicService() {
		whenStartAtomicService(username, keyId);
	}

	private void whenStartAtomicService(String username, String keyId) {
		Action<String> action = actionFactory.createStartAtomicServiceAction(
				atomicServiceId, name, contextId, username, keyId);
		id = action.execute();
	}

	private void thenCheckIfAtomicServiceWasStarted() {
		verify(atmosphere, times(1)).addRequiredAppliances(argThat(matcher));

		verify(air, times(1)).getWorkflow(contextId);

		// TODO Atmosphere should return ASI instance in response.
		assertNull(id);
	}

	@Test
	public void shouldStartASWithKeyWhenDevelopmentWorkflow() throws Exception {
		givenAtomicServiceRequestAndWorkflowAlreadyStarted(WorkflowType.development);
		whenStartAtomicService();
		thenCheckIfAtomicServiceWasStarted();
	}

	@Test
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotBelongToTheUser()
			throws Exception {
		givenWorkflowStarted();
		try {
			whenNotWorkflowOwnerTriesToStartASForThisWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK
		}
		thenVerifyThatAirWasAskedAboutUserWorkflows();
	}

	private void whenNotWorkflowOwnerTriesToStartASForThisWorkflow() {
		whenStartAtomicService("otherUser", null);
	}

	@Test
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotExist()
			throws Exception {
		givenNoWorkflowStartedForTheUser();
		try {
			whenStartAtomicService();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK
		}
		thenVerifyThatAirWasAskedAboutUserWorkflows();
	}

	private void thenVerifyThatAirWasAskedAboutUserWorkflows() {
		verify(air, times(1)).getWorkflow(contextId);
	}

	private void givenNoWorkflowStartedForTheUser() {
		mockGetNonExistingWorkflow(air, contextId);
	}

	// FIXME
	@Test(enabled = false)
	public void shouldCreateExceptionWhileAtmosphereActionFailed()
			throws Exception {
		givenAtmosphereReturnsErrorWhileStartingAtomicService();

		try {
			whenStartAtomicService();
			fail();
		} catch (CloudFacadeException e) {
			// OK
		}

		thenVerifyRequestSendToAtmosphere();
	}

	private void givenAtmosphereReturnsErrorWhileStartingAtomicService() {
		givenASInAir(atomicServiceId, false);
		request = new AddRequiredAppliancesRequestImpl();

		ManagerResponse response = new ManagerResponseImpl();
		response.setOperationStatus(OperationStatus.FAILED);

		when(atmosphere.addRequiredAppliances(request)).thenReturn(response);
	}

	private void thenVerifyRequestSendToAtmosphere() {
		verify(atmosphere, times(1)).addRequiredAppliances(request);
	}

	@Test
	public void shouldThrowASNotFoundWhileTryingToStartDevelopmentAS()
			throws Exception {
		givenWorkflowStarted();
		givenASInAir(atomicServiceId, true);
		try {
			whenStartAtomicService();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// Ok should be thrown
		}
	}
}
