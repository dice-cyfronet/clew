package pl.cyfronet.coin.webdav;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.DavConstants;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.Status;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.PropPatchMethod;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.property.DefaultDavProperty;
import org.apache.jackrabbit.webdav.xml.Namespace;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.cyfronet.coin.portlet.util.HttpUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/coinportlet-app-ctx.xml")
public class BasicWebDavTest {
	private static final Logger log = LoggerFactory.getLogger(BasicWebDavTest.class);

	@Value("${lobcder.url}") private String webDavUrl;
	@Value("${security.token}") private String securityToken;
	
	@Autowired private HttpUtil httpUtil;

	private HttpClient client;
	private String testFolder;
	
	@BeforeClass
	public static void checkIntegrationTestFlag() {
		Assume.assumeTrue(Boolean.getBoolean("integration.tests"));
	}

	@Before
	public void setup() throws HttpException, IOException {
		HostConfiguration hostConfig = new HostConfiguration();
		URL url = new URL(webDavUrl);
		hostConfig.setHost(url.getHost(), url.getPort());

		HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		int maxHostConnections = 20;
		params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
		connectionManager.setParams(params);
		client = new HttpClient(connectionManager);
		client.setHostConfiguration(hostConfig);
		
		//create test folder
		testFolder = webDavUrl + "/" + String.valueOf(System.currentTimeMillis());
		MkColMethod mkdirMethod = new MkColMethod(testFolder);
		mkdirMethod.addRequestHeader("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		client.executeMethod(mkdirMethod);
	}
	
	@After
	public void clean() throws HttpException, IOException {
		DeleteMethod deleteMethod = new DeleteMethod(testFolder);
		deleteMethod.addRequestHeader("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		client.executeMethod(deleteMethod);
	}

	@Test
	public void addProperties() throws IOException, DavException {
		DavPropertySet setProperties = new DavPropertySet();
		setProperties.add(new DefaultDavProperty<String>("dri-supervised", "true",
				Namespace.getNamespace("c", "custom:")));
		setProperties.add(new DefaultDavProperty<String>("dri-checksum", "2500",
				Namespace.getNamespace("c", "custom:")));
		setProperties.add(new DefaultDavProperty<String>("dri-last-validation-date-ms", "100",
				Namespace.getNamespace("c", "custom:")));
		setProperties.add(new DefaultDavProperty<String>(DavPropertyName.GETCONTENTTYPE, "aaa"));
		
		DavMethod propPatch = new PropPatchMethod(testFolder, setProperties,
				new DavPropertyNameSet());
		propPatch.addRequestHeader("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		client.executeMethod(propPatch);
	}

	@Test
	public void testPropfind() throws IOException, DavException {
		DavPropertyNameSet properties = new DavPropertyNameSet();
		properties.add("dri-checksum", Namespace.getNamespace("c", "custom:"));
		properties.add(DavPropertyName.CREATIONDATE);
		properties.add(DavPropertyName.GETLASTMODIFIED);
		properties.add(DavPropertyName.GETCONTENTTYPE);
		
		DavMethod pFind = new PropFindMethod(testFolder,
				properties, DavConstants.DEPTH_0);
		pFind.addRequestHeader("Authorization", httpUtil.createBasicAuthenticationHeaderValue(null, securityToken));
		client.executeMethod(pFind);

		MultiStatus multiStatus = pFind.getResponseBodyAsMultiStatus();
		MultiStatusResponse[] responses = multiStatus.getResponses();

		for (int i = 0; i < responses.length; i++) {
			MultiStatusResponse response = responses[i];
			log.info("Resource: {}", response.getHref());
			
			for(Status status : response.getStatus()) {
				log.info("\tStatus code: {}", status.getStatusCode());
				
				StringBuilder props = new StringBuilder();
				props.append("\n");
				
				for(DavPropertyName propName : response.getProperties(status.getStatusCode()).
						getPropertyNames()) {
					Object value = null;
					
					if(response.getProperties(200).get(propName) != null) {
						value = response.getProperties(200).get(propName).getValue();
					}
					
					props.append(propName.getNamespace().getURI()).
							append(propName.getNamespace().getPrefix()).append(":").
							append(propName.getName()).append(" - ").append(value).append("\n");
				}
				
				log.info("{}", props.toString());
			}
		}
	}
}