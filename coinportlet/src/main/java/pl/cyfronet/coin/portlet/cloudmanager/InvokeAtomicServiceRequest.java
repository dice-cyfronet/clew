package pl.cyfronet.coin.portlet.cloudmanager;

import java.util.List;

public class InvokeAtomicServiceRequest {
	private String method;
	private String atomicServiceInstanceId;
	private String workflowId;
	private String configurationId;
	private String atomicServiceId;
	private List<FormField> formFields;
	private String invocationPath;
	private String serviceId;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
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
	public String getConfigurationId() {
		return configurationId;
	}
	public void setConfigurationId(String configurationId) {
		this.configurationId = configurationId;
	}
	public String getAtomicServiceId() {
		return atomicServiceId;
	}
	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}
	public List<FormField> getFormFields() {
		return formFields;
	}
	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
	public String getInvocationPath() {
		return invocationPath;
	}
	public void setInvocationPath(String invocationPath) {
		this.invocationPath = invocationPath;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	@Override
	public String toString() {
		return "InvokeAtomicServiceRequest [method=" + method
				+ ", atomicServiceInstanceId=" + atomicServiceInstanceId
				+ ", workflowId=" + workflowId + ", configurationId="
				+ configurationId + ", atomicServiceId=" + atomicServiceId
				+ ", formFields=" + formFields + ", invocationPath="
				+ invocationPath + ", serviceId=" + serviceId + "]";
	}
}