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
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddInitialConfigurationActionTest extends ActionTest {

	private InitialConfiguration initialConfiguration;
	private String addedConfigId;
	private String atomicServiceId;
	private String configId;

	@Test
	public void shouldAddNewInitialConfiguration() throws Exception {
		givenNewInitialConfiguration();
		whenAddInitialConfiguration();
		thenInitialConfigurationAdded();

	}

	private void givenNewInitialConfiguration() {
		atomicServiceId = "asId";
		configId = "configId";
		createInitialConfiguration();

		when(
				air.addInitialConfiguration(initialConfiguration.getName(),
						atomicServiceId, initialConfiguration.getPayload()))
				.thenReturn(configId);
	}

	private void createInitialConfiguration() {
		initialConfiguration = new InitialConfiguration();
		initialConfiguration.setName("configName");
		initialConfiguration.setPayload("<init/>");
	}

	private void whenAddInitialConfiguration() {
		AddInitialConfigurationAction action = actionFactory
				.createAddInitialConfiguration(atomicServiceId,
						initialConfiguration);
		addedConfigId = action.execute();
	}

	private void thenInitialConfigurationAdded() {
		verify(air, times(1)).addInitialConfiguration(
				initialConfiguration.getName(), atomicServiceId,
				initialConfiguration.getPayload());
		assertEquals(configId, addedConfigId);
	}

	@Test
	public void shouldThrowAtomicServiceNotFoundException() throws Exception {
		givenNonExistingAtomicServiceAndInitialConfiguration();
		try {
			whenAddInitialConfiguration();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			thenCheckAirRequest();
		}
	}

	private void givenNonExistingAtomicServiceAndInitialConfiguration() {
		atomicServiceId = "nonExisitngAsId";
		createInitialConfiguration();

		String exceptionMessage = String.format(
				"Appliance Type with name %s was not found in AIR.",
				atomicServiceId);
		int errorStatus = 400;

		ServerWebApplicationException exception = new ServerWebApplicationException(
				Response.status(errorStatus).entity(exceptionMessage).build());

		when(
				air.addInitialConfiguration(initialConfiguration.getName(),
						atomicServiceId, initialConfiguration.getPayload()))
				.thenThrow(exception);
	}

	private void thenCheckAirRequest() {
		verify(air, times(1)).addInitialConfiguration(
				initialConfiguration.getName(), atomicServiceId,
				initialConfiguration.getPayload());
	}

	@Test
	public void shouldThrowConfigurationAlreadyExistException()
			throws Exception {
		givenAirWithExistingConfigAndNewInitConfWithNotUniqueName();
		try {
			whenAddInitialConfiguration();
			fail();
		} catch (InitialConfigurationAlreadyExistException e) {
			thenCheckAirRequest();
		}
	}

	private void givenAirWithExistingConfigAndNewInitConfWithNotUniqueName() {
		atomicServiceId = "asId";
		createInitialConfiguration();

		String exceptionMessage = String
				.format("Uploading duplicated configuration for the same name %s is not allowed.",
						initialConfiguration.getName());
		int errorStatus = 400;

		ServerWebApplicationException exception = new ServerWebApplicationException(
				Response.status(errorStatus).entity(exceptionMessage).build());

		when(
				air.addInitialConfiguration(initialConfiguration.getName(),
						atomicServiceId, initialConfiguration.getPayload()))
				.thenThrow(exception);

	}
}
