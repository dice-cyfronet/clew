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
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveASIFromWorkflowActionTest extends RemoveWorkflowElementTest {

	private String asiId = "asiId";

	@Test
	public void shouldRemoveASIFromWorkflow() throws Exception {
		givenWorkflowWithASI();
		whenRemoveASIFromWorkflow();
		thenASIRemoved();
	}

	private void givenWorkflowWithASI() {
		givenWorkflowWithASIs("otherId", asiId, "yetAnotherId");
	}

	private void givenWorkflowWithASIs(String ...asiIds) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.development);

		List<Vms> vms = new ArrayList<Vms>();
		
		for (String id : asiIds) {
			vms.add(getVm(id));
		}
		
		wd.setVms(vms);
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}
	
	private Vms getVm(String vmId) {
		Vms vm = new Vms();
		vm.setVms_id(vmId);
		return vm;
	}

	private void whenRemoveASIFromWorkflow() {
		RemoveASIFromWorkflowAction action = actionFactory
				.createRemoveASIFromWorkflowAction(username, contextId, asiId);
		action.execute();
	}

	private void thenASIRemoved() {
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeAppliance(asiId);
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
	public void shouldThrownExceptionWhenWorkflowNotBelonsToTheUser() throws Exception {
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
		givenWorkflowWithASIs("otherId", "yetAnotherId");		
	}

	@Test
	public void shouldThrownExceptionWhenWorkflowInProductionMode() throws Exception {
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
}
