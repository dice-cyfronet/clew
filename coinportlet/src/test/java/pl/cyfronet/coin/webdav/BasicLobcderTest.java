package pl.cyfronet.coin.webdav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.cyfronet.coin.portlet.lobcder.LobcderClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderEntry;
import pl.cyfronet.coin.portlet.lobcder.LobcderException;
import pl.cyfronet.coin.portlet.lobcder.LobcderWebDavMetadata;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicLobcderTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderTest.class);
	
	@Value("${lobcder.url}") private String webDavUrl;
	
	@Autowired private LobcderClient lobcder;
	
	@BeforeClass
	public static void checkIntegrationTestFlag() {
		Assume.assumeTrue(Boolean.getBoolean("integration.tests"));
	}
	
	@Test
	public void sortTest() {
		List<LobcderEntry> entries = new ArrayList<>();
		
		LobcderEntry file = new LobcderEntry("file");
		entries.add(file);
		
		LobcderEntry dir = new LobcderEntry("w");
		dir.setDirectory(true);
		entries.add(dir);
		
		LobcderEntry dir2 = new LobcderEntry("p");
		dir2.setDirectory(true);
		entries.add(dir2);
		sortLobcderEntries(entries);
		log.info("" + entries);
		Assert.assertTrue(entries.get(0).getName().equals("p"));
		Assert.assertTrue(entries.get(1).getName().equals("w"));
	}
	
	@Test
	public void connectAndList() throws LobcderException {
		log.info("Connecting to LOBCDER service at {}", webDavUrl);
		List<LobcderEntry> entries = lobcder.list("/");
		
		for(LobcderEntry entry : entries) {
		     log.info(entry.toString());
		}
		
		Assert.assertNotNull(entries);
		//the current directory should not be returned
		Assert.assertTrue(!entries.contains(new LobcderEntry("/")));
	}
	
	@Test
	public void metadataTest() throws LobcderException {
		String testDirName = String.valueOf(System.currentTimeMillis());
		lobcder.createDirectory("/", testDirName);
		
		try {
			LobcderWebDavMetadata metadata = new LobcderWebDavMetadata();
			metadata.setDriChecksum(500);
			metadata.setDriLastValidationDateMs(600);
			metadata.setDriSupervised(true);
			metadata.setCreationDate("should not be changed");
			metadata.setModificationDate("should not be changed");
			metadata.setFormat("new format");
			lobcder.updateMetadata("/" + testDirName, metadata);
		
			LobcderWebDavMetadata retrievedMetadata = lobcder.getMetadata("/" + testDirName);
			Assert.assertEquals(metadata.getDriChecksum(), retrievedMetadata.getDriChecksum());
			Assert.assertEquals(metadata.getDriLastValidationDateMs(), retrievedMetadata.getDriLastValidationDateMs());
			Assert.assertEquals(metadata.isDriSupervised(), retrievedMetadata.isDriSupervised());
			Assert.assertTrue(!metadata.getCreationDate().equals(retrievedMetadata.getCreationDate()));
			Assert.assertTrue(!metadata.getModificationDate().equals(retrievedMetadata.getModificationDate()));
			Assert.assertTrue(!metadata.getFormat().equals(retrievedMetadata.getFormat()));
		} finally {
			lobcder.delete("/" + testDirName);
		}
	}
	
	private void sortLobcderEntries(List<LobcderEntry> entries) {
		Collections.sort(entries, new Comparator<LobcderEntry>() {
			@Override
			public int compare(LobcderEntry le1, LobcderEntry le2) {
				if(le1.isDirectory() == le2.isDirectory()) {
					return le1.getName().compareTo(le2.getName());
				} else {
					if(le1.isDirectory()) {
						return Integer.MIN_VALUE;
					} else {
						return Integer.MAX_VALUE;
					}
				}
			}
		});
	}
}