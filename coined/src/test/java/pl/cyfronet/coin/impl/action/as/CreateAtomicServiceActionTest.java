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
package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceAlreadyExistsException;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.mock.matcher.AddAtomicServiceMatcher;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceActionTest extends ActionTest {

	private NewAtomicService newAtomicService;
	private ApplianceType atomicService;
	private ApplianceType sourceAtomicService;
	private String instanceId = "instanceId";
	private AddAtomicServiceMatcher matcher;
	private String asId = "asAirId";
	private String createdAsId;
	private Action<String> action;

	/**
	 * #1021
	 * @since 1.1.0
	 */
	private String username = "user123";

	@Test
	public void shouldCreateNewAtomicService() throws Exception {
		givenAtomicServiceMetadata();
		whenCreateNewAtomicService();
		thenAtomicServiceCreated();
	}

	private void givenAtomicServiceMetadata() throws Exception {
		createASObjects();

		when(air.addAtomicService(argThat(matcher))).thenReturn(asId);
		when(
				atmosphere.createTemplate(instanceId,
						newAtomicService.getName(), cloudSiteId, asId))
				.thenReturn("1");
	}

	private void createASObjects() {
		String asName = "name";
		String asDescription = "description";

		newAtomicService = new NewAtomicService();
		newAtomicService.setSourceAsiId(instanceId);
		newAtomicService.setName(asName);
		newAtomicService.setDescription(asDescription);
		newAtomicService.setProxyConfigurationName("proxy/configuration/name");
		newAtomicService.setPublished(true);
		newAtomicService.setShared(null);
		newAtomicService.setScalable(true);
		
		sourceAtomicService = new ApplianceType();
		sourceAtomicService.setName("sourceASName");
		sourceAtomicService.setScalable(false);
		sourceAtomicService.setId("sourceAsId");

		atomicService = new ApplianceType();
		atomicService.setName(newAtomicService.getName());
		atomicService.setDescription(newAtomicService.getDescription());
		atomicService.setProxy_conf_name(newAtomicService.getProxyConfigurationName());
		atomicService.setPublished(true);
		atomicService.setShared(false);
		atomicService.setScalable(true);

		matcher = new AddAtomicServiceMatcher(username, atomicService);
		matcher.setCreatingNewAS(true);

		when(air.getTypeFromVM(instanceId)).thenReturn(sourceAtomicService);
	}

	private void whenCreateNewAtomicService() throws Exception {
		action = actionFactory.createCreateAtomicServiceAction(username,
				newAtomicService);
		createdAsId = action.execute();
	}

	private void thenAtomicServiceCreated() throws Exception {
		verify(air, times(1)).addAtomicService(argThat(matcher));
		verify(atmosphere, times(1)).createTemplate(instanceId,
				atomicService.getName(), cloudSiteId, asId);
		assertEquals(createdAsId, asId);
	}

	@Test
	public void shouldCreateAtomicServiceAndRollback() throws Exception {
		givenAtomicServiceMetadata();
		whenCreateNewAtomicServiceAndRollback();
		thenAtomicServiceCreatedAndRemoved();
	}

	private void whenCreateNewAtomicServiceAndRollback() throws Exception {
		whenCreateNewAtomicService();
		action.rollback();
	}

	private void thenAtomicServiceCreatedAndRemoved() throws Exception {
		thenAtomicServiceCreated();

		verify(air, times(1)).deleteAtomicService(asId, true);
	}

	@Test
	public void shouldThrown404WhenASINotFound() throws Exception {
		givenNonExistingASI();
		try {
			whenCreateNewAtomicService();
			fail();
		} catch (AtomicServiceInstanceNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenNonExistingASI() {
		when(air.getTypeFromVM(instanceId)).thenThrow(
				getAirException(400));
	}

	@Test
	public void shouldThrown302WhenASAlreadyExist() throws Exception {
		givenAlreadyExistingAS();
		try {
			whenCreateNewAtomicService();
		} catch (AtomicServiceAlreadyExistsException e) {
			// OK should be thrown.
		}
	}

	private void givenAlreadyExistingAS() {
		createASObjects();

		when(air.addAtomicService(argThat(matcher))).thenThrow(
				getAirException(302));
	}
}
