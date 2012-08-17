package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceConfiguration;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetInitialConfigurationsAction extends AirAction<List<InitialConfiguration>> {

	private String atomicServiceId;
	
	/**
	 * @param air Air client.
	 * @param atomicServiceId Atomic Service id.
	 */
	public GetInitialConfigurationsAction(AirClient air,
			String atomicServiceId) {
		super(air);
		this.atomicServiceId = atomicServiceId;
	}

	/**
	 * Get Atomic Service initial configurations.
	 * @return List of Atomic Service configurations.
	 */
	@Override
	public List<InitialConfiguration> execute() throws CloudFacadeException {
		ApplianceType type = getApplianceType(atomicServiceId);
		List<ApplianceConfiguration> typeConfigurations = type
				.getConfigurations();
		List<InitialConfiguration> configurations = new ArrayList<InitialConfiguration>();
		if (typeConfigurations != null) {
			for (ApplianceConfiguration applianceConfiguration : typeConfigurations) {
				InitialConfiguration configuration = new InitialConfiguration();
				configuration.setId(applianceConfiguration.getId());
				configuration.setName(applianceConfiguration.getConfig_name());
				configurations.add(configuration);
			}
		}

		return configurations;
	}

	@Override
	public void rollback() {
		// read only action, no rollback needed.
	}
}
