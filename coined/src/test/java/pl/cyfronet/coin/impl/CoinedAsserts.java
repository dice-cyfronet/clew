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

	public static void assertATAndAs(ApplianceType at, AtomicService as,
			String... endpointsPayload) {
		assertEquals(as.getAtomicServiceId(), at.getId());
		assertEquals(as.getName(), at.getName());
		assertEquals(as.getDescription(), at.getDescription());
		assertEquals(as.isHttp(), at.isHttp());
		assertEquals(as.isPublished(), at.isPublished());
		assertEquals(as.isHttp(), at.isHttp() && at.isIn_proxy());
		assertEquals(as.isScalable(), at.isScalable());
		assertEquals(as.isShared(), at.isShared());
		assertEquals(as.isVnc(), at.isVnc());
		assertEquals(as.isActive(), at.getTemplates_count() > 0);

		List<ATEndpoint> atEndpoints = at.getEndpoints();
		if (atEndpoints != null) {
			List<Endpoint> asEndpoints = as.getEndpoints();

			assertNotNull(asEndpoints);
			int atSize = atEndpoints.size();
			assertEquals(asEndpoints.size(), atSize);

			for (int i = 0; i < atSize; i++) {
				assertAtAndAsEndpoint(endpointsPayload[i], atEndpoints.get(i),
						asEndpoints.get(i));
			}
		}
	}

	private static void assertAtAndAsEndpoint(String descriptor,
			ATEndpoint atEndpoint, Endpoint asEndpoint) {
		assertEquals(asEndpoint.getDescription(), atEndpoint.getDescription());
		assertEquals(asEndpoint.getDescriptor(), descriptor);
		EndpointType type = "WS"
				.equalsIgnoreCase(atEndpoint.getEndpoint_type()) ? EndpointType.WS
				: EndpointType.REST;
		assertEquals(asEndpoint.getType(), type);
		assertEquals(asEndpoint.getInvocationPath(),
				atEndpoint.getInvocation_path());
		assertEquals(asEndpoint.getPort(), atEndpoint.getPort());
		assertEquals(asEndpoint.getId(), atEndpoint.getId());
	}
}
