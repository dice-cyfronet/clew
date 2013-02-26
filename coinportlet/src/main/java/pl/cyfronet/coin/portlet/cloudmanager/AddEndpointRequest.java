package pl.cyfronet.coin.portlet.cloudmanager;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import pl.cyfronet.coin.api.beans.EndpointType;

public class AddEndpointRequest {
	@NotNull private EndpointType type;
	@NotEmpty private String invocationPath;
	@NotEmpty private int port;
	@NotEmpty private String serviceName;
	private String description;
	private String descriptor;
	private String atomicServiceInstanceId;
	private String workflowId;
	
	public EndpointType getType() {
		return type;
	}
	public void setType(EndpointType type) {
		this.type = type;
	}
	public String getInvocationPath() {
		return invocationPath;
	}
	public void setInvocationPath(String invocationPath) {
		this.invocationPath = invocationPath;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public String toString() {
		return "AddEndpointRequest [type=" + type + ", invocationPath="
				+ invocationPath + ", port=" + port + ", serviceName="
				+ serviceName + ", description=" + description
				+ ", descriptor=" + descriptor + ", atomicServiceInstanceId="
				+ atomicServiceInstanceId + ", workflowId=" + workflowId + "]";
	}
	
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
}