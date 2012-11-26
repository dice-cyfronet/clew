package pl.cyfronet.coin.webdav;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicLobcderTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderTest.class);
	
	@Value("${lobcder.user}") private String webDavUser;
	@Value("${lobcder.password}") private String webDavPassword;
	@Value("${lobcder.url}") private String webDavUrl;
	
	private Sardine sardine;
	
	@Before
	public void initialize() throws KeyManagementException, SardineException,
			NoSuchAlgorithmException {
		sardine = SardineFactory.begin(webDavUser, webDavPassword, createNaiveSslSocketFactory());
	}
	
	@Test
	public void connectAndList() throws SardineException {
		log.info("Connecting to LOBCDER service at {}", webDavUrl);
		List<DavResource> resources = sardine.getResources(webDavUrl);
		
		for (DavResource res : resources) {
		     log.info(res.toString());
		}
		
		Assert.assertNotNull(resources);
	}
	
	@Test
	public void createDirectory() throws SardineException {
		String directoryName = createLobcderDirectoryName(String.valueOf(System.currentTimeMillis()));
		sardine.createDirectory(directoryName);
		Assert.assertTrue(sardine.exists(directoryName));
		
		//let's clean up
		sardine.delete(directoryName);
		Assert.assertTrue(!sardine.exists(directoryName));
	}
	
	private String createLobcderDirectoryName(String name) {
		String baseUrl = webDavUrl;
		
		if(!webDavUrl.endsWith("/")) {
			baseUrl += "/";
		}
		
		return baseUrl + name;
	}

	private SSLSocketFactory createNaiveSslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSL");
		TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    }
		};
		sc.init(null, trustAllCerts, new SecureRandom());

		return new org.apache.http.conn.ssl.SSLSocketFactory(sc);
	}
}