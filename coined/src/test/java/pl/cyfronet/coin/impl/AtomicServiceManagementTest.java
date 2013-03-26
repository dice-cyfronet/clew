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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceAlreadyExistsException;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-as-management-client.xml",
		"classpath:META-INF/spring/rest-services.xml"
	} )
//@formatter:on
@SuppressWarnings("unchecked")
public class AtomicServiceManagementTest extends AbstractServiceTest {

	@Autowired
	private ActionFactory actionFactory;

	@Autowired
	private CloudFacade asManagementClient;

	private List<AtomicService> atomicServices;

	private String atomicServiceId = "50b70f252a9524132a04cae5";

	private List<InitialConfiguration> initialConfigurations;
	private List<InitialConfiguration> receivedInitialConfigurations;

	private String icId = "initialConfigurationId";

	private String addedICId;

	private NewAtomicService atomicService;

	private String asInstanceId = "asiId";

	private String createdAsId;

	/**
	 * #1021
	 * @since 1.1.0
	 */
	private String username = "User123";

	private AtomicServiceRequest updateRequest;

	@DataProvider(name = "getAtomicServicesSizes")
	protected Object[][] getAtomicServicesSizes() {
		return new Object[][] { { 0 }, { 1 }, { 2 }, { 3 }, { 4 } };
	}

	@Test(dataProvider = "getAtomicServicesSizes")
	public void shouldGetAtomicServices(int size) throws Exception {
		givenAtomicServicesSize(size);
		whenGetAtomicServices();
		thanCheckReceivedAtomicServices(size);
	}

	private void givenAtomicServicesSize(int size) {
		List<AtomicService> ases = new ArrayList<AtomicService>();
		for (int i = 0; i < size; i++) {
			AtomicService as = new AtomicService();
			as.setName("as" + i);
			as.setDescription("description " + i);
			as.setHttp(true);
			as.setEndpoints(getEndpoints(i));

			ases.add(as);
		}

		Action<List<AtomicService>> action = mock(Action.class);
		currentAction = action;

		when(action.execute()).thenReturn(ases);
		when(actionFactory.createListAtomicServicesAction(username))
				.thenReturn(action);
	}

	private List<Endpoint> getEndpoints(int size) {
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		for (int i = 0; i < size; i++) {
			Endpoint endpoint = new Endpoint();
			endpoint.setDescription("endpoint description " + i);
			endpoint.setDescriptor("descriptor " + i);
			endpoint.setInvocationPath("/path/" + i);
			endpoint.setPort(900 + i);
			endpoint.setType(getEndpointType(i));
			endpoints.add(endpoint);
		}
		return endpoints;
	}

	private EndpointType getEndpointType(int i) {
		int nr = i % 3;
		if (nr == 0) {
			return EndpointType.WS;
		} else if (nr == 0) {
			return EndpointType.WEBAPP;
		} else {
			return EndpointType.REST;
		}
	}

	private void whenGetAtomicServices() {
		atomicServices = asManagementClient.getAtomicServices();
	}

	private void thanCheckReceivedAtomicServices(int size) {
		assertEquals(atomicServices.size(), size);
		for (int i = 0; i < size; i++) {
			checkAtomicService(atomicServices.get(i), i);
		}
		thenActionExecuted();
	}

	private void checkAtomicService(AtomicService atomicService, int nr) {
		assertEquals(atomicService.getName(), "as" + nr);
		assertEquals(atomicService.getDescription(), "description " + nr);
		assertTrue(atomicService.isHttp());
		assertFalse(atomicService.isActive());
		assertFalse(atomicService.isInProxy());
		assertFalse(atomicService.isPublished());
		assertFalse(atomicService.isScalable());
		assertFalse(atomicService.isShared());
		assertFalse(atomicService.isVnc());

		if (nr == 0) {
			checkEndpointsListEmpty(atomicService.getEndpoints());
		} else {
			checkEndpoints(atomicService.getEndpoints(), nr);
		}
	}

	private void checkEndpointsListEmpty(List<Endpoint> endpoints) {
		assertEquals(endpoints.size(), 0);
	}

	private void checkEndpoints(List<Endpoint> endpoints, int size) {
		assertEquals(endpoints.size(), size);
		for (int i = 0; i < size; i++) {
			checkEndpoint(endpoints.get(i), i);
		}
	}

	private void checkEndpoint(Endpoint endpoint, int nr) {
		assertEquals(endpoint.getDescription(), "endpoint description " + nr);
		assertEquals(endpoint.getDescriptor(), "descriptor " + nr);
		assertEquals(endpoint.getInvocationPath(), "/path/" + nr);
		assertEquals(endpoint.getPort(), new Integer(900 + nr));
		assertEquals(endpoint.getType(), getEndpointType(nr));
	}

	@Test
	public void shouldGetInitialConfiguration() throws Exception {
		givenInitialConfigurationForSelectedAS();
		whenGetInitialConfiguration();
		thenInitialConfigurationReceived();

	}

	private void givenInitialConfigurationForSelectedAS() {
		Action<List<InitialConfiguration>> action = mock(Action.class);

		InitialConfiguration ic1 = getInitialConfiguration(1);
		InitialConfiguration ic2 = getInitialConfiguration(1);

		initialConfigurations = Arrays.asList(ic1, ic2);

		when(action.execute()).thenReturn(initialConfigurations);
		when(
				actionFactory.createListInitialConfigurationsAction(
						atomicServiceId, false)).thenReturn(action);
		currentAction = action;
	}

	private InitialConfiguration getInitialConfiguration(int nr) {
		InitialConfiguration ic1 = new InitialConfiguration();
		ic1.setId("ic" + nr);
		ic1.setName("ic" + nr);
		ic1.setPayload("<ic" + nr + " />");

		return ic1;
	}

	private void whenGetInitialConfiguration() {
		receivedInitialConfigurations = asManagementClient
				.getInitialConfigurations(atomicServiceId, false);
	}

	private void thenInitialConfigurationReceived() {
		thenActionExecuted();
		assertEquals(receivedInitialConfigurations.size(), 2);
		assertIcEquals(receivedInitialConfigurations.get(0),
				initialConfigurations.get(0));
		assertIcEquals(receivedInitialConfigurations.get(1),
				initialConfigurations.get(1));
	}

	private void assertIcEquals(InitialConfiguration actual,
			InitialConfiguration expected) {
		assertEquals(actual.getId(), expected.getId());
		assertEquals(actual.getName(), expected.getName());
		assertEquals(actual.getPayload(), expected.getPayload());
	}

	@Test
	public void shouldThrowExceptionWhileGettingICForNonExistingAS()
			throws Exception {
		givenActionThrowingAtomicServiceNotFoundException();
		try {
			whenGetInitialConfiguration();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK should be thrown
		}
		thenActionExecuted();

	}

	private void givenActionThrowingAtomicServiceNotFoundException() {
		Action<List<InitialConfiguration>> action = mock(Action.class);

		when(action.execute()).thenThrow(new AtomicServiceNotFoundException());
		when(
				actionFactory.createListInitialConfigurationsAction(
						atomicServiceId, false)).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldAddNewInitialConfiguration() throws Exception {
		givenActionAbleToSuccessfullyAddInitialConfiguration();
		whenAddInitialConfiguration();
		thenInitialConfigurationAdded();
	}

	private void givenActionAbleToSuccessfullyAddInitialConfiguration() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn(icId);

		initialConfigurations = Arrays.asList(getInitialConfiguration(1));

		when(
				actionFactory.createAddInitialConfiguration(atomicServiceId,
						initialConfigurations.get(0))).thenReturn(action);
		currentAction = action;
	}

	private void whenAddInitialConfiguration() {
		addedICId = asManagementClient.addInitialConfiguration(atomicServiceId,
				initialConfigurations.get(0));
	}

	private void thenInitialConfigurationAdded() {
		thenActionExecuted();
		assertEquals(addedICId, icId);
	}

	@Test
	public void shouldThrowExceptionWhileAddingInitConfForNonExistingAS()
			throws Exception {
		givenActionWhichDoesNotKnowGivenAS();
		try {
			whenAddInitialConfiguration();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// Ok should be thrown
		}
		thenActionExecuted();
	}

	private void givenActionWhichDoesNotKnowGivenAS() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(new AtomicServiceNotFoundException());

		initialConfigurations = Arrays.asList(getInitialConfiguration(1));

		when(
				actionFactory.createAddInitialConfiguration(atomicServiceId,
						initialConfigurations.get(0))).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldThrowExceptionWhenInitConfNameIsNotUnique()
			throws Exception {
		givenAirWithConfigurationNameWeWantToAdd();
		try {
			whenAddInitialConfiguration();
			fail();
		} catch (InitialConfigurationAlreadyExistException e) {
			// Ok - should be thrown
		}

		thenActionExecuted();

	}

	private void givenAirWithConfigurationNameWeWantToAdd() {
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(
				new InitialConfigurationAlreadyExistException());

		initialConfigurations = Arrays.asList(getInitialConfiguration(1));

		when(
				actionFactory.createAddInitialConfiguration(atomicServiceId,
						initialConfigurations.get(0))).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldAddNewAtomicService() throws Exception {
		givenMocketAddAtomicServiceAction();
		whenAddNewAtomicService();
		thenAtomicServiceAdded();
	}

	private void givenMocketAddAtomicServiceAction() {
		createNewAs();

		Action<String> action = mock(Action.class);
		when(action.execute()).thenReturn("newASId");

		when(
				actionFactory.createCreateAtomicServiceAction(username,
						atomicService)).thenReturn(action);
		currentAction = action;
	}

	private void createNewAs() {
		atomicService = new NewAtomicService();
		atomicService.setSourceAsiId(asInstanceId);
		atomicService.setName("New as name");
		atomicService.setDescription("New as description");
	}

	private void whenAddNewAtomicService() {
		createdAsId = asManagementClient.createAtomicService(atomicService);
	}

	private void thenAtomicServiceAdded() {
		thenActionExecuted();
		assertEquals(createdAsId, "newASId");
	}

	@Test
	public void shouldThrowExceptionWhileTryingToCreateAsFromNonExistingAsi()
			throws Exception {
		givenActionWhichDoesNotKnowAsi();
		try {
			whenAddNewAtomicService();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// Ok - should be thrown
		}

		thenActionExecuted();

	}

	private void givenActionWhichDoesNotKnowAsi() {
		createNewAs();
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(
				new AtomicServiceInstanceNotFoundException());

		when(
				actionFactory.createCreateAtomicServiceAction(username,
						atomicService)).thenReturn(action);
		currentAction = action;
	}

	@Test
	public void shouldThrowExceptionWhileTryingToAddASWithNotUniqueName()
			throws Exception {
		givenActionWhichReceivesASNonUniqueException();
		try {
			whenAddNewAtomicService();
			fail();
		} catch (AtomicServiceAlreadyExistsException e) {
			// Ok - should be thrown
		}

		thenActionExecuted();
	}

	private void givenActionWhichReceivesASNonUniqueException() {
		createNewAs();
		Action<String> action = mock(Action.class);
		when(action.execute()).thenThrow(
				new AtomicServiceAlreadyExistsException());

		when(
				actionFactory.createCreateAtomicServiceAction(username,
						atomicService)).thenReturn(action);
		currentAction = action;
	}

	// delete AS

	@DataProvider
	protected Object[][] deleteASRoleProvider() {
		// admin = true, not admin = false
		return new Object[][] { { true }, { false } };
	}

	@Test(dataProvider = "deleteASRoleProvider")
	public void shouldDeleteAtomicService(boolean admin) throws Exception {
		givenAtomicServiceDeletedAction(admin);
		whenDeleteAtomicService();
		thenAtomicServiceRemoved();
	}

	private void givenAtomicServiceDeletedAction(boolean admin) {
		when(
				authenticationHandler.hasRole(eq("admin"), anyString(),
						anyString())).thenReturn(admin);
		Action<Class<Void>> action = mock(Action.class);
		when(
				actionFactory.createDeleteAtomicServiceAction(username,
						atomicServiceId, admin)).thenReturn(action);
		currentAction = action;
	}

	private void whenDeleteAtomicService() {
		asManagementClient.deleteAtomicService(atomicServiceId);
	}

	private void thenAtomicServiceRemoved() {
		thenActionExecuted();
	}

	@Test
	public void shouldThrow404WhenASNotFound() throws Exception {
		givenDeleteActionThrows(new AtomicServiceNotFoundException());
		try {
			whenDeleteAtomicService();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// Ok should be thrown
		}
	}

	@Test
	public void shouldThrow406WhenASInUse() throws Exception {
		givenDeleteActionThrows(new NotAcceptableException());
		try {
			whenDeleteAtomicService();
			fail();
		} catch (NotAcceptableException e) {
			// Ok should be thrown
		}
	}

	private void givenDeleteActionThrows(CloudFacadeException exception) {
		Action<Class<Void>> action = mock(Action.class);
		when(action.execute()).thenThrow(exception);
		when(
				actionFactory.createDeleteAtomicServiceAction(username,
						atomicServiceId, false)).thenReturn(action);
	}

	@Test
	public void shouldThrow403WhenDeletingNotOwnedAS() throws Exception {
		givenDeleteActionThrows(new NotAllowedException());
		try {
			whenDeleteAtomicService();
			fail();
		} catch (NotAllowedException e) {
			// Ok should be thrown
		}
	}

	@Test
	public void shouldThrow406WhenDeletingASWithRunningASI() throws Exception {
		givenDeleteActionThrows(new NotAcceptableException());
		try {
			whenDeleteAtomicService();
			fail();
		} catch (NotAcceptableException e) {
			// Ok should be thrown
		}
	}

	@Test
	public void shouldUpdateAtomicService() throws Exception {
		givenASUpdateAction();
		whenUpdateAtomicService();
		thenASUpdated();
	}

	private void givenASUpdateAction() {
		Action<Class<Void>> action = mock(Action.class);

		updateRequest = new AtomicServiceRequest();
		updateRequest.setName("name");
		updateRequest.setDescription("description");
		updateRequest.setProxyConfigurationName("proxy/name");
		updateRequest.setScalable(true);
		updateRequest.setPublished(false);

		when(
				actionFactory.createUpdateAtomicServiceAction(username,
						atomicServiceId, updateRequest, false)).thenReturn(
				action);
		currentAction = action;
	}

	private void whenUpdateAtomicService() {
		asManagementClient.updateAtomicService(atomicServiceId, updateRequest);
	}

	private void thenASUpdated() {
		thenActionExecuted();
	}

	@Test
	public void shouldThrow400WhenASIdNotValid() throws Exception {
		String wrongASId = "wrongAsId";
		try {
			asManagementClient.getInitialConfigurations(wrongASId, false);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			asManagementClient.addInitialConfiguration(wrongASId,
					getInitialConfiguration(1));
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			asManagementClient.getEndpointDescriptor(wrongASId, "80", "path");
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			asManagementClient.getInvocationPathInfo(wrongASId, "80", "path");
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			asManagementClient.deleteAtomicService(wrongASId);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}

		try {
			asManagementClient.updateAtomicService(wrongASId,
					new AtomicServiceRequest());
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}
}
