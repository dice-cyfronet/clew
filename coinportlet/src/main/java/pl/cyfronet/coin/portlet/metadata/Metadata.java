package pl.cyfronet.coin.portlet.metadata;

import pl.cyfronet.coin.portlet.lobcder.LobcderWebDavMetadata;

public class Metadata {
	private LobcderWebDavMetadata lobcderWebDavMetadata;

	public LobcderWebDavMetadata getLobcderWebDavMetadata() {
		return lobcderWebDavMetadata;
	}
	public void setLobcderWebDavMetadata(LobcderWebDavMetadata lobcderWebDavMetadata) {
		this.lobcderWebDavMetadata = lobcderWebDavMetadata;
	}
	
	@Override
	public String toString() {
		return "Metadata [lobcderWebDavMetadata=" + lobcderWebDavMetadata + "]";
	}
}