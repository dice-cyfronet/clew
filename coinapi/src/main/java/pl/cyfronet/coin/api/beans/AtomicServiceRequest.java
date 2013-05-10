/*
 * Copyright 2013 ACC CYFRONET AGH
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
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
@XmlRootElement
public class AtomicServiceRequest {

	private String name;
	private String description;
	
	private String proxyConfigurationName;
	private Boolean shared;
	private Boolean scalable;
	private Boolean published;
	
	private Float cpu;
	private Long memory;
	private Long disk;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProxyConfigurationName() {
		return proxyConfigurationName;
	}

	public void setProxyConfigurationName(String proxyConfigurationName) {
		this.proxyConfigurationName = proxyConfigurationName;
	}

	/**
	 * @return the shared
	 */
	public Boolean getShared() {
		return shared;
	}

	/**
	 * @param shared the shared to set
	 */
	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	/**
	 * @return the scalable
	 */
	public Boolean getScalable() {
		return scalable;
	}

	/**
	 * @param scalable the scalable to set
	 */
	public void setScalable(Boolean scalable) {
		this.scalable = scalable;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published) {
		this.published = published;
	}

	public Float getCpu() {
		return cpu;
	}

	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	public Long getMemory() {
		return memory;
	}

	public void setMemory(Long memory) {
		this.memory = memory;
	}

	public Long getDisk() {
		return disk;
	}

	public void setDisk(Long disk) {
		this.disk = disk;
	}

	
	@Override
	public String toString() {
		return "AtomicServiceRequest [name=" + name + ", description="
				+ description + ", proxyConfigurationName="
				+ proxyConfigurationName + ", shared=" + shared + ", scalable="
				+ scalable + ", published=" + published + ", cpu=" + cpu
				+ ", memory=" + memory + ", disk=" + disk + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpu == null) ? 0 : cpu.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((disk == null) ? 0 : disk.hashCode());
		result = prime * result + ((memory == null) ? 0 : memory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((proxyConfigurationName == null) ? 0
						: proxyConfigurationName.hashCode());
		result = prime * result
				+ ((published == null) ? 0 : published.hashCode());
		result = prime * result
				+ ((scalable == null) ? 0 : scalable.hashCode());
		result = prime * result + ((shared == null) ? 0 : shared.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomicServiceRequest other = (AtomicServiceRequest) obj;
		if (cpu == null) {
			if (other.cpu != null)
				return false;
		} else if (!cpu.equals(other.cpu))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (disk == null) {
			if (other.disk != null)
				return false;
		} else if (!disk.equals(other.disk))
			return false;
		if (memory == null) {
			if (other.memory != null)
				return false;
		} else if (!memory.equals(other.memory))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (proxyConfigurationName == null) {
			if (other.proxyConfigurationName != null)
				return false;
		} else if (!proxyConfigurationName.equals(other.proxyConfigurationName))
			return false;
		if (published == null) {
			if (other.published != null)
				return false;
		} else if (!published.equals(other.published))
			return false;
		if (scalable == null) {
			if (other.scalable != null)
				return false;
		} else if (!scalable.equals(other.scalable))
			return false;
		if (shared == null) {
			if (other.shared != null)
				return false;
		} else if (!shared.equals(other.shared))
			return false;
		return true;
	}
}
