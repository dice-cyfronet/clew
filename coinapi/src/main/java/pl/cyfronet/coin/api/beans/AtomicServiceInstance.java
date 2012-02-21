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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean which describes atomic service instance (vm instance).
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AtomicServiceInstance {
	private String atomicServiceId;
	private String instanceId;
	private Status status;
	private String name;
	
	public enum Status {
		Running,
		Paused,
		Stopped,
		Booting,
		Stopping
	}
	
	public AtomicServiceInstance() {
		
	}

	public AtomicServiceInstance(String name) {
		this.name = name;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	@Override
	public String toString() {
		return "AtomicServiceInstance [instanceId=" + instanceId
				+ ", toString()=" + super.toString() + "]";
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getAtomicServiceId() {
		return atomicServiceId;
	}

	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}