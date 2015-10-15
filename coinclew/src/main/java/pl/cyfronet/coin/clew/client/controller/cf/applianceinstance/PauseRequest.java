package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

public class PauseRequest {
	private String pause;
	
	public PauseRequest() {
		setPause("");
	}

	public String getPause() {
		return pause;
	}

	public void setPause(String pause) {
		this.pause = pause;
	}
}