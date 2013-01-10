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
package pl.cyfronet.coin.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class CoinedAsserts {

	public static void assertATAndAs(ApplianceType at, AtomicService as, String... endpointsPayload) {
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
				assertAtAndAsEndpoint(endpointsPayload[i], atEndpoints.get(i), asEndpoints.get(i));
			}
		}
	}

	private static void assertAtAndAsEndpoint(String descriptor, ATEndpoint atEndpoint,
			Endpoint asEndpoint) {
		assertEquals(asEndpoint.getDescription(), atEndpoint.getDescription());
		assertEquals(asEndpoint.getDescriptor(), descriptor);
		EndpointType type = "WS"
				.equalsIgnoreCase(atEndpoint.getEndpoint_type()) ? EndpointType.WS
				: EndpointType.REST;
		assertEquals(type, asEndpoint.getType());
		assertEquals(atEndpoint.getInvocation_path(),
				asEndpoint.getInvocationPath());
		assertEquals(atEndpoint.getPort(), asEndpoint.getPort());
		assertEquals(atEndpoint.getService_name(), asEndpoint.getServiceName());
		assertEquals(atEndpoint.getId(), asEndpoint.getId());
	}
}
