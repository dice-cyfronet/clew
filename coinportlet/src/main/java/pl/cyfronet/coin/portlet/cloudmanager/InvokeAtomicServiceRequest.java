package pl.cyfronet.coin.portlet.cloudmanager;

public class InvokeAtomicServiceRequest {
	private String method;
	private String messageBody;
	private String atomicServiceInstanceId;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getAtomicServiceInstanceId() {
		return atomicServiceInstanceId;
	}
	public void setAtomicServiceInstanceId(String atomicServiceInstanceId) {
		this.atomicServiceInstanceId = atomicServiceInstanceId;
	}
}