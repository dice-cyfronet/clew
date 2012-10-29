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
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.impl.action.GetAsiRedirectionsAction;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
//@formatter:off
@ContextConfiguration( locations={
		"classpath:rest-test-properties-key.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",
		"classpath:rest-test-workflow-management-client.xml",
		"classpath:META-INF/spring/rest-services.xml"} )
//@formatter:on
public class WorkflowManagementTest extends AbstractServiceTest {

	@Autowired
	@Qualifier("workflowManagementClient")
	private WorkflowManagement workflowManagement;

	private String contextId = "context_id";

	private String asiId = "atomic_service_instance_id";

	private List<Redirection> redirections;

	private String host = "host.com";

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
	public void should() throws Exception {
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
}
