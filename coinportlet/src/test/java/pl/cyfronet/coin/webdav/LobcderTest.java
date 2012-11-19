package pl.cyfronet.coin.webdav;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.junit.Ignore;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class LobcderTest {
	private static final Logger log = LoggerFactory.getLogger(LobcderTest.class);
	
	@Value("${lobcder.user}") private String webDavUser;
	@Value("${lobcder.password}") private String webDavPassword;
	@Value("${lobcder.url}") private String webDavUrl;
	
	@Test
	@Ignore
	public void connectAndList() throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException {
		log.info("Connecting to LOBCDER service at {}", webDavUrl);
		Sardine sardine = SardineFactory.begin(webDavUser, webDavPassword, createNaiveSslSocketFactory());
		List<DavResource> resources = sardine.getResources(webDavUrl);
		
		for (DavResource res : resources) {
		     log.info(res.toString());
		}
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