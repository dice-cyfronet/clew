package pl.cyfronet.coin.impl.air.client;

public class VmHttpRedirection {
	
	private Integer vm_port;
	private String url;
	
	public Integer getVm_port() {
		return vm_port;
	}
	
	public void setVm_port(Integer vm_port) {
		this.vm_port = vm_port;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "VmHttpRedirection [vm_port=" + vm_port + ", url=" + url + "]";
	}
}
