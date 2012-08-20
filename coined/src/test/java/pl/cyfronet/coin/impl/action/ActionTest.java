package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.mock;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.testng.annotations.BeforeMethod;

import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.dyrealla.core.DyReAllaManagerService;

public abstract class ActionTest {

	protected AirClient air;

	protected DyReAllaManagerService atmosphere;

	protected ActionFactory actionFactory;
	
	protected String cloudSiteId = "siteId";
	
	@BeforeMethod
	protected void setUp() {
		air = mock(AirClient.class);
		atmosphere = mock(DyReAllaManagerService.class);
		
		actionFactory = new ActionFactory();
		actionFactory.setAir(air);
		actionFactory.setAtmosphere(atmosphere);
		actionFactory.setDefaultSiteId(cloudSiteId);
		
		Properties credentialProp = new Properties();
		credentialProp.put("type1.username", "vm1Username");
		credentialProp.put("type1.password", "vm1Password");
		credentialProp.put("type2.username", "vm2Username");
		credentialProp.put("type2.password", "vm2Password");
		
		actionFactory.setCredentialProperties(credentialProp);
	}
	
	protected ServerWebApplicationException getAirException(int status) {
		return new ServerWebApplicationException(Response.status(status)
				.build());
	}
}
