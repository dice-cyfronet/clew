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
import static org.junit.Assert.assertNull;
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
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowStatus;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;
import pl.cyfronet.dyrealla.allocation.AddRequiredAppliancesRequest;
import pl.cyfronet.dyrealla.allocation.OperationStatus;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CloudManagerTest {

	@Test
	public void shoutGetAtomicServicesList() {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		ApplianceType type1 = new ApplianceType();
		type1.setName("type1");
		type1.setDescription("type1 description");
		type1.setHttp(true);
		type1.setIn_proxy(true);
		type1.setPublished(true);
		type1.setScalable(true);
		type1.setShared(true);
		type1.setVnc(true);

		ApplianceType type2 = new ApplianceType();
		type2.setPublished(false);
		type1.setName("type2");
		type1.setDescription("type2 description");
		type1.setHttp(false);
		type1.setIn_proxy(false);
		type1.setPublished(false);
		type1.setScalable(false);
		type1.setShared(false);
		type1.setVnc(false);

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		List<AtomicService> asList = manager.getAtomicServices();

		// then
		assertEquals(2, asList.size());

		AtomicService as1 = asList.get(0);
		AtomicService as2 = asList.get(1);

		verify(air, times(1)).getApplianceTypes();

		assertATAndAs(type1, as1);
		assertATAndAs(type2, as2);
	}

	@Test(expectedExceptions = { ApplianceTypeNotFound.class })
	public void shouldTrowExceptionWhenASIsNotFound() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		ApplianceType type1 = new ApplianceType();
		type1.setName("type1");

		ApplianceType type2 = new ApplianceType();
		type1.setName("type2");

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		manager.getInitialConfigurations("nonExisting");

		// then

	}

	@Test
	public void shouldStartNewAtomicService() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		DyReAllaManagerService atmosphere = mock(DyReAllaManagerService.class);
		manager.setAtmosphere(atmosphere);

		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		final String atomicServiceId = "asId";
		final String name = "name";
		final String contextId = "contextId";
		String username = "user";

		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);

		// when
		mockGetWorkflow(air, contextId, username);
		when(
				atmosphere
						.addRequiredAppliances(argThat(new AddRequiredAppliancesRequestMatcher(
								contextId, atomicServiceId)))).thenReturn(
				new ManagerResponseTestImpl(OperationStatus.SUCCESSFUL));

		String id = manager.startAtomicService(atomicServiceId, name,
				contextId, username);

		// then
		verify(atmosphere, times(1)).addRequiredAppliances(
				any(AddRequiredAppliancesRequest.class));

		verify(air, times(1)).getWorkflow(contextId);

		// TODO Atmosphere should return ASI instance in response.
		assertNull(id);
	}

	@Test(expectedExceptions = WorkflowNotFoundException.class)
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotExist()
			throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "contextId";

		// when
		mockGetNonExistingWorkflow(air, contextId);
		manager.startAtomicService("1", "name", contextId, "user");

		// then
	}

	@Test(expectedExceptions = WorkflowNotFoundException.class)
	public void shouldTestAddingASIThrowWorkflowNotFoundWhenWorkflowDoesNotBelongToTheUser()
			throws Exception {
		// given
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);
		manager.setAir(air);

		String contextId = "contextId";

		// when
		mockGetWorkflow(air, contextId, "otherUser");
		manager.startAtomicService("1", "name", contextId, "user");

		// then
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
	public Object[][] getWorkflowTypes() {
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

		// when

		// then

	}

	@Test
	public void shouldGetWorkflowStatus() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);
		String contextId = "contextId";
		String name = "name";
		String description = "description";
		int priority = 40;
		String username = "user";

		WorkflowDetail workflowDetail = new WorkflowDetail();
		workflowDetail.setName(name);
		workflowDetail.setDescription(description);
		workflowDetail.setPriority(priority);
		workflowDetail.setState(Status.booting);
		workflowDetail.setWorkflow_type(WorkflowType.development);
		workflowDetail.setVph_username(username);

		Vms vm1 = getVms("name1", "as1", "conf1", "sTemplate1", "1",
				Status.running);
		Vms vm2 = getVms("name2", "as1", "conf1", "sTemplate1", "2",
				Status.booting);
		Vms vm3 = getVms("name3", "as2", "conf2", "sTemplate1", "3",
				Status.stopping);

		workflowDetail.setVms(Arrays.asList(vm1, vm2, vm3));

		// when
		when(air.getWorkflow(contextId)).thenReturn(workflowDetail);
		when(air.getTypeFromConfig("conf1"))
				.thenReturn(getApplianceType("as1"));
		when(air.getTypeFromConfig("conf2"))
				.thenReturn(getApplianceType("as2"));

		WorkflowStatus status = manager.getWorkflowStatus(contextId, username);

		// then

		assertEquals(name, status.getName());
		assertEquals(2, status.getAses().size());

		AtomicServiceStatus asi1 = status.getAses().get(1);
		{
			assertEquals("as1", asi1.getId());
			assertNull(asi1.getMessage());
			assertEquals(Status.booting, asi1.getStatus());
			assertEquals(2, asi1.getInstances().size());

			assertEquals("1", asi1.getInstances().get(0).getId());
			assertEquals("name1", asi1.getInstances().get(0).getName());
			assertEquals("", asi1.getInstances().get(0).getMessage());
			assertEquals(Status.running, asi1.getInstances().get(0).getStatus());

			assertEquals("2", asi1.getInstances().get(1).getId());
			assertEquals("name2", asi1.getInstances().get(1).getName());
			assertEquals("", asi1.getInstances().get(1).getMessage());
			assertEquals(Status.booting, asi1.getInstances().get(1).getStatus());
		}

		AtomicServiceStatus asi2 = status.getAses().get(0);
		{
			assertEquals(1, asi2.getInstances().size());
			assertEquals("3", asi2.getInstances().get(0).getId());
			assertEquals("name3", asi2.getInstances().get(0).getName());
			assertEquals("", asi2.getInstances().get(0).getMessage());
			assertEquals(Status.stopping, asi2.getStatus());
			assertEquals(Status.stopping, asi2.getInstances().get(0)
					.getStatus());
		}

		verify(air, times(1)).getWorkflow(contextId);
		verify(air, times(3)).getTypeFromConfig(any(String.class));
	}

	@Test
	public void shouldGetInitialConfiguration() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String asName1 = "asName";
		String asName2 = "withoutConfigs";

		ApplianceType type1 = new ApplianceType();
		type1.setName(asName1);

		ApplianceConfiguration conf1 = new ApplianceConfiguration();
		conf1.setId("initConf1");
		conf1.setConfig_name("initConfName1");

		ApplianceConfiguration conf2 = new ApplianceConfiguration();
		conf2.setId("initConf2");
		conf2.setConfig_name("initConfName2");

		type1.setConfigurations(Arrays.asList(conf1, conf2));

		ApplianceType type2 = new ApplianceType();
		type2.setName(asName2);

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));

		List<InitialConfiguration> initConfs1 = manager
				.getInitialConfigurations(asName1);
		List<InitialConfiguration> initConfs2 = manager
				.getInitialConfigurations(asName2);

		// then

		assertEquals(2, initConfs1.size());
		assertEquals("initConf1", initConfs1.get(0).getId());
		assertEquals("initConfName1", initConfs1.get(0).getName());
		assertEquals("initConf2", initConfs1.get(1).getId());
		assertEquals("initConfName2", initConfs1.get(1).getName());

		assertEquals(0, initConfs2.size());

		verify(air, times(2)).getApplianceTypes();
	}

	@Test(expectedExceptions = ApplianceTypeNotFound.class)
	public void shouldThrownASNotFoundException() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String configId = "nonExisting";

		ApplianceType type1 = new ApplianceType();
		type1.setName("name1");
		ApplianceType type2 = new ApplianceType();
		type2.setName("name2");

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		manager.getInitialConfigurations(configId);

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

	private ApplianceType getApplianceType(String name) {
		ApplianceType at = new ApplianceType();
		at.setName(name);
		return at;
	}

	private Vms getVms(String name, String type, String confId,
			String sourceTemplate, String id, Status state) {
		Vms vm = new Vms();
		vm.setAppliance_type(type);
		vm.setConf_id(confId);
		vm.setName(name);
		vm.setSource_template(sourceTemplate);
		vm.setState(state);
		vm.setSpecs(null);
		vm.setVms_id(id);

		return vm;
	}

	private void assertATAndAs(ApplianceType at, AtomicService as) {
		assertEquals(at.getName(), as.getName());
		assertEquals(at.getDescription(), as.getDescription());
		assertEquals(at.isHttp(), as.isHttp());
		assertEquals(at.isPublished(), as.isPublished());
		assertEquals(at.isHttp() && at.isIn_proxy(), as.isHttp());
		assertEquals(at.isScalable(), as.isScalable());
		assertEquals(at.isShared(), as.isShared());
		assertEquals(at.isVnc(), as.isVnc());
	}

	/**
	 * @param air
	 * @param username
	 */
	private void mockGetWorkflow(AirClient air, String contextId,
			String username) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	/**
	 * @param air
	 * @param contextId
	 */
	private void mockGetNonExistingWorkflow(AirClient air, String contextId) {
		when(air.getWorkflow(contextId))
				.thenThrow(
						new ServerWebApplicationException(Response.status(404)
								.build()));
	}
}
