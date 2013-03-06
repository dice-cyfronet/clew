package pl.cyfronet.coin.webdav;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

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
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadataPermissions;

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
		client.delete("/" + TEST_DIR_NAME, securityToken);
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
	
	@Test
	public void setPermissions() throws LobcderException {
		LobcderRestMetadata metadata = lobcderRestClient.getMetadata("/" + TEST_DIR_NAME + "/" + TEST_FILE_NAME, securityToken);
		metadata.getPermissions().getReadGroups().add("testing_group");
		lobcderRestClient.updateMetadata(metadata, securityToken);
		metadata = lobcderRestClient.getMetadata("/" + TEST_DIR_NAME + "/" + TEST_FILE_NAME, securityToken);
		Assert.assertTrue(metadata.getPermissions().getReadGroups().contains("testing_group"));
	}
	
	@Test
	public void permissionSerialization() throws JAXBException {
		LobcderRestMetadataPermissions perms = new LobcderRestMetadataPermissions();
		perms.setOwner("owner");
		perms.setReadGroups(Arrays.asList(new String[] {"group1", "group2"}));
		perms.setWriteGroups(Arrays.asList(new String[] {"group3", "group4"}));
		
		JAXBContext jaxb = JAXBContext.newInstance(LobcderRestMetadataPermissions.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxb.createMarshaller().marshal(perms, baos);
		log.info("Serialized permissions: {}", baos.toString());
	}
	
	@Test
	public void metadataSerialization() throws JAXBException {
		LobcderRestMetadataPermissions perms = new LobcderRestMetadataPermissions();
		perms.setOwner("owner");
		perms.setReadGroups(Arrays.asList(new String[] {"group1", "group2"}));
		perms.setWriteGroups(Arrays.asList(new String[] {"group3", "group4"}));
		
		LobcderRestMetadata metadata = new LobcderRestMetadata();
		metadata.setPermissions(perms);
		metadata.setName("name");
		
		JAXBContext jaxb = JAXBContext.newInstance(LobcderRestMetadata.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		jaxb.createMarshaller().marshal(metadata, baos);
		log.info("Serialized permissions: {}", baos.toString());
	}
}