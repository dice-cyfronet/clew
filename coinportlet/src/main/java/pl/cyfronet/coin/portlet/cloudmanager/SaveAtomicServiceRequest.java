package pl.cyfronet.coin.portlet.cloudmanager;

import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotEmpty;


public class SaveAtomicServiceRequest {
	@NotEmpty
	private String atomicServiceInstanceId;
	@NotEmpty
	private String name;
	private String description;
	private String descriptionEndpoint;
	@NotEmpty
	private String invocationName;
	@NotEmpty
	private String invocationEndpoint;
	@NotEmpty @DecimalMin("1")
	private String invocationPort;
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
	public String getInvocationPort() {
		return invocationPort;
	}
	public void setInvocationPort(String invocationPort) {
		this.invocationPort = invocationPort;
	}
	public String getInvocationName() {
		return invocationName;
	}
	public void setInvocationName(String invocationName) {
		this.invocationName = invocationName;
	}
}