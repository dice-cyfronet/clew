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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.api.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.api.allocation.OperationStatus;
import pl.cyfronet.dyrealla.api.allocation.impl.ManagerResponseImpl;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveASIFromWorkflowActionTest extends RemoveWorkflowElementTest {

	private String asiId = "asiId";

	@Test
	public void shouldRemoveASIFromWorkflow() throws Exception {
		givenWorkflowWithASI();
		whenRemoveASIFromWorkflow();
		thenASIAndDevelopmentASRemovedRemoved();
	}

	private void givenWorkflowWithASI() {
		givenWorkflowWithASIs(true, "otherId", asiId, "yetAnotherId");
		givenAirResponse(OperationStatus.SUCCESSFUL);
	}

	private void givenWorkflowWithASIs(boolean development, String... asiIds) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.development);

		List<Vms> vms = new ArrayList<Vms>();
		List<ApplianceType> ats = new ArrayList<>();

		for (String id : asiIds) {
			Vms vm = getVm(id);
			vms.add(vm);

			ApplianceType at = new ApplianceType();
			at.setDevelopment(development);
			at.setId(id + "AS");
			ApplianceConfiguration ac = new ApplianceConfiguration();
			ac.setId(id + "InitConf");
			at.setConfigurations(Arrays.asList(ac));

			when(air.getTypeFromVM(id)).thenReturn(at);
			ats.add(at);
		}

		wd.setVms(vms);
		when(air.getWorkflow(contextId)).thenReturn(wd);
		when(air.getApplianceTypes()).thenReturn(ats);
	}

	private Vms getVm(String vmId) {
		Vms vm = new Vms();
		vm.setVms_id(vmId);
		return vm;
	}

	private void whenRemoveASIFromWorkflow() {
		Action<Class<Void>> action = actionFactory
				.createRemoveASIFromWorkflowAction(username, contextId, asiId);
		action.execute();
	}

	private void thenASIAndDevelopmentASRemovedRemoved() {
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeAppliance(asiId);
		verify(air, times(1)).getTypeFromVM(asiId);
		verify(air, times(1)).removeInitialConfiguration(asiId + "InitConf");
		verify(air, times(1)).deleteAtomicService(asiId + "AS");
	}

	@Test
	public void shouldThrownExceptionWhenWorkflowNotFound() throws Exception {
		givenAiRWithoutWorkflow();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	@Test
	public void shouldThrownExceptionWhenWorkflowNotBelonsToTheUser()
			throws Exception {
		givenWorkflowNotBelongingToTheUser();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowNotBelongingToTheUser() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username("otherUser");
		wd.setWorkflow_type(WorkflowType.development);

		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	@Test
	public void shouldThrownExceptionWhenASINotFound() throws Exception {
		givenWorkflowWithoutASI();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowWithoutASI() {
		givenWorkflowWithASIs(true, "otherId", "yetAnotherId");
	}

	@Test
	public void shouldThrownExceptionWhenWorkflowInProductionMode()
			throws Exception {
		givenWorkflowInProductionMode();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowInProductionMode() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.workflow);

		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	@Override
	protected void verifyElementRemovedFromAtmosphere(int times) {
		verify(atmosphere, times(times)).removeAppliance(asiId);
	}

	@Test
	public void shouldRemoveDevASOnlyWhenIsDevelopment() throws Exception {
		givenDevWorkflowWithNotIsDevelopmentAT();
		whenRemoveASIFromWorkflow();
		thenASIStoppedAndASNotRemoved();
	}

	private void givenDevWorkflowWithNotIsDevelopmentAT() {
		givenWorkflowWithASIs(false, "otherId", asiId, "yetAnotherId");
		givenAirResponse(OperationStatus.SUCCESSFUL);
	}

	private void thenASIStoppedAndASNotRemoved() {
		verify(air, times(1)).getWorkflow(contextId);
		verify(air, times(1)).getTypeFromVM(asiId);
		verify(atmosphere, times(1)).removeAppliance(asiId);
		verify(air, times(0)).removeInitialConfiguration(asiId + "InitConf");
		verify(air, times(0)).deleteAtomicService(asiId + "AS");
	}

	@Test
	public void shouldNotRemoveTmpASWhileDyreallaReturnsFailure()
			throws Exception {
		givenDyreallaReturningFailure();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (CloudFacadeException e) {
			thenTmpASNotRemoved();
		}
	}

	private void givenDyreallaReturningFailure() {
		givenWorkflowWithASI();
		givenAirResponse(OperationStatus.FAILED);
	}

	private void givenAirResponse(OperationStatus status) {
		ManagerResponse response = new ManagerResponseImpl();
		response.setOperationStatus(status);

		when(atmosphere.removeAppliance(asiId)).thenReturn(response);
	}

	private void thenTmpASNotRemoved() {
		thenASIStoppedAndASNotRemoved();
	}
}
