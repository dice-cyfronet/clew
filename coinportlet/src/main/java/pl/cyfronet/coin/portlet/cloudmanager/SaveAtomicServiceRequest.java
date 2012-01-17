package pl.cyfronet.coin.portlet.cloudmanager;

public class SaveAtomicServiceRequest {
	private String atomicServiceInstanceId;
	private String name;
	private String description;
	private String descriptionEndpoint;
	private String invocationEndpoint;
	private String ports;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescriptionEndpoint() {
		return descriptionEndpoint;
	}
	public void setDescriptionEndpoint(String descriptionEndpoint) {
		this.descriptionEndpoint = descriptionEndpoint;
	}
	public String getInvocationEndpoint() {
		return invocationEndpoint;
	}
	public void setInvocationEndpoint(String invocationEndpoint) {
		this.invocationEndpoint = invocationEndpoint;
	}
	public String getPorts() {
		return ports;
	}
	public void setPorts(String ports) {
		this.ports = ports;
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