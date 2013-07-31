package pl.cyfronet.coin.impl.action.redirection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.redirection.HttpRedirection;
import pl.cyfronet.coin.api.beans.redirection.NatRedirection;
import pl.cyfronet.coin.api.beans.redirection.Redirections;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.VmHttpRedirection;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @since 1.6
 * @see #1726
 */
public class GetAsiRedirectionsAction extends ReadOnlyAirAction<Redirections> {

	private String username;
	private String contextId;
	private String asiIdentifier;
	private String asiId;
	private Vms asi;

	public GetAsiRedirectionsAction(ActionFactory actionFactory,
			String username, String contextId, String asiIdentifier) {
		super(actionFactory);
		this.username = username;
		this.contextId = contextId;
		this.asiIdentifier = asiIdentifier;
	}

	@Override
	public Redirections execute() throws CloudFacadeException {
		findAsi();

		Redirections redirections = new Redirections();
		redirections.setHttp(getHttpRedirections());
		redirections.setNat(getNatRedirections());

		return redirections;
	}

	private void findAsi() {
		asi = getAsi();
		if (asi == null) {
			throw new AtomicServiceInstanceNotFoundException();
		}
		asiId = asi.getVms_id();
	}

	private Vms getAsi() {
		Action<WorkflowDetail> getWfDetailAct = getActionFactory()
				.createGetWorkflowDetailAction(contextId, username);
		WorkflowDetail wfd = getWfDetailAct.execute();
		for (Vms instance : wfd.getVms()) {
			if (asiIdentifier.equals(instance.getVms_id())
					|| asiIdentifier.equals(instance.getConfiguration())) {
				return instance;
			}
		}
		return null;
	}

	private List<HttpRedirection> getHttpRedirections() {
		List<HttpRedirection> httpRedirections = new ArrayList<>();

		ApplianceType applType = getAir().getTypeFromVM(asiId);
		if (applType == null) {
			return httpRedirections;
		}

		List<ATPortMapping> atpms = applType.getPort_mappings();
		if (atpms == null || atpms.isEmpty()) {
			return httpRedirections;
		}

		for (ATPortMapping atpm : atpms) {
			if (atpm.isHttp() || atpm.isHttps()) {
				HttpRedirection httpRedirection = new HttpRedirection();
				httpRedirection.setId(atpm.getId());
				httpRedirection.setName(atpm.getService_name());
				httpRedirection.setToPort(atpm.getPort());
				httpRedirection.setUrls(getUrls(atpm));

				httpRedirections.add(httpRedirection);
			}
		}

		return httpRedirections;
	}

	private List<String> getUrls(ATPortMapping atpm) {
		List<String> urls = getProxyUrl(atpm);
		if (urls == null || urls.size() == 0) {
			urls = getDirectUrl(atpm);
		}

		return urls;
	}

	private List<String> getDirectUrl(ATPortMapping atpm) {
		List<String> urls = new ArrayList<>();
		if (atpm.isHttp()) {
			urls.add(String.format("http://%s:%s", getAsiIp(), atpm.getPort()));
		}

		if (atpm.isHttps()) {
			urls.add(String.format("https://%s:%s", getAsiIp(), atpm.getPort()));
		}

		return urls;
	}

	private String getAsiIp() {
		List<String> ips = getIps();
		if (ips.size() > 0) {
			for (String ip : getIps()) {
				try {
					if (!InetAddress.getByName(ip).isSiteLocalAddress()) {
						return ip;
					}
				} catch (UnknownHostException e) {
					// wrong ip - skipping.
				}
			}
			// no public ip returning first one.
			return ips.get(0);
		}
		return null;
	}

	private List<String> getIps() {
		List<String> ips = new ArrayList<>();
		if (asi.getSpecs() != null && asi.getSpecs().getIp() != null
				&& asi.getSpecs().getIp().size() > 0) {
			ips = asi.getSpecs().getIp();
		}
		return ips;
	}

	private List<String> getProxyUrl(ATPortMapping atpm) {
		List<String> urls = new ArrayList<>();
		if (asi.getHttp_redirections() != null) {
			for (VmHttpRedirection httpRedirection : asi.getHttp_redirections()) {
				if (httpRedirection.getVm_port() == atpm.getPort()) {
					urls.add(httpRedirection.getUrl());
				}
			}
		}
		return urls;
	}

	private List<NatRedirection> getNatRedirections() {
		List<NatRedirection> natRedirections = new ArrayList<>();
		List<PortMapping> pms = asi.getInternal_port_mappings();
		if (pms == null) {
			return natRedirections;
		}
		for (PortMapping pm : pms) {
			NatRedirection natRedirection = new NatRedirection();
			natRedirection.setId(pm.getId());
			natRedirection.setFromPort(pm.getHeadnode_port());
			natRedirection.setHost(pm.getHeadnode_ip());
			natRedirection.setName(pm.getService_name());
			natRedirection.setToPort(pm.getVm_port());
			natRedirection.setType(RedirectionType.TCP);
			// #1942
			natRedirection.setDirect(isDirect(pm.getHeadnode_ip()));

			natRedirections.add(natRedirection);
		}

		return natRedirections;
	}

	private boolean isDirect(String ip) {
		return getIps().contains(ip);
	}
}
