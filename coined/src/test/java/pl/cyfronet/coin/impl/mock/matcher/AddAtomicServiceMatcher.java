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

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.impl.air.client.ATEndpoint;
import pl.cyfronet.coin.impl.air.client.AddAtomicServiceRequest;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AddAtomicServiceMatcher extends
		BaseMatcher<AddAtomicServiceRequest> {

	private AtomicService as;
	private String username;
	private boolean development;

	public AddAtomicServiceMatcher(String username, AtomicService as) {
		this.as = as;
		this.username = username;
	}

	public AddAtomicServiceMatcher(String username, AtomicService as,
			boolean development) {
		this.as = as;
		this.username = username;
		this.development = development;
	}

	/*
	 * (non-Javadoc)
	 * @see org.hamcrest.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(Object arg0) {
		AddAtomicServiceRequest request = (AddAtomicServiceRequest) arg0;
		return request.getAuthor().equals(username) && nameEquals(request)
				&& request.getClient().equals("rest")
				&& request.getDescription().equals(as.getDescription())
				&& equals(request.getEndpoints(), as.getEndpoints())
				&& request.isHttp() == as.isHttp()
				&& request.isIn_proxy() == as.isInProxy()
				&& request.isPublished() == as.isPublished()
				&& request.isScalable() == as.isScalable()
				&& request.isShared() == as.isShared()
				&& request.isVnc() == as.isVnc()
				&& request.isDevelopment() == as.isDevelopment();
	}

	private boolean nameEquals(AddAtomicServiceRequest request) {
		if (development) {
			return request.getName().startsWith(as.getName());
		} else {
			return request.getName().equals(as.getName());
		}
	}

	/**
	 * @param asEndpoints
	 * @param endpoint
	 * @return
	 */
	private boolean equals(List<ATEndpoint> asEndpoints,
			List<Endpoint> endpoints) {
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
			return (asEndpoints == null || asEndpoints.size() == 0) && endpoints == null;
		}
		return false;
	}

	/**
	 * @param asEndpoint
	 * @param endpoint
	 * @return
	 */
	private boolean equals(ATEndpoint asEndpoint, Endpoint endpoint) {
		EndpointType endpointType = endpoint.getType() == null ? EndpointType.REST
				: endpoint.getType();

		return asEndpoint.getInvocation_path().equals(
				endpoint.getInvocationPath())
				&& asEndpoint.getPort() == endpoint.getPort()
				&& asEndpoint.getDescription()
						.equals(endpoint.getDescription())
				&& asEndpoint.getDescriptor().equals(endpoint.getDescriptor())
				&& asEndpoint.getService_name().equals(
						endpoint.getServiceName())
				&& asEndpoint.getEndpoint_type()
						.equals(endpointType.toString());
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
