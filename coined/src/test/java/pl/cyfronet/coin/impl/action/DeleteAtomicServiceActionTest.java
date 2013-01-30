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

package pl.cyfronet.coin.impl.action;

import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class DeleteAtomicServiceActionTest extends ActionTest {

	private String asName = "atName";
	
	@Test
	public void shouldDeleteAtomicService() throws Exception {
		givenAtomicServiceWith2InitialConfigurations();
		whenRemovingAtomicService();
		thenAtomicServiceAndAllInitConfsRemovedFromAir();
	}

	private void givenAtomicServiceWith2InitialConfigurations() {
		ApplianceConfiguration ac1 = new ApplianceConfiguration();
		ac1.setId("ac1");
		ApplianceConfiguration ac2 = new ApplianceConfiguration();
		ac2.setId("ac2");
		
		ApplianceType at = new ApplianceType();
		at.setName(asName);
		at.setConfigurations(Arrays.asList(ac1, ac2));
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(at));
	}

	private void whenRemovingAtomicService() {
		DeleteAtomicServiceAction action = actionFactory.createDeleteAtomicServiceAction(asName);
		action.execute();
	}

	private void thenAtomicServiceAndAllInitConfsRemovedFromAir() {
		verify(air, times(1)).getApplianceTypes();
		verify(air, times(2)).removeInitialConfiguration(startsWith("ac"));
		verify(air, times(1)).deleteAtomicService(asName);
	}
}
