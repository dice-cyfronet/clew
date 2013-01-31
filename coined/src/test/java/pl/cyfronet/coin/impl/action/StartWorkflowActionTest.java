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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.mock.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.api.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class StartWorkflowActionTest extends WorkflowActionTest {

	private String name = "name";
	private String description = "description";
	private int priority = 40;
	private WorkflowStartRequest startRequest;
	private WorkflowType workflowType = WorkflowType.workflow;
	private String createdContextId;
	private String keyId = "myKey";

	@Test
	public void shouldStartEmptyWorkflow() throws Exception {
		givenProductionWorkflowStartRequest();
		whenStartWorkflow();
		thenWorkflowRegistered();
	}

	private void givenProductionWorkflowStartRequest() {
		workflowType = WorkflowType.workflow;
		createWorkflowStartRequest();
		mockAirStartMethod();
	}

	private void createWorkflowStartRequest() {
		startRequest = new WorkflowStartRequest();

		startRequest.setName(name);
		startRequest.setDescription(description);
		startRequest.setPriority(priority);
		startRequest.setType(workflowType);
		// always set key id and check if request sent to atmosphere is null in
		// production mode and keyId in development mode.
		startRequest.setKeyId(keyId);
	}

	private void mockAirStartMethod() {
		when(
				air.startWorkflow(name, username, description, priority,
						workflowType)).thenReturn(contextId);
	}

	private void whenStartWorkflow() throws WorkflowStartException {
		StartWorkflowAction action = actionFactory.createStartWorkflowAction(
				startRequest, username);
		createdContextId = action.execute();
	}

	private void thenWorkflowRegistered() {
		checkWorkflowRegisteredInAir(workflowType);

		verify(atmosphere, times(0)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));
	}

	private void checkWorkflowRegisteredInAir(WorkflowType workflowType) {
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		assertEquals(createdContextId, contextId);
	}

	@Test
	public void shouldStartProductionWorkflowWithAtomicService()
			throws Exception {
		givenASInAir("id1", false);
		givenASInAir("id2", false);
		givenProductionWorkflowStartRequestWithAsIds(
				OperationStatus.SUCCESSFUL, "id1", "id2");
		whenStartWorkflow();
		thenWorkflowRegisteredAndRequiredAsesStarted();
	}

	private void givenProductionWorkflowStartRequestWithAsIds(
			OperationStatus resultOperationStatus, String... ids) {
		givenProductionWorkflowStartRequest();
		startRequest.setAsConfigIds(Arrays.asList(ids));
		mockAtmosphereAddAppliancesWithResponse(resultOperationStatus);
	}

	private void mockAtmosphereAddAppliancesWithResponse(
			OperationStatus returnStatus) {
		AddRequiredAppliancesRequestMatcher matcher = getAddAppliancesMather();
		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(returnStatus));
	}

	private void thenWorkflowRegisteredAndRequiredAsesStarted() {
		checkWorkflowRegisteredInAir(workflowType);
		verify(atmosphere, times(1)).addRequiredAppliances(
				argThat(getAddAppliancesMather()));
	}

	private AddRequiredAppliancesRequestMatcher getAddAppliancesMather() {
		AddRequiredAppliancesRequestMatcher matcher = new AddRequiredAppliancesRequestMatcher(
				contextId, defaultPriority, username, workflowType,
				startRequest.getAsConfigIds().toArray(new String[0]));
		matcher.setGivenKeyId(keyId);
		return matcher;
	}

	@Test
	public void shouldStartDevelopmentWorkflowWithAtomicServices()
			throws Exception {
		givenDevelopmentWorkflowWith2ASes();
		whenStartWorkflow();
		thenWorkflowStartedAndKeyIdPassedToAtmosphere();
	}

	private void givenDevelopmentWorkflowWith2ASes() {
		workflowType = WorkflowType.development;
		createWorkflowStartRequest();
		mockAirStartMethod();
		givenASInAir("id1", false);
		givenASInAir("id2", false);
		startRequest.setAsConfigIds(Arrays.asList("id1", "id2"));
		mockAtmosphereAddAppliancesWithResponse(OperationStatus.SUCCESSFUL);
	}

	private void thenWorkflowStartedAndKeyIdPassedToAtmosphere() {
		thenWorkflowRegisteredAndRequiredAsesStarted();
	}

	@Test
	public void shouldNotStartWorkflowWithAsWhenAtmosphereOperationFailed()
			throws Exception {
		givenASInAir("id1", false);
		givenASInAir("id2", false);
		givenProductionWorkflowStartRequestWithAsIds(OperationStatus.FAILED,
				"id1", "id2");
		try {
			whenStartWorkflow();
			fail();
		} catch (WorkflowStartException e) {
			thanWorkflowNotRegisteredAndErrorReturned(e);
		}
	}

	private void thanWorkflowNotRegisteredAndErrorReturned(
			WorkflowStartException e) {
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		verify(atmosphere, times(1)).addRequiredAppliances(
				argThat(getAddAppliancesMather()));
		verify(air, times(1)).getWorkflow(any(String.class));
	}

	@Test
	public void shouldStartWorkflowForNewUser() throws Exception {
		givenPortalWorkflowStartRequestForNewUser();
		whenStartWorkflow();
		thenPortalWorkflowRegistered();
	}

	private void givenPortalWorkflowStartRequestForNewUser() {
		workflowType = WorkflowType.portal;
		createWorkflowStartRequest();
		mockAirStartMethod();

		when(air.getUserWorkflows(username)).thenThrow(
				new WebApplicationException(400));
	}

	private void thenPortalWorkflowRegistered() {
		checkWorkflowRegisteredInAir(workflowType);

		verify(air, times(1)).getUserWorkflows(username);
		verify(atmosphere, times(0)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));
	}

	@DataProvider(name = "workflowType")
	protected Object[][] getWorkflowTypes() {
		return new Object[][] { { WorkflowType.development },
				{ WorkflowType.portal } };
	}

	@Test(dataProvider = "workflowType")
	public void shouldNotAllowStart2PortalOrDevelopmentWorkflows(
			WorkflowType type) throws Exception {
		givenWorkflowStartRequestAndUserHasRunningWorkflowOfType(type);

		try {
			whenStartWorkflow();
			fail();
		} catch (WorkflowStartException e) {
			thanCannotStartTwoWorkflowsOfType(e, type);
		}
	}

	private void givenWorkflowStartRequestAndUserHasRunningWorkflowOfType(
			WorkflowType type) {
		givenProductionWorkflowStartRequest();
		startRequest.setType(type);

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setWorkflow_type(WorkflowType.workflow);
		w1.setState(Status.running);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setWorkflow_type(type);
		w2.setState(Status.running);

		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1, w2));
	}

	private void thanCannotStartTwoWorkflowsOfType(WorkflowStartException e,
			WorkflowType type) {
		assertEquals(e.getResponse().getEntity(),
				String.format("Cannot start two %s workflows", type));
		verify(air, times(1)).getUserWorkflows(username);
		verify(air, times(0)).startWorkflow(name, username, description,
				priority, workflowType);
	}
}
