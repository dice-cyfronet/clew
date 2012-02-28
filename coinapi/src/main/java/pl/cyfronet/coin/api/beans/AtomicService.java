/*
 * Copyright 2011 ACC CYFRONET AGH
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
package pl.cyfronet.coin.api.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean which describes atomic service (vm template).
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AtomicService {
	private String atomicServiceId;
	private String name;
	private String description;
	private List<Endpoint> endpoint;
	private boolean vnc;
	private boolean http;
	private boolean shared;
	private boolean scalable;
	
	public AtomicService() {
	}
	
	public AtomicService(String name) {
		this.name = name;
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

	public String getAtomicServiceId() {
		return atomicServiceId;
	}

	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "AtomicService [atomicServiceId=" + atomicServiceId + ", name="
				+ name + ", description=" + description + ", endpoint="
				+ endpoint + ", vnc=" + vnc + ", http=" + http + ", shared="
				+ shared + "]";
	}

	public List<Endpoint> getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(List<Endpoint> endpoint) {
		this.endpoint = endpoint;
	}

	public boolean isVnc() {
		return vnc;
	}

	public void setVnc(boolean vnc) {
		this.vnc = vnc;
	}

	public boolean isHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
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
}