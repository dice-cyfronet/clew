package pl.cyfronet.coin.webdav;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.junit.Test;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;

public class WebDavTest {
	@Test
	public void connectAndList() throws HttpException, IOException {
		Sardine sardine = SardineFactory.begin("login", "password");
		List<DavResource> resources = sardine.getResources("http://yourdavserver.com/adirectory/");
		
		for (DavResource res : resources) {
		     System.out.println(res);
		}
	}
}