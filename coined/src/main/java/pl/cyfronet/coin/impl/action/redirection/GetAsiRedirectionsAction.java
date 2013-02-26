/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package pl.cyfronet.coin.impl.action.redirection;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.GetWorkflowDetailAction;
import pl.cyfronet.coin.impl.action.ReadOnlyAirAction;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @author <a href="mailto:t.bartynski@cyfronet.pl">Tomek Bartyński</a>
 */
public class GetAsiRedirectionsAction extends ReadOnlyAirAction<List<Redirection>> {

	private String asiId;
	private String contextId;
	private String username;
	private String proxyHost;
	private int proxyPort;

	public GetAsiRedirectionsAction(String contextId, String username, String asiId,
			String proxyHost, int proxyPort, AirClient air) {
		super(air);
		this.contextId = contextId;
		this.username = username;
		this.asiId = asiId;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	@Override
	public List<Redirection> execute() throws CloudFacadeException {
		List<Redirection> redirections = new ArrayList<>();
		Action<WorkflowDetail> getWfDetailAct = new GetWorkflowDetailAction(getAir(), contextId, username);
		WorkflowDetail wfd = getWfDetailAct.execute();
		if (wfd.getVms() == null || wfd.getVms().isEmpty()) { return redirections; }
		Redirection red = null;
		String initConfId = null;
		for (Vms asi : wfd.getVms()) {
			if (asi.getVms_id().equals(asiId)) {
				initConfId = asi.getConf_id();
				if (initConfId == null) {
					throw new CloudFacadeException("Error while getting redirection for ASI " + asiId + " because ASI does not have a valid init conf id");
				}
				List<PortMapping> pms = asi.getInternal_port_mappings();
				if (pms == null) { break; }
				for(PortMapping pm : pms) {
					red = new Redirection();
					red.setId(pm.getId());
					red.setFromPort(pm.getHeadnode_port());
					red.setHost(pm.getHeadnode_ip());
					red.setName(pm.getService_name());
					red.setToPort(pm.getVm_port());
					red.setType(RedirectionType.TCP);
					redirections.add(red);					
				}
				break;
			}
		}
		
		ApplianceType applType = getAir().getTypeFromVM(asiId);
		if (applType == null) { return redirections; }
		List<ATPortMapping> atpms = applType.getPort_mappings();
		if (atpms == null || atpms.isEmpty()) { return redirections; }
		for (ATPortMapping atpm : atpms) {
			if (atpm.isHttp()) {
				red = new Redirection();
				red.setId(atpm.getId());
				red.setFromPort(proxyPort);
				red.setHost(proxyHost);
				red.setName(atpm.getService_name());
				if (wfd.getWorkflow_type() == WorkflowType.development) {
					red.setPostfix(contextId + "/" + asiId + "/" + atpm.getService_name());
				} else {
					red.setPostfix(contextId + "/" + initConfId + "/" + atpm.getService_name());
				}
				red.setToPort(atpm.getPort());
				red.setType(RedirectionType.HTTP);
				redirections.add(red);
			}
		}
		return redirections;
	}

}
