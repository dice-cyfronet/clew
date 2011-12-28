package pl.cyfronet.coin.portlet.util;

public class ContextIdFactory {
	private String contextIdPrefix;
	
	public ContextIdFactory(String contextIdPrefix) {
		this.contextIdPrefix = contextIdPrefix;
	}
	
	public String createContextId(String userName) {
		return contextIdPrefix + "_" + userName;
	}
}