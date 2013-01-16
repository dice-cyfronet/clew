package pl.cyfronet.coin.portlet.lobcder;

public class LobcderWebDavMetadata {
	private boolean driSupervised;
	private String driChecksum;
	private String driLastValidationDateMs;
	
	public boolean isDriSupervised() {
		return driSupervised;
	}
	public void setDriSupervised(boolean driSupervised) {
		this.driSupervised = driSupervised;
	}
	public String getDriChecksum() {
		return driChecksum;
	}
	public void setDriChecksum(String driChecksum) {
		this.driChecksum = driChecksum;
	}
	public String getDriLastValidationDateMs() {
		return driLastValidationDateMs;
	}
	public void setDriLastValidationDateMs(String driLastValidationDateMs) {
		this.driLastValidationDateMs = driLastValidationDateMs;
	}
}