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
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
import pl.cyfronet.coin.api.exception.WorkflowNotInProductionModeException;
import pl.cyfronet.coin.impl.action.Action;

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
@SuppressWarnings("unchecked")
public class WorkflowManagementTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("workflowManagementClient")
	private WorkflowManagement workflowManagement;

	private List<Redirection> redirections;

	private String host = "host.com";

	private String contextId = "50b70f252a9524132a04cae5";

	private String asConfigId = "50b70f252a9524132a04cae6";

	private String asiId = "50b70f252a9524132a04cae7";

	private String username = "User123";

	private String atomicServiceId = "50b70f252a9524132a04cae8";
	private String asName = "asName";
	private String keyId = "50b70f252a9524132a04cae9";

	private RedirectionType redirectionType = RedirectionType.HTTP;
	private int redirectionPort = 80;
	private String redirectionName = "myRedirection";

	private String redirectionId = "50b70f252a9524132a04caf9";

	private List<Endpoint> endpoints;

	private List<Endpoint> givenEndpoints;

	private Endpoint endpoint;

	private String givenId = "id";

	private String endpointId;

	private AtomicServiceInstance asInstance;

	private AtomicServiceInstance givenAsInstance;

	private String invalidId = "invalidId";

	private String validId = "50b70f252a9524132a04cae0";

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

		Action<List<Redirection>> action = mock(Action.class);
		when(action.execute()).thenReturn(asiRedirections);

		when(
				actionFactory.createGetAsiRedirectionsAction(contextId,
						username, asiId)).thenReturn(action);
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
			assertEquals(redirection.getType() == RedirectionType.HTTP,
					i % 2 == 0);

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
		Action<List<Redirection>> action = mock(Action.class);
		when(action.execute()).thenThrow(exception);

		when(
				actionFactory.createGetAsiRedirectionsAction(contextId,
						username, asiId)).thenReturn(action);
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
		Action<Class<Void>> action = mock(Action.class);
		givenRemoveASAction(action);
	}

	private void givenRemoveASAction(Action<Class<Void>> action) {
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
		Action<Class<Void>> action = mock(Action.class);
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
		Action<Class<Void>> action = mock(Action.class);
		givenRemoveASIAction(action);
	}

	private void givenRemoveASIAction(Action<Class<Void>> action) {
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
		Action<Class<Void>> action = mock(Action.class);
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
		Action<String> action = mock(Action.class);
		when(
				actionFactory.createStartAtomicServiceAction(username,
						atomicServiceId, asName, contextId, keyId)).thenReturn(
				action);
		currentAction = action;
	}

	private void whenAddAtomicServiceWithKeyToWorkflow() {
		workflowManagement.addAtomicServiceToWorkflow(contextId,
				atomicServiceId, asName, keyId);
	}

	private void thenAtomicServiceWithKeyAdded() {
		thenActionExecuted();
	}

	// redirections

	@Test
	public void shouldAddNewRedirection() throws Exception {
		givenAddAsiRedirectionAction();
		whenAddAsiRedirectionAction();
		thenRedirectionAdded();
	}

	private void givenAddAsiRedirectionAction() {
		Action<String> action = mock(Action.class);
		when(
				actionFactory.createAddAsiRedirectionAction(username,
						contextId, asiId, redirectionName, redirectionPort,
						redirectionType)).thenReturn(action);
		currentAction = action;
	}

	private void whenAddAsiRedirectionAction() {
		workflowManagement.addRedirection(contextId, asiId, redirectionName,
				redirectionPort, redirectionType);
	}

	private void thenRedirectionAdded() {
		thenActionExecuted();
	}

	@Test
	public void shouldAddRedirectionThrowExceptionWhileWorkflowNotFoud()
			throws Exception {
		givenAddAsiRedirectionActionThrowException(new WorkflowNotFoundException());
		try {
			whenAddAsiRedirectionAction();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenAddAsiRedirectionActionThrowException(
			CloudFacadeException exception) {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(exception);
		when(
				actionFactory.createAddAsiRedirectionAction(username,
						contextId, asiId, redirectionName, redirectionPort,
						redirectionType)).thenReturn(action);
	}

	@Test
	public void shouldAddRedirectionThrowExceptionWhileWorkflowNotInDevelopment()
			throws Exception {
		givenAddAsiRedirectionActionThrowException(new WorkflowNotInDevelopmentModeException());
		try {
			whenAddAsiRedirectionAction();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown
		}
	}

	@Test
	public void shouldAddRedirectionThrowExceptionWhileAsiNotFound()
			throws Exception {
		givenAddAsiRedirectionActionThrowException(new AtomicServiceInstanceNotFoundException());
		try {
			whenAddAsiRedirectionAction();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown
		}
	}

	@Test
	public void shouldRemoveRedirection() throws Exception {
		givenAsiWithRedirection();
		whenRemoveAsiRedirection();
		thenAsiRedirectionRemoved();
	}

	private void givenAsiWithRedirection() {
		Action<Class<Void>> action = mock(Action.class);
		when(
				actionFactory.createRemoveAsiRedirectionAction(username,
						contextId, asiId, redirectionId)).thenReturn(action);
		currentAction = action;
	}

	private void whenRemoveAsiRedirection() {
		workflowManagement.deleteRedirection(contextId, asiId, redirectionId);
	}

	private void thenAsiRedirectionRemoved() {
		thenActionExecuted();
	}

	@Test
	public void shouldRemoveRedirectionThrowWorkflowNotInDevelopment()
			throws Exception {
		givenRemoveAsiRedirectionActionThrowException(new WorkflowNotInDevelopmentModeException());
		try {
			whenRemoveAsiRedirection();
			fail();
		} catch (WorkflowNotInDevelopmentModeException e) {
			// OK should be thrown.
		}
	}

	private void givenRemoveAsiRedirectionActionThrowException(
			Exception exception) {
		Action<Class<Void>> action = mock(Action.class);
		when(action.execute()).thenThrow(exception);
		when(
				actionFactory.createRemoveAsiRedirectionAction(username,
						contextId, asiId, redirectionId)).thenReturn(action);
	}

	@Test
	public void shouldRemoveRedirectionThrowWorkflowNotFound() throws Exception {
		givenRemoveAsiRedirectionActionThrowException(new WorkflowNotFoundException());
		try {
			whenRemoveAsiRedirection();
			fail();
		} catch (WorkflowNotFoundException e) {
			// OK should be thrown.
		}
	}

	@Test
	public void shouldRemoveRedirectionThrowAsiNotFound() throws Exception {
		givenRemoveAsiRedirectionActionThrowException(new AtomicServiceInstanceNotFoundException());
		try {
			whenRemoveAsiRedirection();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown.
		}
	}

	@Test
	public void shouldRemoveRedirectionThrowRedirectionNotFound()
			throws Exception {
		givenRemoveAsiRedirectionActionThrowException(new RedirectionNotFoundException());
		try {
			whenRemoveAsiRedirection();
			fail();
		} catch (RedirectionNotFoundException e) {
			// OK should be thrown.
		}
	}

	// endpoints
	@Test
	public void shouldGetAsiEndpoints() throws Exception {
		givenAsiEndpoints();
		whenGetAsiEndpoints();
		thenAsiEndpointsReceived();
	}

	private void givenAsiEndpoints() {
		Action<List<Endpoint>> action = mock(Action.class);

		givenEndpoints = Arrays.asList(getEndpoint("e1", EndpointType.REST),
				getEndpoint("e2", EndpointType.WS),
				getEndpoint("e3", EndpointType.WEBAPP));

		when(action.execute()).thenReturn(givenEndpoints);
		when(
				actionFactory.createListAsiEndpointsAction(username, contextId,
						asiId)).thenReturn(action);
	}

	private Endpoint getEndpoint(String name, EndpointType type) {
		Endpoint endpoint = new Endpoint();
		endpoint.setId(name + "Id");
		endpoint.setDescription(name + " description");
		endpoint.setDescriptor(name + " descriptor");
		endpoint.setInvocationPath("/" + name);
		endpoint.setPort(80);
		endpoint.setType(type);

		return endpoint;
	}

	private void whenGetAsiEndpoints() {
		endpoints = workflowManagement.getEndpoints(contextId, asiId);
	}

	private void thenAsiEndpointsReceived() {
		assertEquals(endpoints.size(), 3);
		assertEquals(endpoints.get(0).getId(), "e1Id");
		assertEquals(endpoints.get(1).getId(), "e2Id");
		assertEquals(endpoints.get(2).getId(), "e3Id");
	}

	@Test
	public void shouldAddNewEndpoint() throws Exception {
		givenAsiStartedInDevelopmentMode();
		whenAddNewEndpoint();
		thenEndpointAdded();
	}

	private void givenAsiStartedInDevelopmentMode() {
		endpoint = new Endpoint();
		endpoint.setDescription("description");
		endpoint.setDescriptor("descriptor");
		endpoint.setInvocationPath("/invocation_path");
		endpoint.setPort(80);
		endpoint.setType(EndpointType.REST);

		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn(givenId);

		when(
				actionFactory.createAddAsiEndpointAction(username, contextId,
						asiId, endpoint)).thenReturn(action);
	}

	private void whenAddNewEndpoint() {
		endpointId = workflowManagement.addEndpoint(contextId, asiId, endpoint);
	}

	private void thenEndpointAdded() {
		thenActionExecuted();
		assertEquals(endpointId, givenId);
	}

	@Test
	public void shouldRemoveAsiEndpoint() throws Exception {
		givenAsiWithEndpoint();
		whenRemoveAsiEndpoint();
		thenAsiRemoved();
	}

	private void givenAsiWithEndpoint() {
		endpointId = "50b70f252a9524132a04cae9";
		Action<Class<Void>> action = mock(Action.class);
		when(
				actionFactory.createRemoveAsiEndpointAction(username,
						contextId, asiId, endpointId)).thenReturn(action);
		currentAction = action;
	}

	private void whenRemoveAsiEndpoint() {
		workflowManagement.deleteEndpoint(contextId, asiId, endpointId);
	}

	private void thenAsiRemoved() {
		thenActionExecuted();
	}

	@Test
	public void shouldThrow404WhenEndpointNotFound() throws Exception {
		givenAsiWithoutEndpoint();
		try {
			whenRemoveAsiEndpoint();
			fail();
		} catch (EndpointNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenAsiWithoutEndpoint() {
		Action<Class<Void>> action = mock(Action.class);
		when(action.execute()).thenThrow(new EndpointNotFoundException());
		when(
				actionFactory.createRemoveAsiEndpointAction(username,
						contextId, asiId, endpointId)).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldGetWroflowAtomicServiceInstance() throws Exception {
		givenWorkflowWithAtomicServiceInstance();
		whenGetWorkflowAtomicServiceInstance();
		thenAtomicServiceReceived();
	}

	private void givenWorkflowWithAtomicServiceInstance() {
		givenAsInstance = new AtomicServiceInstance();
		givenAsInstance.setAtomicServiceId("asId");
		givenAsInstance.setAtomicServiceName("name");
		givenAsInstance.setConfigurationId("configurationId");
		givenAsInstance.setInstanceId(asiId);

		Action<AtomicServiceInstance> action = mock(Action.class);
		when(action.execute()).thenReturn(givenAsInstance);
		when(
				actionFactory.createGetWorkflowAtomicServiceInstanceAction(
						username, contextId, asiId)).thenReturn(action);
		currentAction = action;
	}

	private void whenGetWorkflowAtomicServiceInstance() {
		asInstance = workflowManagement.getWorkflowAtomicServiceInstance(
				contextId, asiId);
	}

	private void thenAtomicServiceReceived() {
		thenActionExecuted();
		assertEquals(asInstance.getAtomicServiceId(),
				givenAsInstance.getAtomicServiceId());
		assertEquals(asInstance.getAtomicServiceName(),
				givenAsInstance.getAtomicServiceName());
		assertEquals(asInstance.getConfigurationId(),
				givenAsInstance.getConfigurationId());
		assertEquals(asInstance.getInstanceId(),
				givenAsInstance.getInstanceId());
	}

	@Test
	public void shouldThrow400WhenIdIsNotValid() throws Exception {
		try {
			workflowManagement.stopWorkflow(invalidId);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.getWorkflow(invalidId);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
		
		try {
			workflowManagement.removeAtomicServiceFromWorkflow(invalidId, "asiId");
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement
					.removeAtomicServiceInstanceFromWorkflow(invalidId, "asiId");
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}

	@DataProvider
	protected Object[][] get2Ids() {
		return new Object[][] { { invalidId, invalidId },
				{ validId, invalidId } };
	}

	@Test(dataProvider = "get2Ids")
	public void shouldThrow400WhenIdNotValid2Ids(String id1, String id2)
			throws Exception {

		try {
			workflowManagement.getWorkflowAtomicServiceInstance(id1, id2);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.getRedirections(id1, id2);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.addRedirection(id1, id2, "name", 80,
					RedirectionType.HTTP);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.getEndpoints(id1, id2);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.addEndpoint(id1, id2, new Endpoint());
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}

	@DataProvider
	protected Object[][] get3Ids() {
		return new Object[][] { { invalidId, invalidId, invalidId },
				{ validId, invalidId, invalidId },
				{ validId, validId, invalidId }, };
	}

	@Test(dataProvider = "get3Ids")
	public void shouldThrow400WhenIdNotValid3Ids(String id1, String id2,
			String id3) throws Exception {
		try {
			workflowManagement
					.addAtomicServiceToWorkflow(id1, id2, "name", id3);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			workflowManagement.deleteRedirection(id1, id2, id3);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
		
		try {
			workflowManagement.deleteEndpoint(id1, id2, id3);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}
}
