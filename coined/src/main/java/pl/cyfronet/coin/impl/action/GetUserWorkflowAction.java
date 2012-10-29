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
package pl.cyfronet.coin.impl.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Credential;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.impl.air.client.AirClient;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class GetUserWorkflowAction extends ReadOnlyAirAction<Workflow> {

	private String contextId;
	private String username;
	private Properties credentialProperties;

	GetUserWorkflowAction(AirClient air, Properties credentialProperties,
			String contextId, String username) {
		super(air);
		this.contextId = contextId;
		this.username = username;
		this.credentialProperties = credentialProperties;
	}

	@Override
	public Workflow execute() throws CloudFacadeException {
		WorkflowDetail detail = getUserWorkflow();

		Workflow workflow = new Workflow();
		workflow.setName(detail.getName());
		workflow.setType(detail.getWorkflow_type());

		List<Vms> vms = detail.getVms();
		if (vms != null) {
			List<AtomicServiceInstance> instances = new ArrayList<AtomicServiceInstance>();
			for (Vms vm : vms) {
				if (vm.getVms_id() == null) {
					// /AIR returns vms:[null] when workflow does not have VMs
					// and it is parsed by CXF as Vms object with all properties
					// set to null.
					continue;
				}

				AtomicServiceInstance instance = new AtomicServiceInstance();
				instance.setAtomicServiceId(vm.getAppliance_type());
				instance.setId(vm.getVms_id());
				instance.setStatus(vm.getState());
				instance.setName(vm.getName());
				instance.setConfigurationId(vm.getConf_id());
				instance.setMessage(""); // TODO

				addRedirections(instance, vm);

				if (detail.getWorkflow_type() == WorkflowType.development) {
					instance.setCredential(getCredential(vm.getAppliance_type()));
				}

				instances.add(instance);

			}
			workflow.setAtomicServiceInstances(instances);
		}

		return workflow;
	}

	private WorkflowDetail getUserWorkflow() {
		return new GetWorkflowDetailAction(getAir(), contextId, username)
				.execute();
	}

	private void addRedirections(AtomicServiceInstance instance, Vms vm) {
		List<Redirection> redirections = new ArrayList<Redirection>();
		if (vm.getInternal_port_mappings() != null) {
			for (PortMapping portMapping : vm.getInternal_port_mappings()) {
				Redirection redirection = new Redirection();
				redirection.setHost(portMapping.getHeadnode_ip());
				redirection.setFromPort(portMapping.getHeadnode_port());
				redirection.setToPort(portMapping.getVm_port());
				// information about redirection is wrong represented in AiR it
				// should be refactored .
				redirection.setType(portMapping.isHttp() ? RedirectionType.HTTP
						: RedirectionType.TCP);
				redirection.setName(portMapping.getService_name());

				redirections.add(redirection);
			}
		}
		instance.setRedirections(redirections);
	}

	private Credential getCredential(String vms_id) {
		Credential cred = new Credential();
		if (credentialProperties != null) {
			cred.setUsername(credentialProperties.getProperty(vms_id
					+ ".username"));
			cred.setPassword(credentialProperties.getProperty(vms_id
					+ ".password"));
		}
		return cred;
	}
}
