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
 * Bean which describes atomic service (vm template).
 * @author <a href="mailto:d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class AtomicService {
	private String atomicServiceId;
	private String name;
	private String description;
	private boolean shared;
	private boolean scalable;
	private boolean published;
	private boolean active;
	private boolean development;

	private String proxyConfigurationName;

	private Float cpu;

	private Integer memory;

	private Integer disk;

	/**
	 * @see #1433
	 */
	private String owner;

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
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the development
	 */
	public boolean isDevelopment() {
		return development;
	}

	/**
	 * @param development the development to set
	 */
	public void setDevelopment(boolean development) {
		this.development = development;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the proxyConfigurationName
	 */
	public String getProxyConfigurationName() {
		return proxyConfigurationName;
	}

	/**
	 * @param proxyConfigurationName the proxyConfigurationName to set
	 */
	public void setProxyConfigurationName(String proxyConfigurationName) {
		this.proxyConfigurationName = proxyConfigurationName;
	}

	/**
	 * @return the cpu
	 */
	public Float getCpu() {
		return cpu;
	}

	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(Float cpu) {
		this.cpu = cpu;
	}

	/**
	 * @return the memory
	 */
	public Integer getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	/**
	 * @return the disk
	 */
	public Integer getDisk() {
		return disk;
	}

	/**
	 * @param disk the disk to set
	 */
	public void setDisk(Integer disk) {
		this.disk = disk;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AtomicService [atomicServiceId=" + atomicServiceId + ", name="
				+ name + ", description=" + description + ", shared=" + shared
				+ ", scalable=" + scalable + ", published=" + published
				+ ", active=" + active + ", development=" + development
				+ ", proxyConfigurationName=" + proxyConfigurationName
				+ ", cpu=" + cpu + ", memory=" + memory + ", disk=" + disk
				+ ", owner=" + owner + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((atomicServiceId == null) ? 0 : atomicServiceId.hashCode());
		result = prime * result + ((cpu == null) ? 0 : cpu.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (development ? 1231 : 1237);
		result = prime * result + ((disk == null) ? 0 : disk.hashCode());
		result = prime * result + ((memory == null) ? 0 : memory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime
				* result
				+ ((proxyConfigurationName == null) ? 0
						: proxyConfigurationName.hashCode());
		result = prime * result + (published ? 1231 : 1237);
		result = prime * result + (scalable ? 1231 : 1237);
		result = prime * result + (shared ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomicService other = (AtomicService) obj;
		if (active != other.active)
			return false;
		if (atomicServiceId == null) {
			if (other.atomicServiceId != null)
				return false;
		} else if (!atomicServiceId.equals(other.atomicServiceId))
			return false;
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
		if (development != other.development)
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
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (proxyConfigurationName == null) {
			if (other.proxyConfigurationName != null)
				return false;
		} else if (!proxyConfigurationName.equals(other.proxyConfigurationName))
			return false;
		if (published != other.published)
			return false;
		if (scalable != other.scalable)
			return false;
		if (shared != other.shared)
			return false;
		return true;
	}
}