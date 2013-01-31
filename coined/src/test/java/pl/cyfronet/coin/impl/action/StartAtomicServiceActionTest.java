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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.mock.matcher.AddAtomicServiceMatcher;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;
import pl.cyfronet.dyrealla.api.allocation.impl.AddRequiredAppliancesRequestImpl;
import pl.cyfronet.dyrealla.api.allocation.impl.ManagerResponseImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartAtomicServiceActionTest extends WorkflowActionTest {

	private String name = "asIdName";

	private String id;
	private AddRequiredAppliancesRequestImpl request;
	private ApplianceType baseAS;
	private AtomicService as;

	@Test
	public void shouldStartWithoutKeyWhenProductionWorkflow() throws Exception {
		givenAtomicServiceRequestAndWorkflowAlreadyStarted(WorkflowType.portal);
		whenStartAtomicService();
		thenCheckIfAtomicServiceWasStarted();
	}

	private ApplianceType givenAtomicServiceRequestAndWorkflowAlreadyStarted(
			WorkflowType workflowType) {		
		givenWorkflowStarted(workflowType);
		givenAsiRequestMatcher(workflowType);
		ApplianceType at = givenASInAir(initConfigId, false);
		
		return at;
	}

	private void whenStartAtomicService() {
		whenStartAtomicService(username, keyId);
	}

	private void whenStartAtomicService(String username, String keyId) {
		StartAtomicServiceAction action = actionFactory
				.createStartAtomicServiceAction(initConfigId, name, contextId,
						username, keyId);
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
		givenMockedAtmosphereForStartingASInDevMode();
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
		givenASInAir(initConfigId, false);
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
		givenASInAir(initConfigId, true);
		try {
			whenStartAtomicService();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// Ok should be thrown
		}
	}

	@Test
	public void shouldCreateTmpASWhileStartingASIInDevelopmentMode()
			throws Exception {

		givenMockedAtmosphereForStartingASInDevMode();
		AddAtomicServiceMatcher asMatcher = new AddAtomicServiceMatcher(
				username, as, true);

		whenStartAtomicService();

		verify(air, times(1)).getTypeFromConfig(initConfigId);
		verify(air, times(1)).addAtomicService(argThat(asMatcher));
		verify(air, times(1)).getApplianceConfig(initConfigId);
		verify(air, times(1)).addInitialConfiguration(anyString(),
				startsWith(as.getName()), eq(initConfigPayload));
	}

	private void givenMockedAtmosphereForStartingASInDevMode() {
		baseAS = givenAtomicServiceRequestAndWorkflowAlreadyStarted(WorkflowType.development);

		as = new AtomicService();
		as.setName(baseAS.getName());
		as.setDescription(baseAS.getDescription());
		as.setDevelopment(true);
		
		when(air.getTypeFromConfig(initConfigId)).thenReturn(baseAS);
		when(
				air.addInitialConfiguration(anyString(),
						startsWith(as.getName()), eq(initConfigPayload)))
				.thenReturn(newConfigId);
	}
}
