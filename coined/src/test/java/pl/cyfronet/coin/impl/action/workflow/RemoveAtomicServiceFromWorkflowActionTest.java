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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceInUseException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.coin.impl.mock.matcher.RemoveRequiredAppliancesRequestMatcher;
import pl.cyfronet.dyrealla.api.VMSavingException;
import pl.cyfronet.dyrealla.api.allocation.RemoveRequiredAppliancesRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class RemoveAtomicServiceFromWorkflowActionTest extends
		RemoveWorkflowElementTest {

	private String asConfId = "asConigId";

	private RemoveRequiredAppliancesRequestMatcher matcher;

	@BeforeMethod
	protected void setUpMatcher() {
		matcher = new RemoveRequiredAppliancesRequestMatcher(contextId,
				asConfId);
	}

	@Test(dataProvider = "productionWorkflowTypeProvider")
	public void shouldRemoveAS(WorkflowType workflowType) throws Exception {
		givenWorkflowWithAS(workflowType);
		whenRemoveASFromWorkflow();
		thenWorkflowElementRemoved();
	}

	@DataProvider
	private Object[][] productionWorkflowTypeProvider() {
		return new Object[][] { { WorkflowType.portal },
				{ WorkflowType.workflow } };
	}

	private void givenWorkflowWithAS(WorkflowType workflowType) {
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
		as.setConfiguration(asConfId);
		return as;
	}

	private void whenRemoveASFromWorkflow() {
		Action<Class<Void>> action = actionFactory
				.createRemoveAtomicServiceFromWorkflowAction(username,
						contextId, asConfId);
		action.execute();
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

	@Override
	protected void verifyElementRemovedFromAtmosphere(int times)
			throws Exception {
		verify(atmosphere, times(times)).removeRequiredAppliances(
				argThat(matcher));
	}

	@DataProvider
	private Object[][] allWorkflowTypeProvider() {
		return new Object[][] { { WorkflowType.portal },
				{ WorkflowType.workflow }, { WorkflowType.development } };
	}

	@Test(dataProvider = "allWorkflowTypeProvider")
	public void shouldThrowExceptionWhenRemovingSavingAS(WorkflowType type)
			throws Exception {
		givenWorkflowWithSavingAS(type);
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (AtomicServiceInstanceInUseException e) {
			// OK should be thrown.
		}
	}

	private void givenWorkflowWithSavingAS(WorkflowType type) throws Exception {
		givenWorkflowWithAS(WorkflowType.portal);

		when(atmosphere.removeAppliance(anyString())).thenThrow(
				new VMSavingException());

		when(
				atmosphere
						.removeRequiredAppliances(any(RemoveRequiredAppliancesRequest.class)))
				.thenThrow(new VMSavingException());
	}
}