package pl.cyfronet.coin.webdav;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.Status;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.PropPatchMethod;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicWebDavTest {
	private static final Logger log = LoggerFactory.getLogger(BasicLobcderTest.class);

	@Value("${lobcder.user}") private String webDavUser;
	@Value("${lobcder.password}") private String webDavPassword;
	@Value("${lobcder.url}") private String webDavUrl;

	private HttpClient client;

	@Before
	public void setup() throws MalformedURLException {
		HostConfiguration hostConfig = new HostConfiguration();
		URL url = new URL(webDavUrl);
		hostConfig.setHost(url.getHost(), url.getPort());

		HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		int maxHostConnections = 20;
		params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
		connectionManager.setParams(params);

		Credentials creds = new UsernamePasswordCredentials(webDavUser, webDavPassword);
		client = new HttpClient(connectionManager);
		client.getState().setCredentials(AuthScope.ANY, creds);
		client.setHostConfiguration(hostConfig);
	}

	@Ignore("Awaiting fixed on the webdav server side")
	@Test
	public void addProperties() throws IOException, DavException {
		DavPropertySet setProperties = new DavPropertySet();
		setProperties.add(new DefaultDavProperty<Boolean>("dri-supervised", true,
				Namespace.getNamespace("c", "custom:")));
		setProperties.add(new DefaultDavProperty<String>("dri-checksum", "checksum",
				Namespace.getNamespace("c", "custom:")));
		setProperties.add(new DefaultDavProperty<String>("dri-last-validation-date-ms", "validationDate",
				Namespace.getNamespace("c", "custom:")));
		
		DavMethod propPatch = new PropPatchMethod(webDavUrl + "/test/", setProperties,
				new DavPropertyNameSet());
		client.executeMethod(propPatch);
		
		MultiStatus multiStatus = propPatch.getResponseBodyAsMultiStatus();
		MultiStatusResponse[] responses = multiStatus.getResponses();

		for (int i = 0; i < responses.length; i++) {
			MultiStatusResponse response = responses[i];
			
			for(Status status : response.getStatus()) {
				log.info("Status: {} for ", status.getStatusCode());
			}
		}
	}

	@Test
	public void testPropfind() throws IOException, DavException {
		DavMethod pFind = new PropFindMethod(webDavUrl,
				DavConstants.PROPFIND_ALL_PROP, DavConstants.DEPTH_INFINITY);
		client.executeMethod(pFind);

		MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();
		MultiStatusResponse[] responses = multiStatus.getResponses();

		for (int i = 0; i < responses.length; i++) {
			MultiStatusResponse response = responses[i];
			log.info("Resource: {}", response.getHref());
			
			for(Status status : response.getStatus()) {
				log.info("\tStatus code: {}", status.getStatusCode());
				
				StringBuilder props = new StringBuilder();
				
				for(DavPropertyName propName : response.getProperties(status.getStatusCode()).
						getPropertyNames()) {
					props.append(propName.getNamespace().getURI()).
							append(propName.getNamespace().getPrefix()).append(":").
							append(propName.getName()).append(", ");
				}
				
				log.info("\t\t{}", props.toString());
			}
		}
	}
}