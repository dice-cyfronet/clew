/*
 * Copyright 2013 ACC CYFRONET AGH
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.GrantService;
import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantAlreadyExistException;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.grant.DeleteGrantAction;
import pl.cyfronet.coin.impl.action.grant.GetGrantAction;
import pl.cyfronet.coin.impl.action.grant.ListGrantsAction;
import pl.cyfronet.coin.impl.action.grant.UpdateGrantAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:grants/rest-test-properties.xml",
		"classpath:grants/client.xml",
		
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",		
		"classpath:META-INF/spring/rest-services.xml"
	} )
//@formatter:on
public class GrantServiceTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("grantServiceClient")
	private GrantService grantService;
	private List<String> grants;
	private List<String> givenGrants = Arrays.asList("grant1", "grant2",
			"grant3");
	private Grant grant;
	private Grant givenGrant;
	private String grantPath = "my/grant/name";
	private Action<?> action;

	@Test
	public void shouldGetGrantsNames() throws Exception {
		givenAIRWith3GrantsRegistered();
		whenGetGrantes();
		then3GranteNamesReceived();
	}

	private void givenAIRWith3GrantsRegistered() {
		givenAIRWithouldGrants(givenGrants);
	}

	private void givenAIRWithouldGrants(List<String> grants) {
		ListGrantsAction action = mock(ListGrantsAction.class);
		when(action.execute()).thenReturn(grants);
		when(actionFactory.createListGrantsAction()).thenReturn(action);
	}

	private void whenGetGrantes() {
		grants = grantService.listGrants();
	}

	private void then3GranteNamesReceived() {
		assertEquals(grants, givenGrants);
	}

	@Test
	public void shouldGetEmptyGrantsNamesList() throws Exception {
		givenAIRWithouldGrantsRegistered();
		whenGetGrantes();
		thenGrantsListIsEmpty();
	}

	private void givenAIRWithouldGrantsRegistered() {
		givenAIRWithouldGrants(new ArrayList<String>());
	}

	private void thenGrantsListIsEmpty() {
		assertEquals(grants.size(), 0);
	}

	@Test
	public void shouldGetGrant() throws Exception {
		givenExistingGrant();
		whenGetGrant();
		thenGrantReceived();

	}

	private void givenExistingGrant() {
		createGivenGrant();
		GetGrantAction action = mock(GetGrantAction.class);
		when(action.execute()).thenReturn(givenGrant);
		when(actionFactory.createGetGrantAction(grantPath)).thenReturn(action);
	}

	private void createGivenGrant() {
		givenGrant = new Grant();
		givenGrant.setDelete("delete");
		givenGrant.setGet("get");
		givenGrant.setPost("post");
		givenGrant.setPut("put");
	}

	private void whenGetGrant() {
		grant = grantService.getGrant(grantPath);
	}

	private void thenGrantReceived() {
		assertNotNull(grant);
		assertEquals(grant.getDelete(), givenGrant.getDelete());
		assertEquals(grant.getGet(), givenGrant.getGet());
		assertEquals(grant.getPost(), givenGrant.getPost());
		assertEquals(grant.getPut(), givenGrant.getPut());
	}

	@Test
	public void shouldThrow404WhileGrantNotFound() throws Exception {
		givenNonExistingGrant();
		try {
			whenGetGrant();
			fail();
		} catch (GrantNotFoundException e) {
			// Ok should be thrown
		}
	}

	private void givenNonExistingGrant() {
		GetGrantAction action = mock(GetGrantAction.class);
		when(action.execute()).thenThrow(new GrantNotFoundException());
		when(actionFactory.createGetGrantAction(grantPath)).thenReturn(action);
	}

	@Test
	public void shouldCreateNewGrant() throws Exception {
		givenEmptyGrantsList();
		whenCreateGrant();
		thenNewGrantCreated();
	}

	private void givenEmptyGrantsList() {
		createGivenGrant();
		UpdateGrantAction action = mock(UpdateGrantAction.class);
		this.action = action;
		when(
				actionFactory.createUpdateGrantAction(grantPath, givenGrant,
						false)).thenReturn(action);
	}

	private void whenCreateGrant() {
		grantService.updateGrant(grantPath, givenGrant, false);
	}

	private void thenNewGrantCreated() {
		verify(action, times(1)).execute();
	}

	@Test
	public void shouldThrow409WhileCreatingGrantWithExistingPath()
			throws Exception {
		givenGrantWithExistingPath();
		try {
			whenAddGrantWithNonUniquePath();
			fail("Updating already existing grant without override should thrown exception");
		} catch (GrantAlreadyExistException e) {
			// OK should be thrown
		}
	}

	private void givenGrantWithExistingPath() {
		createGivenGrant();
		UpdateGrantAction action = mock(UpdateGrantAction.class);
		when(action.execute()).thenThrow(new GrantAlreadyExistException());
		when(
				actionFactory.createUpdateGrantAction(grantPath, givenGrant,
						false)).thenReturn(action);
	}

	private void whenAddGrantWithNonUniquePath() {
		whenCreateGrant();
	}

	@Test
	public void shouldDeleteExistingGrant() throws Exception {
		givenExistingGrantForDelete();
		whenDeleteGrant();
		thenGrantDeleted();
	}

	private void givenExistingGrantForDelete() {
		DeleteGrantAction action = mock(DeleteGrantAction.class);
		this.action = action;
		when(actionFactory.createDeleteGrantAction(grantPath)).thenReturn(
				action);
	}

	private void whenDeleteGrant() {
		grantService.deleteGrant(grantPath);
	}

	private void thenGrantDeleted() {
		verify(action, times(1)).execute();
	}

	@Test
	public void shouldThrow404WhenDeletingNonExistingGrant() throws Exception {
		givenNonExistingGrantToDelete();
		try {
			whenDeletingNonExistingGrant();
			fail("Exception should be thrown while deleting non existing grant");
		} catch (GrantNotFoundException e) {
			// OK should be thrown
		}
	}

	private void givenNonExistingGrantToDelete() {
		DeleteGrantAction action = mock(DeleteGrantAction.class);
		when(action.execute()).thenThrow(new GrantNotFoundException());
		when(actionFactory.createDeleteGrantAction(grantPath)).thenReturn(
				action);
	}

	private void whenDeletingNonExistingGrant() {
		whenDeleteGrant();
	}
}
