package pl.cyfronet.coin.impl.action.as;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.beans.InvocationPathInfo;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class GetInvocationPathInfo extends
		ReadOnlyAirAction<InvocationPathInfo> {

	private static final Logger logger = LoggerFactory
			.getLogger(GetInvocationPathInfo.class);

	private String asIs;
	private String serviceName;

	public GetInvocationPathInfo(ActionFactory actionFactory, String asId,
			String serviceName) {
		super(actionFactory);
		this.asIs = asId;
		this.serviceName = serviceName;
	}

	@Override
	public InvocationPathInfo execute() throws CloudFacadeException {
		ApplianceType at = getApplianceType(asIs);
		for (ATPortMapping portMapping : at.getPort_mappings()) {
			logger.debug(
					"Checking redirectino for invocation path info {} vs. {}",
					portMapping.getService_name(), serviceName);
			if (portMapping.getService_name().equals(serviceName)) {
				InvocationPathInfo info = new InvocationPathInfo();
				info.setAtomicServiceId(asIs);
				info.setRedirectionName(serviceName);
				return info;
			}
		}
		throw new RedirectionNotFoundException();
	}

}
