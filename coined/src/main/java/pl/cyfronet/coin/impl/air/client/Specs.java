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

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class Specs {
	
	private List<String> mac;
	
	private List<String> ip;
	
	private Float cpu;
	
	private String id;
	
	private Long disk;

	private Long memory;
	
	/**
	 * @return the memory
	 */
	public Long getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(Long memory) {
		this.memory = memory;
	}

	/**
	 * @return the mac
	 */
	public List<String> getMac() {
		return mac;
	}

	/**
	 * @param mac the mac to set
	 */
	public void setMac(List<String> mac) {
		this.mac = mac;
	}

	/**
	 * @return the ip
	 */
	public List<String> getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(List<String> ip) {
		this.ip = ip;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the disk
	 */
	public Long getDisk() {
		return disk;
	}

	/**
	 * @param disk the disk to set
	 */
	public void setDisk(Long disk) {
		this.disk = disk;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Specs [memory=" + memory + ", mac=" + mac + ", ip=" + ip
				+ ", cpu=" + cpu + ", id=" + id + ", disk=" + disk + "]";
	}
}
