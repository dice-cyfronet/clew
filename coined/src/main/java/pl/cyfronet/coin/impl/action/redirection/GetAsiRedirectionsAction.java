package pl.cyfronet.coin.impl.action.redirection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.WorkflowType;
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
		if (wfd.getWorkflow_type() == WorkflowType.development) {
			for (Vms instance : wfd.getVms()) {
				if (instance.getVms_id().equals(asiIdentifier)) {
					return instance;
				}
			}
		} else {
			for (Vms instance : wfd.getVms()) {
				if (instance.getConfiguration().equals(asiIdentifier)) {
					return instance;
				}
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
				httpRedirection.setUrl(getUrl(atpm));

				httpRedirections.add(httpRedirection);
			}
		}

		return httpRedirections;
	}

	private String getUrl(ATPortMapping atpm) {
		String url = getProxyUrl(atpm);
		if (url == null) {
			url = getDirectUrl(atpm);
		}

		return url;
	}

	private String getDirectUrl(ATPortMapping atpm) {
		String prefix = atpm.isHttp() ? "http://" : "https://";
		return String.format("%s%s:%s", prefix, getAsiIp(), atpm.getPort());
	}

	private String getAsiIp() {
		if (asi.getSpecs() != null && asi.getSpecs().getIp() != null
				&& asi.getSpecs().getIp().size() > 0) {
			List<String> ips = asi.getSpecs().getIp();
			for (String ip : ips) {
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

	private String getProxyUrl(ATPortMapping atpm) {
		if (asi.getHttp_redirections() != null) {
			for (VmHttpRedirection httpRedirection : asi.getHttp_redirections()) {
				if (httpRedirection.getVm_port() == atpm.getPort()) {
					return httpRedirection.getUrl();
				}
			}
		}
		return null;
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
			natRedirections.add(natRedirection);
		}

		return natRedirections;
	}
}
