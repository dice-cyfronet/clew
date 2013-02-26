package pl.cyfronet.coin.portlet.cloudmanager;

import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotEmpty;

import pl.cyfronet.coin.api.RedirectionType;

public class AddRedirectionRequest {
	private String atomicServiceInstanceId;
	private String workflowId;
	@NotEmpty private String name;
	@DecimalMin("1") private int toPort;
	private RedirectionType type;
	
	public String getAtomicServiceInstanceId() {
		return atomicServiceInstanceId;
	}
	public void setAtomicServiceInstanceId(String atomicServiceInstanceId) {
		this.atomicServiceInstanceId = atomicServiceInstanceId;
	}
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getToPort() {
		return toPort;
	}
	public void setToPort(int toPort) {
		this.toPort = toPort;
	}
	public RedirectionType getType() {
		return type;
	}
	public void setType(RedirectionType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "AddRedirectionRequest [atomicServiceInstanceId="
				+ atomicServiceInstanceId + ", workflowId=" + workflowId
				+ ", name=" + name + ", toPort=" + toPort + ", type=" + type
				+ "]";
	}	
}