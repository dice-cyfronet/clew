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

package pl.cyfronet.coin.impl.manager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.manager.exception.ApplianceTypeNotFound;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AtomicServiceInitialConfigurationCloudManagerTest {

	@Test
	public void shouldGetInitialConfiguration() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String asName1 = "asName";
		String asName2 = "withoutConfigs";

		ApplianceType type1 = new ApplianceType();
		type1.setName(asName1);

		ApplianceConfiguration conf1 = new ApplianceConfiguration();
		conf1.setId("initConf1");
		conf1.setConfig_name("initConfName1");

		ApplianceConfiguration conf2 = new ApplianceConfiguration();
		conf2.setId("initConf2");
		conf2.setConfig_name("initConfName2");

		type1.setConfigurations(Arrays.asList(conf1, conf2));

		ApplianceType type2 = new ApplianceType();
		type2.setName(asName2);

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));

		List<InitialConfiguration> initConfs1 = manager
				.getInitialConfigurations(asName1);
		List<InitialConfiguration> initConfs2 = manager
				.getInitialConfigurations(asName2);

		// then

		assertEquals(2, initConfs1.size());
		assertEquals("initConf1", initConfs1.get(0).getId());
		assertEquals("initConfName1", initConfs1.get(0).getName());
		assertEquals("initConf2", initConfs1.get(1).getId());
		assertEquals("initConfName2", initConfs1.get(1).getName());

		assertEquals(0, initConfs2.size());

		verify(air, times(2)).getApplianceTypes();
	}

	@Test(expectedExceptions = ApplianceTypeNotFound.class)
	public void shouldThrownASNotFoundException() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String configId = "nonExisting";

		ApplianceType type1 = new ApplianceType();
		type1.setName("name1");
		ApplianceType type2 = new ApplianceType();
		type2.setName("name2");

		// when
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(type1, type2));
		manager.getInitialConfigurations(configId);

	}
}
