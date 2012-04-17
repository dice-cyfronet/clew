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

package pl.cyfronet.coin.impl.air.client;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AddAtomicServiceRequest {

	private String client;
	
	private String name;
	
	private String description;
	
	private boolean in_proxy;
	
	private boolean scalable;
	
	private boolean published;
	
	private boolean shared;
	
	private boolean http;
	
	private boolean vnc;
	
	private List<ASEndpoint> endpoints;

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the in_proxy
	 */
	public boolean isIn_proxy() {
		return in_proxy;
	}

	/**
	 * @param in_proxy the in_proxy to set
	 */
	public void setIn_proxy(boolean in_proxy) {
		this.in_proxy = in_proxy;
	}

	/**
	 * @return the scalable
	 */
	public boolean isScalable() {
		return scalable;
	}

	/**
	 * @param scalable the scalable to set
	 */
	public void setScalable(boolean scalable) {
		this.scalable = scalable;
	}

	/**
	 * @return the published
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}

	/**
	 * @return the shared
	 */
	public boolean isShared() {
		return shared;
	}

	/**
	 * @param shared the shared to set
	 */
	public void setShared(boolean shared) {
		this.shared = shared;
	}

	/**
	 * @return the http
	 */
	public boolean isHttp() {
		return http;
	}

	/**
	 * @param http the http to set
	 */
	public void setHttp(boolean http) {
		this.http = http;
	}

	/**
	 * @return the vnc
	 */
	public boolean isVnc() {
		return vnc;
	}

	/**
	 * @param vnc the vnc to set
	 */
	public void setVnc(boolean vnc) {
		this.vnc = vnc;
	}

	/**
	 * @return the endpoints
	 */
	public List<ASEndpoint> getEndpoints() {
		return endpoints;
	}

	/**
	 * @param endpoints the endpoints to set
	 */
	public void setEndpoints(List<ASEndpoint> endpoints) {
		this.endpoints = endpoints;
	}
}