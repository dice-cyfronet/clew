package pl.cyfronet.coin.impl.action;

import static pl.cyfronet.coin.impl.BeanConverter.getAtomicService;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetAtomicServiceAction extends AirAction<AtomicService> {

	private String atomicServiceId;

	/**
	 * @param air Air rest client.
	 * @param atomicServiceId Atomic service instance. Right now it is equals
	 *            into atomic service name.
	 */
	GetAtomicServiceAction(AirClient air, String atomicServiceId) {
		super(air);
		this.atomicServiceId = atomicServiceId;
	}

	/**
	 * Get atomic service.
	 * @return Atomic service
	 * @throws AtomicServiceNotFoundException Thrown if atomic service with
	 *             given id is not registered in atmosphere.
	 */
	@Override
	public AtomicService execute() throws CloudFacadeException {
		ApplianceType applianceType = getApplianceType(atomicServiceId);
		return getAtomicService(applianceType);
	}

	@Override
	public void rollback() {
		// read only action, no rollback needed
	}

}
