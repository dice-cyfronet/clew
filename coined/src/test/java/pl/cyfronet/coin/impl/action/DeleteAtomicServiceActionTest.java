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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class DeleteAtomicServiceActionTest extends ActionTest {

	private String asName = "atName";

	@Test
	public void shouldDeleteAtomicService() throws Exception {
		givenAtomicServiceWith2InitialConfigurations(asName);
		whenRemovingAtomicService();
		thenAtomicServiceAndAllInitConfsRemovedFromAir();
	}

	private void givenAtomicServiceWith2InitialConfigurations(String... asIds) {
		List<ApplianceType> ats = new ArrayList<>();
		for (String asId : asIds) {
			ApplianceConfiguration ac1 = new ApplianceConfiguration();
			ac1.setId("ac1" + asId);
			ApplianceConfiguration ac2 = new ApplianceConfiguration();
			ac2.setId("ac2" + asId);

			ApplianceType at = new ApplianceType();
			at.setId(asId);
			at.setConfigurations(Arrays.asList(ac1, ac2));
			ats.add(at);
		}
		when(air.getApplianceTypes()).thenReturn(ats);
	}

	private void whenRemovingAtomicService() {
		DeleteAtomicServiceAction action = actionFactory
				.createDeleteAtomicServiceAction(asName);
		action.execute();
	}

	private void thenAtomicServiceAndAllInitConfsRemovedFromAir() {
		verify(air, times(1)).getApplianceTypes();
		verify(air, times(2)).removeInitialConfiguration(startsWith("ac"));
		verify(air, times(1)).deleteAtomicService(asName);
	}

	@Test
	public void shouldDelete2AtomicServices() throws Exception {
		given2ASesToRemove();
		whenRemoving2Ases();
		thenBothASesAndTheirInitConfsAreRemovedFromAir();
	}

	private void given2ASesToRemove() {		
		givenAtomicServiceWith2InitialConfigurations(asName + "1", asName + "2");		
	}

	private void whenRemoving2Ases() {
		DeleteAtomicServiceAction action = new DeleteAtomicServiceAction(air,
				Arrays.asList(asName + "1", asName + "2"));
		action.execute();
	}

	private void thenBothASesAndTheirInitConfsAreRemovedFromAir() {
		verify(air, times(2)).getApplianceTypes();
		verify(air, times(4)).removeInitialConfiguration(startsWith("ac"));
		verify(air, times(2)).deleteAtomicService(startsWith(asName));		
	}
}
