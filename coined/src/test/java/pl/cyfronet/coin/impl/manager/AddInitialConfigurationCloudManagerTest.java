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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;
import pl.cyfronet.coin.impl.air.client.AirClient;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddInitialConfigurationCloudManagerTest {

	@Test
	public void shouldAddInitialConfiguration() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String atomicServiceId = "asId";
		String configName = "uniqueConfigName";
		String configPayload = "<init />";

		String configId = "configId";

		InitialConfiguration initialConfiguration = new InitialConfiguration();
		initialConfiguration.setName(configName);
		initialConfiguration.setPayload(configPayload);

		// when
		when(
				air.addInitialConfiguration(configName, atomicServiceId,
						configPayload)).thenReturn(configId);

		String addedConfigId = manager.addInitialConfiguration(atomicServiceId,
				initialConfiguration);

		// then
		verify(air, times(1)).addInitialConfiguration(configName,
				atomicServiceId, configPayload);
		assertEquals(configId, addedConfigId);
	}

	@Test
	public void shouldThrowAtomicServiceNotFoundException() throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String atomicServiceId = "nonExisitngAsId";
		String configName = "uniqueConfigName";
		String configPayload = "<init />";

		InitialConfiguration initialConfiguration = new InitialConfiguration();
		initialConfiguration.setName(configName);
		initialConfiguration.setPayload(configPayload);

		String exceptionMessage = String.format(
				"Appliance Type with name %s was not found in AIR.",
				atomicServiceId);
		int errorStatus = 400;

		ServerWebApplicationException exception = new ServerWebApplicationException(
				Response.status(errorStatus).entity(exceptionMessage).build());

		// when
		when(
				air.addInitialConfiguration(configName, atomicServiceId,
						configPayload)).thenThrow(exception);
		try {
			manager.addInitialConfiguration(atomicServiceId,
					initialConfiguration);
			fail();
		} catch (AtomicServiceNotFoundException e) {
			//ok
		}

		// then
		verify(air, times(1)).addInitialConfiguration(configName,
				atomicServiceId, configPayload);
	}

	@Test
	public void shouldThrowConfigurationAlreadyExistException()
			throws Exception {
		// given
		CloudManagerImpl manager = new CloudManagerImpl();
		AirClient air = mock(AirClient.class);

		manager.setAir(air);

		String atomicServiceId = "nonExisitngAsId";
		String configName = "existingConfigName";
		String configPayload = "<init />";

		InitialConfiguration initialConfiguration = new InitialConfiguration();
		initialConfiguration.setName(configName);
		initialConfiguration.setPayload(configPayload);

		String exceptionMessage = String.format(
				"Uploading duplicated configuration for the same name %s is not allowed.",
				configName);
		int errorStatus = 400;

		ServerWebApplicationException exception = new ServerWebApplicationException(
				Response.status(errorStatus).entity(exceptionMessage).build());

		// when
		when(
				air.addInitialConfiguration(configName, atomicServiceId,
						configPayload)).thenThrow(exception);
		try {
			manager.addInitialConfiguration(atomicServiceId,
					initialConfiguration);
			fail();
		} catch (InitialConfigurationAlreadyExistException e) {
			//ok
		}

		// then
		verify(air, times(1)).addInitialConfiguration(configName,
				atomicServiceId, configPayload);
	}
}
