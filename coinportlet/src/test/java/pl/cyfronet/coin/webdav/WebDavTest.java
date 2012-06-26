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
import org.junit.Test;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

public class WebDavTest {
	@Test
	public void connectAndList() throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException {
		Sardine sardine = SardineFactory.begin("todo", "todo", createNaiveSslSocketFactory());
//		List<DavResource> resources = sardine.getResources("https://149.156.10.138:8444/lobcder-1.1-SNAPSHOT");
		List<DavResource> resources = sardine.getResources("http://149.156.10.138:8080/lobcder-1.0-SNAPSHOT");
		
		for (DavResource res : resources) {
		     System.out.println(res);
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