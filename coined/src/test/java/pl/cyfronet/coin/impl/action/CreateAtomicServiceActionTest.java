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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.impl.mock.matcher.AddAtomicServiceMatcher;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CreateAtomicServiceActionTest extends ActionTest {

	private AtomicService atomicService;
	private String instanceId = "instanceId";	
	private AddAtomicServiceMatcher matcher;
	private String asId = "asAirId";
	private String createdAsId;
	private CreateAtomicServiceAction action;

	@Test
	public void shouldCreateNewAtomicService() throws Exception {
		givenAtomicServiceMetadata();
		whenCreateNewAtomicService();
		thenAtomicServiceCreated();
	}

	private void givenAtomicServiceMetadata() {
		String asName = "name";
		String asDescription = "description";

		atomicService = new AtomicService();
		atomicService.setName(asName);
		atomicService.setDescription(asDescription);
		atomicService.setHttp(true);
	}

	private void whenCreateNewAtomicService() throws Exception {

		matcher = new AddAtomicServiceMatcher(atomicService);
		when(air.addAtomicService(argThat(matcher))).thenReturn(asId);
		when(
				atmosphere.createTemplate(instanceId, atomicService.getName(),
						cloudSiteId, asId)).thenReturn("1");
		action = actionFactory
				.createCreateAtomicServiceAction(instanceId, atomicService);
		createdAsId = action.execute();
	}

	private void thenAtomicServiceCreated() throws Exception {
		verify(air, times(1)).addAtomicService(argThat(matcher));
		verify(atmosphere, times(1)).createTemplate(instanceId,
				atomicService.getName(), cloudSiteId, asId);
		assertEquals(atomicService.getName(), createdAsId);
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
		
		verify(air, times(1)).deleteAtomicService(asId);
	}
}
