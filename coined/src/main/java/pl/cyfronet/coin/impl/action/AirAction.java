package pl.cyfronet.coin.impl.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public abstract class AirAction<T> implements Action<T> {

	static final Logger logger = LoggerFactory.getLogger(AirAction.class);

	private AirClient air;

	AirAction(AirClient air) {
		this.air = air;
	}

	protected List<ApplianceType> getApplianceTypes() {
		return air.getApplianceTypes();
	}

	protected AirClient getAir() {
		return air;
	}

	protected ApplianceType getApplianceType(String applianceTypeName)
			throws AtomicServiceNotFoundException {
		List<ApplianceType> applianceTypes = getApplianceTypes();

		for (ApplianceType applianceType : applianceTypes) {
			String name = applianceType.getName();
			if (name != null && name.equals(applianceTypeName)) {
				return applianceType;
			}
		}

		logger.debug("Atomic service {} not found", applianceTypeName);
		throw new AtomicServiceNotFoundException();
	}
}
