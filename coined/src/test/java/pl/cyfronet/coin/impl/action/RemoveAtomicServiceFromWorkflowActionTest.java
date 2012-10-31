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

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.matcher.RemoveRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.api.allocation.RemoveRequiredAppliancesRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveAtomicServiceFromWorkflowActionTest extends ActionTest {

	private String contextId = "contextId";
	private String asConfId = "asConigId";
	private String username = "username";

	private RemoveRequiredAppliancesRequestMatcher matcher;

	@BeforeMethod
	protected void setUpMatcher() {
		matcher = new RemoveRequiredAppliancesRequestMatcher(contextId,
				asConfId);
	}

	@Test(dataProvider = "productionWorkflowTypeProvider")
	public void shouldRemoveAS(WorkflowType workflowType) throws Exception {
		givenWorkflowWithASAndRemoveASAction(workflowType);
		whenRemoveASFromWorkflow();
		thenASRemovedFromWorkflow();
	}

	@DataProvider
	private Object[][] productionWorkflowTypeProvider() {
		return new Object[][] { { WorkflowType.portal },
				{ WorkflowType.workflow } };
	}

	private void givenWorkflowWithASAndRemoveASAction(WorkflowType workflowType) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(workflowType);

		Vms as1 = getVms("otherConfId");
		Vms as2 = getVms(asConfId);
		Vms as3 = getVms("yetAnotherConfId");
		wd.setVms(Arrays.asList(as1, as2, as3));

		givenGetWorkflowAction(wd);
	}

	private void givenGetWorkflowAction(WorkflowDetail wd) {
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	private Vms getVms(String asConfId) {
		Vms as = new Vms();
		as.setConf_id(asConfId);
		return as;
	}

	private void whenRemoveASFromWorkflow() {
		RemoveAtomicServiceFromWorkflowAction action = actionFactory
				.createRemoveAtomicServiceFromWorkflowAction(username,
						contextId, asConfId);
		action.execute();
	}

	private void thenASRemovedFromWorkflow() {
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(1)).removeRequiredAppliances(argThat(matcher));
	}

	@Test
	public void shouldThrowExceptionWhenWorkflowNotFound() throws Exception {
		givenAiRWithoutWorkflow();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenAiRWithoutWorkflow() {
		when(air.getWorkflow(contextId)).thenThrow(getAirException(404));
	}

	private void thenOnlyAirActionInvoked() {
		verify(air, times(1)).getWorkflow(contextId);
		verify(atmosphere, times(0)).removeRequiredAppliances(
				any(RemoveRequiredAppliancesRequest.class));
	}

	@Test
	public void shouldThrowExceptionWhenWorkflowNotBelonsToTheUser()
			throws Exception {
		givenWorkflowNotBelongingToTheUser();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowNotBelongingToTheUser() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username("differentUser");
		wd.setWorkflow_type(WorkflowType.portal);

		givenGetWorkflowAction(wd);
	}

	@Test
	public void shouldThrowExceptionWhenWorkflowNotInProductionMode()
			throws Exception {
		givenWorkflowInDevelopmentMode();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (WorkflowNotInProductionModeException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowInDevelopmentMode() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.development);

		givenGetWorkflowAction(wd);
	}

	@Test
	public void shouldThrowExceptionWhenASNotFound() throws Exception {
		givenWorkflowWithoutAS();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK - should be thrown
		}

		thenOnlyAirActionInvoked();
	}

	private void givenWorkflowWithoutAS() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.portal);

		Vms as1 = getVms("otherConfId");
		Vms as2 = getVms("yetAnotherConfId");
		wd.setVms(Arrays.asList(as1, as2));
		
		givenGetWorkflowAction(wd);
	}
}