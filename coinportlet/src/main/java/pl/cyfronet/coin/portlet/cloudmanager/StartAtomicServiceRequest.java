package pl.cyfronet.coin.portlet.cloudmanager;

public class StartAtomicServiceRequest {
	private String atomicServiceId;
	private String atomicServiceName;
	private String requestedStorageInGigaBytes;
	
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
	public String getRequestedStorageInGigaBytes() {
		return requestedStorageInGigaBytes;
	}
	public void setRequestedStorageInGigaBytes(String requestedStorageInGigaBytes) {
		this.requestedStorageInGigaBytes = requestedStorageInGigaBytes;
	}
	
	@Override
	public String toString() {
		return "StartAtomicServiceRequest [atomicServiceId=" + atomicServiceId
				+ ", atomicServiceName=" + atomicServiceName
				+ ", requestedStorageInGigaBytes="
				+ requestedStorageInGigaBytes + "]";
	}
}