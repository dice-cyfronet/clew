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
package pl.cyfronet.coin.impl.action.workflow;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.AppliancePreferences;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.mock.matcher.AddAtomicServiceMatcher;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@SuppressWarnings("unchecked")
public class StartAtomicServiceActionTest extends WorkflowActionTest {

	private String name = "asIdName";

	private String id;
	private ApplianceType baseAS;
	private ApplianceType at;

	private AddAtomicServiceMatcher asMatcher;

	private Action<Class<Void>> removeASAction;

	private String devAsId = "devAsId";

	private Float cpu = 1.2f;

	private Long disk = 123l;

	private Long memory = 321l;

	@Test
	public void shouldStartWithoutKeyAndPropsWhenProductionWorkflow() throws Exception {
		givenAtomicServiceRequestAndWorkflowAlreadyStarted(WorkflowType.portal,
				OperationStatus.SUCCESSFUL);
		whenStartAtomicService();
		thenCheckIfAtomicServiceWasStarted();
	}

	private ApplianceType givenAtomicServiceRequestAndWorkflowAlreadyStarted(
			WorkflowType workflowType, OperationStatus status) {
		givenWorkflowStarted(workflowType);
		givenAsiRequestMatcher(workflowType, status);
		ApplianceType at = givenASInAir(initConfigId, false);

		return at;
	}

	private void whenStartAtomicService() {
		whenStartAtomicService(username, keyId, cpu, disk, memory );
	}

	private void whenStartAtomicService(String username, String keyId, Float cpu, Long disk, Long memory) {
		AddAsWithKeyToWorkflow request = new AddAsWithKeyToWorkflow();
		request.setAsConfigId(initConfigId);
		request.setName(name);
		request.setKeyId(keyId);
		request.setCpu(cpu);
		request.setDisk(disk);
		request.setMemory(memory);

		Action<String> action = actionFactory.createStartAtomicServiceAction(
				username, contextId, request);
		id = action.execute();
	}

	private void thenCheckIfAtomicServiceWasStarted() {
		verify(atmosphere, times(1)).addRequiredAppliances(argThat(matcher));

		verify(air, times(1)).getWorkflow(contextId);

		// TODO Atmosphere should return ASI instance in response.
		assertNull(id);
	}

	@Test
	public void shouldStartASWithKeyAndPropsWhenDevelopmentWorkflow() throws Exception {
		givenMockedAtmosphereForStartingASInDevMode(OperationStatus.SUCCESSFUL);
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
		whenStartAtomicService("otherUser", null, null, null, null);
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

	@Test
	public void shouldThrowExceptionAndRemoveTmpASWhileAtmosphereActionFailed()
			throws Exception {
		givenAtmosphereReturnsErrorWhileStartingAtomicService();

		try {
			whenStartAtomicService();
			fail("Error should be thrown while dyrealla was not able to stop ASI");
		} catch (CloudFacadeException e) {
			thenVerifyRequestSendToAtmosphereAndTmpASRemove();
		}
	}

	private void givenAtmosphereReturnsErrorWhileStartingAtomicService() {
		givenMockedAtmosphereForStartingASInDevMode(OperationStatus.FAILED);

		removeASAction = mock(Action.class);
		when(actionFactory.createDeleteAtomicServiceFromAirAction(devAsId))
				.thenReturn(removeASAction);
	}

	private void thenVerifyRequestSendToAtmosphereAndTmpASRemove() {
		verify(removeASAction, times(1)).execute();
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

		givenMockedAtmosphereForStartingASInDevMode(OperationStatus.SUCCESSFUL);

		whenStartAtomicService();

		verify(air, times(1)).getTypeFromConfig(initConfigId);
		verify(air, times(1)).addAtomicService(argThat(asMatcher));
		verify(air, times(1)).getApplianceConfig(initConfigId);
		verify(air, times(1)).addInitialConfiguration(anyString(),
				eq("devAsId"), eq(initConfigPayload));
	}

	private void givenMockedAtmosphereForStartingASInDevMode(
			OperationStatus status) {
		baseAS = givenAtomicServiceRequestAndWorkflowAlreadyStarted(
				WorkflowType.development, status);

		at = new ApplianceType();
		at.setName(baseAS.getName());
		at.setDescription(baseAS.getDescription());
		at.setDevelopment(true);
		at.setEndpoints(baseAS.getEndpoints());
		at.setPort_mappings(baseAS.getPort_mappings());
		
		AppliancePreferences prefs = new AppliancePreferences();
		prefs.setCpu(cpu);
		prefs.setDisk(disk);
		prefs.setMemory(memory);
		at.setAppliance_preferences(prefs);
		
		asMatcher = new AddAtomicServiceMatcher(username, at, true);

		when(air.getTypeFromConfig(initConfigId)).thenReturn(baseAS);
		when(air.addAtomicService(argThat(asMatcher))).thenReturn(devAsId);
		when(
				air.addInitialConfiguration(anyString(), eq("devAsId"),
						eq(initConfigPayload))).thenReturn(newConfigId);

	}
}
