package pl.cyfronet.coin.webdav;

import java.io.ByteArrayInputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.cyfronet.coin.portlet.lobcder.LobcderClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderException;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicLobcderRestTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderRestTest.class);
	
	private static final String TEST_DIR_NAME = BasicLobcderRestTest.class.getSimpleName();
	private static final String TEST_FILE_NAME = "test.txt";
	
	@Autowired private LobcderRestClient lobcderRestClient;
	@Autowired private LobcderClient client;
	
	@Value("${security.token}") private String securityToken;
	
	@Before
	public void prepareTestDirectory() throws LobcderException {
		client.createDirectory("/", TEST_DIR_NAME, securityToken);
		client.put(TEST_DIR_NAME, TEST_FILE_NAME, new ByteArrayInputStream("hello".getBytes()), securityToken);
	}
	
	@After
	public void cleanUp() throws LobcderException {
//		client.delete("/" + TEST_DIR_NAME, securityToken);
	}
	
	@Test(expected = LobcderException.class)
	public void testMetadataRetrievalForRoot() throws LobcderException {
		lobcderRestClient.getMetadata("/", securityToken);
	}
	
	@Test
	public void testFetchingMetadata() throws LobcderException {
		LobcderRestMetadata metadata = lobcderRestClient.getMetadata("/" + TEST_DIR_NAME + "/" + TEST_FILE_NAME, securityToken);
		log.info("Metadata: " + metadata);
		Assert.assertNotNull(metadata);
	}
}