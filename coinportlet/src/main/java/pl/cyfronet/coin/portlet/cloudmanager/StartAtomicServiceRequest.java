package pl.cyfronet.coin.portlet.cloudmanager;

public class StartAtomicServiceRequest {
	private String atomicServiceId;
	private String atomicServiceName;
	
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
	
	@Override
	public String toString() {
		return "StartAtomicServiceRequest [atomicServiceId=" + atomicServiceId
				+ ", atomicServiceName=" + atomicServiceName + "]";
	}
}