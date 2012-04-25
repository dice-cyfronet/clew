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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.BeforeMethod;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class AbstractCloudManagerTest {

	protected CloudManagerImpl manager = new CloudManagerImpl();
	protected AirClient air;
	protected DyReAllaManagerService atmosphere;
	
	protected final String contextId = "contextId";
	protected final String username = "user";
	
	@BeforeMethod
	protected void setUp() {
		air = mock(AirClient.class);
		manager.setAir(air);
		
		atmosphere = mock(DyReAllaManagerService.class);
		manager.setAtmosphere(atmosphere);
	}
	
	protected ApplianceType getApplianceType(String name) {
		ApplianceType at = new ApplianceType();
		at.setName(name);
		return at;
	}

	protected Vms getVms(String name, String type, String confId,
			String sourceTemplate, String id, Status state) {
		Vms vm = new Vms();
		vm.setAppliance_type(type);
		vm.setConf_id(confId);
		vm.setName(name);
		vm.setSource_template(sourceTemplate);
		vm.setState(state);
		vm.setSpecs(null);
		vm.setVms_id(id);

		return vm;
	}

	protected void assertATAndAs(ApplianceType at, AtomicService as) {
		assertEquals(at.getName(), as.getName());
		assertEquals(at.getDescription(), as.getDescription());
		assertEquals(at.isHttp(), as.isHttp());
		assertEquals(at.isPublished(), as.isPublished());
		assertEquals(at.isHttp() && at.isIn_proxy(), as.isHttp());
		assertEquals(at.isScalable(), as.isScalable());
		assertEquals(at.isShared(), as.isShared());
		assertEquals(at.isVnc(), as.isVnc());
		assertEquals(at.getTemplates_count() > 0, as.isActive());

		List<ATEndpoint> atEndpoints = at.getEndpoints();
		if (atEndpoints != null) {
			List<Endpoint> asEndpoints = as.getEndpoints();

			assertNotNull(asEndpoints);
			int atSize = atEndpoints.size();
			assertEquals(atSize, asEndpoints.size());

			for (int i = 0; i < atSize; i++) {
				assertAtAndAsEndpoint(atEndpoints.get(i), asEndpoints.get(i));
			}
		}
	}

	/**
	 * @param asEndpoint
	 * @param endpoint
	 */
	private void assertAtAndAsEndpoint(ATEndpoint atEndpoint,
			Endpoint asEndpoint) {
		assertEquals(atEndpoint.getDescription(), asEndpoint.getDescription());
		assertEquals(atEndpoint.getDescriptor(), asEndpoint.getDescriptor());
		EndpointType type = "WS".equalsIgnoreCase(atEndpoint.getEndpoint_type()) ? EndpointType.WS
				: EndpointType.REST;
		assertEquals(type, asEndpoint.getType());
		assertEquals(atEndpoint.getInvocation_path(), asEndpoint.getInvocationPath());
		assertEquals(atEndpoint.getPort().intValue(), asEndpoint.getPort());
		assertEquals(atEndpoint.getService_name(), asEndpoint.getServiceName());
	}

	/**
	 * @param air
	 * @param username
	 */
	protected void mockGetWorkflow() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setVph_username(username);
		when(air.getWorkflow(contextId)).thenReturn(wd);
	}

	/**
	 * @param air
	 * @param contextId
	 */
	protected void mockGetNonExistingWorkflow(AirClient air, String contextId) {
		when(air.getWorkflow(contextId)).thenThrow(getAirException(404));
	}

	protected ServerWebApplicationException getAirException(int status) {
		return new ServerWebApplicationException(Response.status(status)
				.build());
	}
}
