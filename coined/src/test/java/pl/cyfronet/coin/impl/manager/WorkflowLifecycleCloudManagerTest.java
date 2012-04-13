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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.manager.atmosphere.ManagerResponseTestImpl;
import pl.cyfronet.coin.impl.manager.matcher.AddRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class WorkflowLifecycleCloudManagerTest extends AbstractCloudManagerTest {

	@Test
	public void shouldStartWorkflowWithAtomicService() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);

		WorkflowStartRequest startRequest = new WorkflowStartRequest();
		String name = "name";
		String username = "username";
		String description = "description";
		int priority = 40;
		WorkflowType workflowType = WorkflowType.workflow;
		String wId = "workflowId";

		startRequest.setName(name);
		startRequest.setDescription(description);
		startRequest.setPriority(priority);
		startRequest.setType(workflowType);
		startRequest.setAsConfigIds(Arrays.asList("id1", "id2"));

		AddRequiredAppliancesRequestMatcher matcher = new AddRequiredAppliancesRequestMatcher(
				wId, startRequest.getAsConfigIds().toArray(new String[0]));

		// when
		when(
				air.startWorkflow(name, username, description, priority,
						workflowType)).thenReturn(wId);

		when(atmosphere.addRequiredAppliances(argThat(matcher))).thenReturn(
				new ManagerResponseTestImpl(OperationStatus.SUCCESSFUL));

		String workflowId = manager.startWorkflow(startRequest, username);

		// then
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		verify(atmosphere, times(1)).addRequiredAppliances(argThat(matcher));

		assertEquals(wId, workflowId);
	}

	@Test
	public void shouldStartEmptyWorkflow() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);

		WorkflowStartRequest startRequest = new WorkflowStartRequest();
		String name = "name";
		String username = "username";
		String description = "description";
		int priority = 40;
		WorkflowType workflowType = WorkflowType.workflow;
		String wId = "workflowId";

		startRequest.setName(name);
		startRequest.setDescription(description);
		startRequest.setPriority(priority);
		startRequest.setType(workflowType);

		// when
		when(
				air.startWorkflow(name, username, description, priority,
						workflowType)).thenReturn(wId);

		String workflowId = manager.startWorkflow(startRequest, username);

		// then
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);
		verify(atmosphere, times(0)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));

		assertEquals(wId, workflowId);
	}

	@Test
	public void shouldStartWorkflowForNewUser() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);

		WorkflowStartRequest startRequest = new WorkflowStartRequest();
		String name = "name";
		String username = "username";
		String description = "description";
		int priority = 40;
		WorkflowType workflowType = WorkflowType.portal;
		String wId = "workflowId";

		startRequest.setName(name);
		startRequest.setDescription(description);
		startRequest.setPriority(priority);
		startRequest.setType(workflowType);

		// when
		when(air.getUserWorkflows(username)).thenThrow(
				new WebApplicationException(400));
		when(
				air.startWorkflow(name, username, description, priority,
						workflowType)).thenReturn(wId);

		String workflowId = manager.startWorkflow(startRequest, username);

		// then
		verify(air, times(1)).getUserWorkflows(username);
		verify(air, times(1)).startWorkflow(name, username, description,
				priority, workflowType);

		assertEquals(wId, workflowId);
	}

	@DataProvider(name = "workflowType")
	protected Object[][] getWorkflowTypes() {
		return new Object[][] { { WorkflowType.development },
				{ WorkflowType.portal } };
	}

	@SuppressWarnings("unchecked")
	@Test(dataProvider = "workflowType")
	public void shouldNotAllowStart2PortalOrDevelopmentWorkflows(
			WorkflowType type) throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		WorkflowStartRequest startRequest = new WorkflowStartRequest();
		startRequest.setType(type);
		startRequest.setName("w");

		String username = "user";

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setWorkflow_type(WorkflowType.workflow);
		w1.setState(Status.running);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setWorkflow_type(type);
		w2.setState(Status.running);

		// when
		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1),
				Arrays.asList(w1, w2));

		manager.startWorkflow(startRequest, username);
		try {
			manager.startWorkflow(startRequest, username);
			fail();
		} catch (WorkflowStartException e) {
			assertEquals(String.format("Cannot start two %s workflows", type),
					e.getMessage());
		}
	}

	@Test
	public void shouldStopWorkflow() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);

		manager.setAir(air);
		manager.setAtmosphere(atmosphere);
		String contextId = "contextId";
		String username = "user";

		// when
		mockGetWorkflow(air, contextId, username);
		manager.stopWorkflow(contextId, username);

		// then
		verify(air, times(1)).stopWorkflow(contextId);
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeRequiredAppliances(contextId);
	}

	@Test
	public void shouldTrowWorkflowNotFoundWhenStoppingNonExistingWorkflow()
			throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String nonExistingContextId = "nonExisting";
		String username = "existingUser";

		// when
		when(air.getWorkflow(nonExistingContextId)).thenThrow(
				getAirException(404));
		try {
			// workflow does not exist
			manager.stopWorkflow(nonExistingContextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(nonExistingContextId);
	}

	@Test
	public void shouldThrowWorkfloNotFoundExceptionWhileStoppingNotOwnWorkflow()
			throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String existingContextId = "id1";
		String username = "existingUser";

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setVph_username("myUser");
		w1.setName("w1");
		w1.setId("id1");
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		// when
		when(air.getWorkflow(existingContextId)).thenReturn(w1);

		try {
			// workflow belongs to othere user
			manager.stopWorkflow(existingContextId, username);
			fail();
		} catch (WorkflowNotFoundException e) {
			// shoud be thrown
		}

		// then
		verify(air, times(1)).getWorkflow(existingContextId);
	}
	
	@Test
	public void shouldGetUserWorkflows() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String username = "user";

		WorkflowDetail w1 = new WorkflowDetail();
		w1.setName("w1");
		w1.setId("id1");
		w1.setState(Status.running);
		w1.setWorkflow_type(WorkflowType.development);

		WorkflowDetail w2 = new WorkflowDetail();
		w2.setName("w2");
		w2.setId("id2");
		w2.setState(Status.running);
		w2.setWorkflow_type(WorkflowType.workflow);

		WorkflowDetail w3 = new WorkflowDetail();
		w3.setState(Status.stopped);

		// when
		when(air.getUserWorkflows(username)).thenReturn(Arrays.asList(w1, w2));
		List<WorkflowBaseInfo> infos = manager.getWorkflows(username);

		// then
		assertEquals(2, infos.size());
		assertEquals("id1", infos.get(0).getId());
		assertEquals("w1", infos.get(0).getName());
		assertEquals(WorkflowType.development, infos.get(0).getType());
		assertEquals("id2", infos.get(1).getId());
		assertEquals("w2", infos.get(1).getName());
		assertEquals(WorkflowType.workflow, infos.get(1).getType());
	}
}
