package pl.cyfronet.coin.webdav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.cyfronet.coin.portlet.lobcder.LobcderRestClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicLobcderRestTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderRestTest.class);
	
	@Autowired private LobcderRestClient lobcderRestClient;
	
	@Test
	public void testMetadataRetrieval() {
		LobcderRestMetadata metadata = lobcderRestClient.getMetadata("/");
		log.info("Retrieved metadata:" + metadata);
	}
}