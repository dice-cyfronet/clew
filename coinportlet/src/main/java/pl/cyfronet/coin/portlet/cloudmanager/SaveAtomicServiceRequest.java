package pl.cyfronet.coin.portlet.cloudmanager;

import org.hibernate.validator.constraints.NotEmpty;


public class SaveAtomicServiceRequest {
	@NotEmpty private String atomicServiceInstanceId;
	@NotEmpty private String name;
	private String description;
	private boolean shared;
	private boolean published;
	private boolean scalable;
	private String proxyConfiguration;
	private String atomicServiceId;
	private String cores;
	private String disk;
	private String memory;
	
	public String getCores() {
		return cores;
	}
	public void setCores(String cores) {
		this.cores = cores;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAtomicServiceInstanceId() {
		return atomicServiceInstanceId;
	}
	public void setAtomicServiceInstanceId(String atomicServiceInstanceId) {
		this.atomicServiceInstanceId = atomicServiceInstanceId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isScalable() {
		return scalable;
	}
	public void setScalable(boolean scalable) {
		this.scalable = scalable;
	}
	public String getProxyConfiguration() {
		return proxyConfiguration;
	}
	public void setProxyConfiguration(String proxyConfiguration) {
		this.proxyConfiguration = proxyConfiguration;
	}
	public String getAtomicServiceId() {
		return atomicServiceId;
	}
	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}
}