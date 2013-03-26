package pl.cyfronet.coin.impl.action.as;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.utils.FileUtils;
import pl.cyfronet.coin.impl.utils.UrlUtils;

public class GetServicesSetAction extends ReadOnlyAirAction<String> {

	private String coinBaseUrl;

	private static final String serviceTemplatesTemplate = FileUtils
			.getFileContent("services_set/serviceDescriptions.tpl");

	private static final String providerTemplate = FileUtils
			.getFileContent("services_set/provider.tpl");
	
	public GetServicesSetAction(ActionFactory actionFactory, String coinBaseUrl) {
		super(actionFactory);
		this.coinBaseUrl = coinBaseUrl;
	}

	@Override
	public String execute() throws CloudFacadeException {
		StringBuilder sb = new StringBuilder();

		List<ApplianceType> applianceTypes = getAir().getApplianceTypes(false);

		for (ApplianceType applianceType : applianceTypes) {
			writeAtomicServiceEndpointIntoServicesSet(sb, applianceType);
		}

		return String.format(serviceTemplatesTemplate, sb);
	}

	private void writeAtomicServiceEndpointIntoServicesSet(StringBuilder sb,
			ApplianceType atomicService) {
		Map<Integer, String> port2name = new HashMap<>();
		for (ATPortMapping portMapping : atomicService.getPort_mappings()) {
			port2name.put(portMapping.getPort(), portMapping.getService_name());
		}

		for (ATEndpoint endpoint : atomicService.getEndpoints()) {
			if ("WS".equalsIgnoreCase(endpoint.getEndpoint_type())) {
				String descriptorUrl = getDescriptorUrl(atomicService.getId(),
						port2name.get(endpoint.getPort()),
						endpoint.getInvocation_path());
				String providerSection = getProviderServiceSetSection(descriptorUrl);
				sb.append(providerSection);
			}
		}
	}

	private String getProviderServiceSetSection(String providerDescriptorUrl) {
		return String.format(providerTemplate, providerDescriptorUrl);
	}

	private String getDescriptorUrl(String atomicServiceId,
			String redirectionName, String invocationPath) {
		String descriptorUrl = String.format("%s/as/%s/endpoint/%s/%s",
				new Object[] { coinBaseUrl, atomicServiceId, redirectionName,
						getDecodedInvocationPath(invocationPath) });
		return UrlUtils.convertToURLEscapingIllegalCharacters(descriptorUrl);
	}

	private String getDecodedInvocationPath(String path) {
		return path.replaceAll("^/+", "");
	}
}
