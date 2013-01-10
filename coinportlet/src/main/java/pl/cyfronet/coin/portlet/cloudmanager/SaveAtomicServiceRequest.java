package pl.cyfronet.coin.portlet.cloudmanager;

import org.hibernate.validator.constraints.NotEmpty;


public class SaveAtomicServiceRequest {
	@NotEmpty private String atomicServiceInstanceId;
	@NotEmpty private String name;
	private String description;
	
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
}