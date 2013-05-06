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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeMethod;

import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.api.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class ActionTest {

	protected AirClient air;

	protected DyReAllaManagerService atmosphere;

	protected ActionFactory actionFactory;

	protected Integer defaultPriority = 40;

	@BeforeMethod
	protected void setUp() {
		air = mock(AirClient.class);
		atmosphere = mock(DyReAllaManagerService.class);

		actionFactory = new ActionFactory();
		actionFactory.setAir(air);
		actionFactory.setAtmosphere(atmosphere);
		actionFactory.setDefaultPriority(defaultPriority);

		actionFactory = spy(actionFactory);

		postSetUp();
	}

	protected void postSetUp() {

	}

	protected WebApplicationException getAirException(int status) {
		return new WebApplicationException(Response.status(status).build());
	}
}
