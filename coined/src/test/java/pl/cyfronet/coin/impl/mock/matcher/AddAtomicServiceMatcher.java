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
package pl.cyfronet.coin.impl.mock.matcher;

import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.AddAtomicServiceRequest;
import pl.cyfronet.coin.impl.air.client.AppliancePreferences;
import pl.cyfronet.coin.impl.air.client.ApplianceSla;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddAtomicServiceMatcher extends
		BaseMatcher<AddAtomicServiceRequest> {

	private ApplianceType as;
	private String username;
	private boolean development;
	private boolean creatingNewAS;

	public AddAtomicServiceMatcher(String username, ApplianceType as) {
		this.as = as;
		this.username = username;
	}

	public AddAtomicServiceMatcher(String username, ApplianceType as,
			boolean development) {
		this.as = as;
		this.username = username;
		this.development = development;
	}

	public void setCreatingNewAS(boolean creatingNewAS) {
		this.creatingNewAS = creatingNewAS;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object arg0) {
		AddAtomicServiceRequest request = (AddAtomicServiceRequest) arg0;
		return request.getAuthor().equals(username)
				&& nameEquals(request)
				&& request.getClient().equals("rest")
				&& request.getDescription().equals(as.getDescription())
				&& equals(request.getEndpoints(), as.getEndpoints())
				&& portMappingsEquals(request.getPort_mappings(),
						as.getPort_mappings())
				&& request.getScalable() == as.isScalable()
				&& request.getShared() == as.isShared()
				&& correctPublishedState(request)
				&& securityProxyNameEquals(request) && propsEquals(request)
				&& slaEquals(request);
	}

	private boolean slaEquals(AddAtomicServiceRequest request) {
		if (as.getAppliance_sla() != null) {
			ApplianceSla sla = as.getAppliance_sla();
			ApplianceSla requestSla = request.getAppliance_sla();
			return sla.getConcurrent_requests() == requestSla
					.getConcurrent_requests()
					&& sla.getMean_response_time() == requestSla
							.getMean_response_time()
					&& sla.getRequests_throughput() == requestSla
							.getRequests_throughput();

		}
		return request.getAppliance_sla() == null;
	}

	private boolean propsEquals(AddAtomicServiceRequest request) {
		if (as.getAppliance_preferences() != null) {
			AppliancePreferences prefs = as.getAppliance_preferences();
			AppliancePreferences requestPrefs = request
					.getAppliance_preferences();
			return prefs.getCpu() == requestPrefs.getCpu()
					&& prefs.getDisk() == requestPrefs.getDisk()
					&& prefs.getMemory() == requestPrefs.getMemory();
		}
		return request.getAppliance_preferences() == null;
	}

	private boolean securityProxyNameEquals(AddAtomicServiceRequest request) {
		if (as.getProxy_conf_name() == null) {
			return request.getProxy_conf_name() == null;
		} else {
			return as.getProxy_conf_name().equals(request.getProxy_conf_name());
		}
	}

	private boolean nameEquals(AddAtomicServiceRequest request) {
		if (development) {
			return request.getName().startsWith(as.getName());
		} else {
			return request.getName().equals(as.getName());
		}
	}

	private boolean correctPublishedState(AddAtomicServiceRequest request) {
		if (request.getPublished() == as.isPublished()) {
			if (creatingNewAS) {
				return !as.isDevelopment();
			} else {
				return request.getDevelopment() == as.isDevelopment();
			}
		}
		return false;
	}

	/**
	 * @param asEndpoints
	 * @param endpoint
	 * @return
	 */
	private boolean equals(List<ATEndpoint> asEndpoints,
			List<ATEndpoint> endpoints) {
		if (asEndpoints != null && endpoints != null) {
			if (asEndpoints.size() == endpoints.size()) {
				for (int i = 0; i < asEndpoints.size(); i++) {
					if (!equals(asEndpoints.get(i), endpoints.get(i))) {
						return false;
					}
				}
				return true;
			}
		} else {
			return (asEndpoints == null || asEndpoints.size() == 0)
					&& endpoints == null;
		}
		return false;
	}

	private boolean portMappingsEquals(List<ATPortMapping> request,
			List<ATPortMapping> expected) {
		if (request != null && expected != null) {
			if (request.size() == expected.size()) {
				for (int i = 0; i < request.size(); i++) {
					if (!equals(request.get(i), expected.get(i))) {
						return false;
					}
				}
				return true;
			}
		} else {
			return (request == null || request.size() == 0) && expected == null;
		}
		return false;
	}

	private boolean equals(ATPortMapping actual, ATPortMapping expected) {
		return actual.getPort() == expected.getPort()
				&& actual.getService_name().equals(expected.getService_name())
				&& actual.isHttp() == expected.isHttp();
	}

	/**
	 * @param asEndpoint
	 * @param endpoint
	 * @return
	 */
	private boolean equals(ATEndpoint asEndpoint, ATEndpoint endpoint) {
		return asEndpoint.getInvocation_path().equals(
				endpoint.getInvocation_path())
				&& asEndpoint.getPort() == endpoint.getPort()
				&& asEndpoint.getDescription()
						.equals(endpoint.getDescription())
				&& asEndpoint.getDescriptor().equals(endpoint.getDescriptor())
				&& asEndpoint.getEndpoint_type().equals(
						endpoint.getEndpoint_type());
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
	 */
	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub

	}

}
