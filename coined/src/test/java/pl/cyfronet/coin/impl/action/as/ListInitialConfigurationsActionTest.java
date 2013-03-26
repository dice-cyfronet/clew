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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListInitialConfigurationsActionTest extends ActionTest {

	private List<InitialConfiguration> initialConfigurations;

	@DataProvider
	protected Object[][] getLoadPayload() {
		return new Object[][] { { true }, { false } };
	}

	@Test(dataProvider = "getLoadPayload")
	public void shouldGetInitialConfiguration(boolean loadPayload)
			throws Exception {
		givenAtomicServicesList();
		whenGetInitialConfigurations("second", loadPayload);
		thenCheck2InitialConfigurations(loadPayload);

	}

	private void givenAtomicServicesList() {
		ApplianceType type1 = new ApplianceType();
		type1.setName("first name");
		type1.setId("first");

		ApplianceType type2 = new ApplianceType();
		type2.setName("second name");
		type2.setId("second");
		
		ApplianceConfiguration config1 = new ApplianceConfiguration();
		config1.setConfig_name("config1");
		config1.setId("1");

		ApplianceConfiguration config2 = new ApplianceConfiguration();
		config2.setConfig_name("config2");
		config2.setId("2");

		type2.setConfigurations(Arrays.asList(config1, config2));

		when(air.getApplianceTypes(type1.getId(), false)).thenReturn(Arrays.asList(type1));
		when(air.getApplianceTypes(type2.getId(), false)).thenReturn(Arrays.asList(type2));
		when(air.getApplianceConfig("1")).thenReturn("payload1");
		when(air.getApplianceConfig("2")).thenReturn("payload2");
	}

	private void whenGetInitialConfigurations(String atomicServiceId, boolean loadPayload)
			throws AtomicServiceNotFoundException {
		Action<List<InitialConfiguration>> action = actionFactory
				.createListInitialConfigurationsAction(atomicServiceId, loadPayload);
		initialConfigurations = action.execute();
	}

	private void thenCheck2InitialConfigurations(boolean loadPayload) {
		thanCheckIfAirWasInvoked();
		assertNotNull(initialConfigurations);
		checkInitialConfiguration(initialConfigurations.get(0), "config1", "1",
				getPayload(loadPayload, "1"));
		checkInitialConfiguration(initialConfigurations.get(1), "config2", "2",
				getPayload(loadPayload, "2"));
	}

	private String getPayload(boolean loadPayload, String postfix) {
		return loadPayload ? "payload" + postfix : null;
	}

	private void thanCheckIfAirWasInvoked() {
		verify(air, times(1)).getApplianceTypes(anyString(), eq(false));
	}

	private void checkInitialConfiguration(InitialConfiguration initConf,
			String name, String id, String payload) {
		assertNotNull(initConf);
		assertEquals(initConf.getName(), name);
		assertEquals(initConf.getId(), id);
		assertEquals(initConf.getPayload(), payload);
	}

	@Test
	public void shouldGetEmptyInitConfList() throws Exception {
		givenAtomicServicesList();
		whenGetInitialConfigurations("first", false);
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
			whenGetInitialConfigurations("nonExisting", false);
			fail();
		} catch (AtomicServiceNotFoundException e) {
			thanCheckIfAirWasInvoked();
		}
	}

}
