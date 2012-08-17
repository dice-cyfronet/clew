package pl.cyfronet.coin.impl.action;

import static org.mockito.Mockito.mock;

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
	}
}
