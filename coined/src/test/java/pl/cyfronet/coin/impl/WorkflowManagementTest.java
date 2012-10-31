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
package pl.cyfronet.coin.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.action.RemoveASIFromWorkflowAction;
import pl.cyfronet.coin.impl.action.RemoveAtomicServiceFromWorkflowAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties-workflow.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-workflow-management-client.xml",
		"classpath:META-INF/spring/rest-services.xml"} )
//@formatter:on
public class WorkflowManagementTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("workflowManagementClient")
	private WorkflowManagement workflowManagement;
	private String contextId = "contextId";
	private String asConfigId = "asConfId";
	private String asiId = "asiId";
	private String username = "User123";

	@Test
	public void shouldRemoveASFromWorkflow() throws Exception {
		givenRemoveASFromWorkflowSuccess();
		whenRemoveASFromWorkflow();
		thenActionExecuted();
	}

	private void givenRemoveASFromWorkflowSuccess() {
		RemoveAtomicServiceFromWorkflowAction action = mock(RemoveAtomicServiceFromWorkflowAction.class);
		givenRemoveASAction(action);
	}

	private void givenRemoveASAction(
			RemoveAtomicServiceFromWorkflowAction action) {
		when(
				actionFactory.createRemoveAtomicServiceFromWorkflowAction(username, 
						contextId, asConfigId)).thenReturn(action);
		currentAction = action;
	}

	private void whenRemoveASFromWorkflow() {
		workflowManagement.removeAtomicServiceFromWorkflow(contextId,
				asConfigId);
	}

	@Test
	public void shouldThrowExceptionWhileRemovingASAndWorkflowNotFound()
			throws Exception {
		givenNotKnownWorkflow();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}

		thenActionExecuted();

	}

	private void givenNotKnownWorkflow() {
		givenRemoveASError(new WorkflowNotFoundException());
	}

	private void givenRemoveASError(CloudFacadeException exception) {
		RemoveAtomicServiceFromWorkflowAction action = mock(RemoveAtomicServiceFromWorkflowAction.class);
		when(action.execute()).thenThrow(exception);

		givenRemoveASAction(action);

	}

	@Test
	public void shouldThrowExceptionWhileRemovingASWhichIsNotFound()
			throws Exception {
		givenNotKnownASConfigId();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK - should be thrown
		}
		thenActionExecuted();
	}

	private void givenNotKnownASConfigId() {
		givenRemoveASError(new AtomicServiceNotFoundException());
	}

	@Test
	public void shouldThrowExceptionWhileRemovingASInDevelopmentMode()
			throws Exception {
		givenWorkflowInDevelopmentModeAndRemoveAS();
		try {
			whenRemoveASFromWorkflow();
			fail();
		} catch (WorkflowNotInProductionModeException e) {
			// OK - should be thrown
		}

		thenActionExecuted();
	}

	private void givenWorkflowInDevelopmentModeAndRemoveAS() {
		givenRemoveASError(new WorkflowNotInProductionModeException());
	}

	@Test
	public void shouldRemoveASIFromWorkflow() throws Exception {
		givenRemoveASIFromWorkflowSuccess();
		whenRemoveASIFromWorkflow();
		thenActionExecuted();
	}

	private void givenRemoveASIFromWorkflowSuccess() {
		RemoveASIFromWorkflowAction action = mock(RemoveASIFromWorkflowAction.class);
		givenRemoveASIAction(action);
	}

	private void givenRemoveASIAction(RemoveASIFromWorkflowAction action) {
		when(actionFactory.createRemoveASIFromWorkflowAction(username, contextId, asiId))
				.thenReturn(action);
		currentAction = action;
	}

	private void whenRemoveASIFromWorkflow() {
		workflowManagement.removeAtomicServiceInstanceFromWorkflow(contextId,
				asiId);
	}

	@Test
	public void shouldThrowExceptionWhileRemovingASIAndWorkflowNotFound()
			throws Exception {
		givenNotKnowWorkflowWhileRemovingASI();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}
		thenActionExecuted();
	}

	private void givenNotKnowWorkflowWhileRemovingASI() {
		givenRemoveASIError(new WorkflowNotFoundException());
	}

	private void givenRemoveASIError(CloudFacadeException exception) {
		RemoveASIFromWorkflowAction action = mock(RemoveASIFromWorkflowAction.class);
		when(action.execute()).thenThrow(exception);
		givenRemoveASIAction(action);
	}

	@Test
	public void shouldThrowExceptionWhileRemovingNonExistingASI()
			throws Exception {
		givenNotKnowASIWhileRemovingASI();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// Ok should be thrown
		}
		thenActionExecuted();
	}

	private void givenNotKnowASIWhileRemovingASI() {
		givenRemoveASIError(new AtomicServiceInstanceNotFoundException());
	}

	@Test
	public void shouldThrowExceptionWhileRemovingASIInProductionMode()
			throws Exception {
		givenRemovingASIInProductionMode();
		try {
			whenRemoveASIFromWorkflow();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// Ok should be thrown
		}
		thenActionExecuted();
	}

	private void givenRemovingASIInProductionMode() {
		givenRemoveASIError(new WorkflowNotInDevelopmentModeException());
	}
}