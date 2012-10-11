package pl.cyfronet.coin.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class CoinedAsserts {

	public static void assertATAndAs(ApplianceType at, AtomicService as) {
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

	private static void assertAtAndAsEndpoint(ATEndpoint atEndpoint,
			Endpoint asEndpoint) {
		assertEquals(atEndpoint.getDescription(), asEndpoint.getDescription());
		assertEquals(atEndpoint.getDescriptor(), asEndpoint.getDescriptor());
		EndpointType type = "WS"
				.equalsIgnoreCase(atEndpoint.getEndpoint_type()) ? EndpointType.WS
				: EndpointType.REST;
		assertEquals(type, asEndpoint.getType());
		assertEquals(atEndpoint.getInvocation_path(),
				asEndpoint.getInvocationPath());
		assertEquals(atEndpoint.getPort(), asEndpoint.getPort());
		assertEquals(atEndpoint.getService_name(), asEndpoint.getServiceName());
	}
}
