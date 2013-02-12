package pl.cyfronet.coin.webdav;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.cyfronet.coin.portlet.lobcder.LobcderClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderException;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadata;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadataList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicLobcderRestTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderRestTest.class);
	
	private static final String TEST_DIR_NAME = BasicLobcderRestTest.class.getSimpleName();
	
	@Autowired private LobcderRestClient lobcderRestClient;
	@Autowired private LobcderClient client;
	
	@Before
	public void prepareTestDirectory() throws LobcderException {
		client.createDirectory("/", TEST_DIR_NAME);
	}
	
	@After
	public void cleanUp() throws LobcderException {
		client.delete("/" + TEST_DIR_NAME);
	}
	
	@Test(expected = LobcderException.class)
	public void testMetadataRetrievalForRoot() throws LobcderException {
		lobcderRestClient.getMetadata("/");
	}
	
	@Test
	@Ignore("Waiting for LOBCDER to get stable")
	public void testFetchingMetadata() throws LobcderException {
//		LobcderRestMetadata metadata = lobcderRestClient.getMetadata("/" + TEST_DIR_NAME);
		LobcderRestMetadata metadata = lobcderRestClient.getMetadata("/testdir/test.docx");
		log.info("Metadata: " + metadata);
		Assert.assertNotNull(metadata);
	}
	
	@Test
	public void testJson() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector ai = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().setAnnotationIntrospector(ai);
		mapper.getSerializationConfig().setAnnotationIntrospector(ai);
		
		LobcderRestMetadataList o = new LobcderRestMetadataList();
		o.setMetadataList(new ArrayList<LobcderRestMetadata>());
		
		LobcderRestMetadata d = new LobcderRestMetadata();
		d.setUid("uid");
		o.getMetadataList().add(d);
		log.info("Out: " + mapper.writeValueAsString(o));
	}
}