package pl.cyfronet.coin.portlet.cloudmanager;

import pl.cyfronet.coin.api.beans.WorkflowType;

public class StartAtomicServiceRequest {
	private String atomicServiceId;
	private String atomicServiceName;
	private WorkflowType workflowType;
	private String userKeyId;
	private String atomicServiceInstanceName;
	
	public String getAtomicServiceId() {
		return atomicServiceId;
	}
	public void setAtomicServiceId(String atomicServiceId) {
		this.atomicServiceId = atomicServiceId;
	}
	public String getAtomicServiceName() {
		return atomicServiceName;
	}
	public void setAtomicServiceName(String atomicServiceName) {
		this.atomicServiceName = atomicServiceName;
	}
	public WorkflowType getWorkflowType() {
		return workflowType;
	}
	public void setWorkflowType(WorkflowType workflowType) {
		this.workflowType = workflowType;
	}
	public String getUserKeyId() {
		return userKeyId;
	}
	public void setUserKeyId(String userKeyId) {
		this.userKeyId = userKeyId;
	}
	
	@Override
	public String toString() {
		return "StartAtomicServiceRequest [atomicServiceId=" + atomicServiceId
				+ ", atomicServiceName=" + atomicServiceName
				+ ", workflowType=" + workflowType + ", userKeyId=" + userKeyId
				+ ", atomicServiceInstanceName=" + atomicServiceInstanceName
				+ "]";
	}
	
	public String getAtomicServiceInstanceName() {
		return atomicServiceInstanceName;
	}
	public void setAtomicServiceInstanceName(String atomicServiceInstanceName) {
		this.atomicServiceInstanceName = atomicServiceInstanceName;
	}
}