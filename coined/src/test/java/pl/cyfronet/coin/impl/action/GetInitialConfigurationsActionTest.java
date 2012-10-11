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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetInitialConfigurationsActionTest extends ActionTest {

	private List<InitialConfiguration> initialConfigurations;
	private List<ApplianceType> applianceTypes;

	@Test
	public void shouldGetInitialConfiguration() throws Exception {
		givenAtomicServicesList();
		whenGetInitialConfigurations("second");
		thenCheck2InitialConfigurations();

	}

	private void givenAtomicServicesList() {
		ApplianceType type1 = new ApplianceType();
		type1.setName("first");

		ApplianceType type2 = new ApplianceType();
		type2.setName("second");

		ApplianceConfiguration config1 = new ApplianceConfiguration();
		config1.setConfig_name("config1");
		config1.setId("1");

		ApplianceConfiguration config2 = new ApplianceConfiguration();
		config2.setConfig_name("config2");
		config2.setId("2");

		type2.setConfigurations(Arrays.asList(config1, config2));

		applianceTypes = Arrays.asList(type1, type2);
	}

	private void whenGetInitialConfigurations(String atomicServiceId)
			throws AtomicServiceNotFoundException {
		when(air.getApplianceTypes()).thenReturn(applianceTypes);
		AirAction<List<InitialConfiguration>> action = actionFactory
				.createGetInitialConfigurationsAction(atomicServiceId);
		initialConfigurations = action.execute();
	}

	private void thenCheck2InitialConfigurations() {
		thanCheckIfAirWasInvoked();
		assertNotNull(initialConfigurations);
		checkInitialConfiguration(initialConfigurations.get(0), "config1", "1");
		checkInitialConfiguration(initialConfigurations.get(1), "config2", "2");		
	}

	private void thanCheckIfAirWasInvoked() {
		verify(air, times(1)).getApplianceTypes();
	}
	
	private void checkInitialConfiguration(InitialConfiguration initConf,
			String name, String id) {
		assertNotNull(initConf);
		assertEquals(initConf.getName(), name);
		assertEquals(initConf.getId(), id);
	}

	@Test
	public void shouldGetEmptyInitConfList() throws Exception {
		givenAtomicServicesList();
		whenGetInitialConfigurations("first");
		thenCheckEmptyInitialConfigurationList();

	}
	
	private void thenCheckEmptyInitialConfigurationList() {
		thanCheckIfAirWasInvoked();
		assertNotNull(initialConfigurations);
		assertEquals(initialConfigurations.size(), 0);
	}

	@Test
	public void shouldTrowExceptionWhileGettingInitConfForNonExistingAS()
			throws Exception {
		givenAtomicServicesList();
		try {
			whenGetInitialConfigurations("nonExisting");
			fail();
		} catch (AtomicServiceNotFoundException e) {
			thanCheckIfAirWasInvoked();
		}
	}

}
