package pl.cyfronet.coin.portlet.lobcder;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "logicalDatas")
public class LobcderRestMetadataList {
	private List<LobcderRestMetadata> metadataList;

	@XmlElement(name = "logicalData")
	public List<LobcderRestMetadata> getMetadataList() {
		return metadataList;
	}
	public void setMetadataList(List<LobcderRestMetadata> metadataList) {
		this.metadataList = metadataList;
	}
	
	@Override
	public String toString() {
		return "LobcderRestMetadataList [metadataList=" + metadataList + "]";
	}
}