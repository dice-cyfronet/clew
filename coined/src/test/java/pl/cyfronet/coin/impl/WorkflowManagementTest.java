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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.action.GetAsiRedirectionsAction;
import pl.cyfronet.coin.impl.action.RemoveASIFromWorkflowAction;
import pl.cyfronet.coin.impl.action.RemoveAtomicServiceFromWorkflowAction;
import pl.cyfronet.coin.impl.action.StartAtomicServiceAction;

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

	private List<Redirection> redirections;

	private String host = "host.com";

	private String contextId = "contextId";
	
	private String asConfigId = "asConfId";
	
	private String asiId = "asiId";
	
	private String username = "User123";
	
	private String atomicServiceId ="as";
	private String asName ="asName";
	private String keyName ="myKey";
	
	@Test(dataProvider = "getRedirectionsSize")
	public void shouldGetAsiRedirections(int nr) throws Exception {
		givenAsiRedirections(nr);
		whenGetAsiRedirections();
		thenRedirectionsReceived(nr);
	}

	@DataProvider
	public Object[][] getRedirectionsSize() {
		return new Object[][] { { 0 }, { 1 }, { 2 }, { 3 } };
	}

	private void givenAsiRedirections(int nr) {
		List<Redirection> asiRedirections = new ArrayList<Redirection>();
		for (int i = 0; i < nr; i++) {
			Redirection redirection = new Redirection();
			redirection.setFromPort(18080 + i);
			redirection.setToPort(8080 + i);
			redirection.setName("redirection" + i);
			redirection.setType(i % 2 == 0 ? RedirectionType.HTTP
					: RedirectionType.TCP);
			redirection.setHost(host);
			asiRedirections.add(redirection);
		}

		GetAsiRedirectionsAction action = mock(GetAsiRedirectionsAction.class);
		when(action.execute()).thenReturn(asiRedirections);

		when(actionFactory.createGetAsiRedirectionsAction(contextId, asiId))
				.thenReturn(action);
		currentAction = action;
	}

	private void whenGetAsiRedirections() throws Exception {
		redirections = workflowManagement.getRedirections(contextId, asiId);
	}

	private void thenRedirectionsReceived(int nr) {
		thenActionExecuted();
		assertNotNull(redirections);
		assertEquals(redirections.size(), nr);

		for (int i = 0; i < nr; i++) {
			Redirection redirection = redirections.get(i);
			assertEquals(redirection.getToPort().intValue(), 8080 + i);
			assertEquals(redirection.getFromPort().intValue(), 18080 + i);
			assertEquals(redirection.getHost(), host);
			assertEquals(redirection.getName(), "redirection" + i);
			assertEquals(redirection.getType(),
					i % 2 == 0 ? RedirectionType.HTTP : RedirectionType.TCP);
			assertEquals(redirection.isHttp(), i % 2 == 0);

		}
	}

	@Test
	public void shouldThrowExceptionWhileWorkflowNotFound() throws Exception {
		givenNotKnownContextId();
		try {
			whenGetAsiRedirections();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK - should be thrown
		}
		thenActionExecuted();
	}

	private void givenNotKnownContextId() {
		givenExceptionThrownByGetAsiRedirectionsAction(new WorkflowNotFoundException());
	}

	private void givenExceptionThrownByGetAsiRedirectionsAction(
			CloudFacadeException exception) {
		GetAsiRedirectionsAction action = mock(GetAsiRedirectionsAction.class);
		when(action.execute()).thenThrow(exception);

		when(actionFactory.createGetAsiRedirectionsAction(contextId, asiId))
				.thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldThrowExceptionWhenAsiNotFound() throws Exception {
		givenAsiNotFound();
		try {
			whenGetAsiRedirections();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK - should be thrown
		}
		thenActionExecuted();
	}

	private void givenAsiNotFound() {
		givenExceptionThrownByGetAsiRedirectionsAction(new AtomicServiceInstanceNotFoundException());
	}

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
				actionFactory.createRemoveAtomicServiceFromWorkflowAction(
						username, contextId, asConfigId)).thenReturn(action);
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
		when(
				actionFactory.createRemoveASIFromWorkflowAction(username,
						contextId, asiId)).thenReturn(action);
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

	@Test
	public void shouldAddAtomicServiceWithKey() throws Exception {
		givenMocketAddAtomicServiceToWorkflowAction();
		whenAddAtomicServiceWithKeyToWorkflow();
		thenAtomicServiceWithKeyAdded();
	}

	private void givenMocketAddAtomicServiceToWorkflowAction() {
		StartAtomicServiceAction action = mock(StartAtomicServiceAction.class);
		when(
				actionFactory.createStartAtomicServiceAction(atomicServiceId,
						asName, contextId, username, keyName)).thenReturn(
				action);
		currentAction = action;
	}

	private void whenAddAtomicServiceWithKeyToWorkflow() {
		workflowManagement.addAtomicServiceToWorkflow(contextId,
				atomicServiceId, asName, keyName);
	}

	private void thenAtomicServiceWithKeyAdded() {
		thenActionExecuted();
	}
}