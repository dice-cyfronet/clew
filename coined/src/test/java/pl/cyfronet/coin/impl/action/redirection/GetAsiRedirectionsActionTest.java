package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.beans.redirection.HttpRedirection;
import pl.cyfronet.coin.api.beans.redirection.NatRedirection;
import pl.cyfronet.coin.api.beans.redirection.Redirections;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.Specs;
import pl.cyfronet.coin.impl.air.client.VmHttpRedirection;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class GetAsiRedirectionsActionTest extends ActionTest {

	private String username = "marek";
	private String contextId = "ctx";
	private String asiId = "asiId";

	private String httpName = "http";
	private Integer httpPort = 80;
	private String httpId = "httpId";
	private String httpUrl = "http://proxy/http";

	private String httpsName = "https";
	private Integer httpsPort = 443;
	private String httpsId = "httpsId";
	private String httpsUrl = "https://proxy/http";

	private String natName = "ssh";
	private Integer natPort = 22;
	private String natId = "httpsId";

	private Redirections redirections;
	private String directHttpUrl = "http://149.156.8.34:80";
	private String directHttpsUrl = "https://149.156.8.34:443";
	private String headnodeIp = "149.156.10.133";
	private int headnodePort = 12345;
	private Integer bothPort = 81;
	private String bothName = "both";
	private String bothId ="bothId";
	private String publicIp = "149.156.8.34";
	private int publicPort = 24;
	private String publicNatName = "public";

	@Test
	public void shouldGetHttpRedirectionsForVmWithPrivateIp() throws Exception {
		givenWorkflowAsiWithPrivateIp();
		whenGetRedirections();
		thenRedirectionWithProxyUrlCreated();
	}

	private void givenWorkflowAsiWithPrivateIp() {
		givenApplianceType();
		Vms vm = givenWorflowWithOneVm(WorkflowType.development);

		VmHttpRedirection httpRedirection = new VmHttpRedirection();
		httpRedirection.setUrl(httpUrl);
		httpRedirection.setVm_port(httpPort);

		VmHttpRedirection httpsRedirection = new VmHttpRedirection();
		httpsRedirection.setUrl(httpsUrl);
		httpsRedirection.setVm_port(httpsPort);

		vm.setHttp_redirections(Arrays
				.asList(httpRedirection, httpsRedirection));
	}

	private void givenApplianceType() {
		ApplianceType applType = new ApplianceType();

		ATPortMapping httpMapping = new ATPortMapping();
		httpMapping.setPort(httpPort);
		httpMapping.setService_name(httpName);
		httpMapping.setHttp(true);
		httpMapping.setId(httpId);

		ATPortMapping httpsMapping = new ATPortMapping();
		httpsMapping.setPort(httpsPort);
		httpsMapping.setService_name(httpsName);
		httpsMapping.setHttps(true);
		httpsMapping.setId(httpsId);

		ATPortMapping natMapping = new ATPortMapping();
		natMapping.setPort(natPort);
		natMapping.setService_name(natName);
		natMapping.setId(natId);

		applType.setPort_mappings(Arrays.asList(httpMapping, httpsMapping,
				natMapping));

		when(air.getTypeFromVM(asiId)).thenReturn(applType);
	}

	private Vms givenWorflowWithOneVm(WorkflowType type) {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setId(contextId);
		wd.setVph_username(username);
		wd.setWorkflow_type(type);

		Vms vm = new Vms();
		vm.setAppliance_type("type1");
		vm.setAppliance_type_name("type1 name");
		vm.setName("vm1");
		if (type == WorkflowType.development) {
			vm.setVms_id(asiId);
		} else {
			vm.setConfiguration(asiId);
		}

		Specs specs = new Specs();
		specs.setIp(Arrays.asList("10.100.8.34", publicIp));

		vm.setSpecs(specs);

		wd.setVms(Arrays.asList(vm));

		when(air.getWorkflow(contextId)).thenReturn(wd);

		return vm;
	}

	private void whenGetRedirections() {
		Action<Redirections> action = actionFactory
				.createGetAsiRedirectionsAction(username, contextId, asiId);
		redirections = action.execute();
	}

	private void thenRedirectionWithProxyUrlCreated() {
		thenRedirectionWithUrlCreated(httpUrl, httpsUrl);
	}

	private void thenRedirectionWithUrlCreated(String r1Url, String r2Url) {
		List<HttpRedirection> http = redirections.getHttp();
		assertNotNull(http);
		assertEquals(http.size(), 2);

		HttpRedirection httpRedirection = http.get(0);
		assertEquals(httpRedirection.getId(), httpId);
		assertEquals(httpRedirection.getName(), httpName);
		assertEquals(httpRedirection.getToPort(), httpPort);
		assertEquals(httpRedirection.getUrls().size(), 1);
		assertEquals(httpRedirection.getUrls().get(0), r1Url);

		HttpRedirection httpsRedirection = http.get(1);
		assertEquals(httpsRedirection.getId(), httpsId);
		assertEquals(httpsRedirection.getName(), httpsName);
		assertEquals(httpsRedirection.getToPort(), httpsPort);
		assertEquals(httpsRedirection.getUrls().size(), 1);
		assertEquals(httpsRedirection.getUrls().get(0), r2Url);
	}

	@Test
	public void shouldGetHttpRedirectionForVmWithPublicIp() throws Exception {
		givenWorkflowAsiWithPublicIp();
		whenGetRedirections();
		thenRedirectionWithDirectUrlCreated();
	}

	private void givenWorkflowAsiWithPublicIp() {
		givenApplianceType();
		givenWorflowWithOneVm(WorkflowType.development);
	}

	private void thenRedirectionWithDirectUrlCreated() {
		thenRedirectionWithUrlCreated(directHttpUrl, directHttpsUrl);
	}

	@Test
	public void shouldGetHttpAndHttpsRedirectionsForTheSamePort()
			throws Exception {
		givenAsiWithHttpAndHttpsRedirectionsIntoTheSamePort();
		whenGetRedirections();
		thenRedirectionWith2UrlsCreated();
	}

	private void givenAsiWithHttpAndHttpsRedirectionsIntoTheSamePort() {
		givenApplianceTypeWithHttpAndHttpsRedirectionIntoTheSamePort();
		Vms vm = givenWorflowWithOneVm(WorkflowType.development);

		VmHttpRedirection httpRedirection = new VmHttpRedirection();
		httpRedirection.setUrl(httpUrl);
		httpRedirection.setVm_port(bothPort);

		VmHttpRedirection httpsRedirection = new VmHttpRedirection();
		httpsRedirection.setUrl(httpsUrl);
		httpsRedirection.setVm_port(bothPort);

		vm.setHttp_redirections(Arrays
				.asList(httpRedirection, httpsRedirection));
	}

	private void givenApplianceTypeWithHttpAndHttpsRedirectionIntoTheSamePort() {
		ApplianceType applType = new ApplianceType();

		ATPortMapping httpAndHttpsMapping = new ATPortMapping();
		httpAndHttpsMapping.setPort(bothPort);
		httpAndHttpsMapping.setService_name(bothName);
		httpAndHttpsMapping.setHttp(true);
		httpAndHttpsMapping.setHttps(true);
		httpAndHttpsMapping.setId(bothId);
		
		applType.setPort_mappings(Arrays.asList(httpAndHttpsMapping));

		when(air.getTypeFromVM(asiId)).thenReturn(applType);		
	}

	private void thenRedirectionWith2UrlsCreated() {
		List<HttpRedirection> http = redirections.getHttp();
		assertNotNull(http);
		assertEquals(http.size(), 1);

		HttpRedirection httpRedirection = http.get(0);
		assertEquals(httpRedirection.getId(), bothId);
		assertEquals(httpRedirection.getName(), bothName);
		assertEquals(httpRedirection.getToPort(), bothPort);
		
		assertEquals(httpRedirection.getUrls().size(), 2);
		assertEquals(httpRedirection.getUrls().get(0), httpUrl);
		assertEquals(httpRedirection.getUrls().get(1), httpsUrl);
	}

	@Test
	public void shouldGetNatASIRedirections() throws Exception {
		givenWorkflowWithNatRedirection();
		whenGetRedirections();
		thenNatRedirectionsCreated();
	}

	private void givenWorkflowWithNatRedirection() {
		givenApplianceType();
		Vms vm = givenWorflowWithOneVm(WorkflowType.development);

		PortMapping pm = new PortMapping();
		pm.setHeadnode_ip(headnodeIp);
		pm.setHeadnode_port(headnodePort);
		pm.setHttp(false);
		pm.setService_name(natName);
		pm.setVm_port(natPort);

		PortMapping publicPm = new PortMapping();
		publicPm.setHeadnode_ip(publicIp);
		publicPm.setHeadnode_port(publicPort);
		publicPm.setHttp(false);
		publicPm.setService_name(publicNatName );
		publicPm.setVm_port(publicPort);

		vm.setInternal_port_mappings(Arrays.asList(pm, publicPm));
	}

	private void thenNatRedirectionsCreated() {
		List<NatRedirection> nat = redirections.getNat();
		assertNotNull(nat);
		assertEquals(nat.size(), 2);

		assertEquals(nat.get(0).getName(), natName);
		assertFalse(nat.get(0).isDirect());

		assertEquals(nat.get(1).getName(), publicNatName);
		assertTrue(nat.get(1).isDirect());
	}

	@Test
	public void shouldGetRedirectionForWorkflowInProduction() throws Exception {
		givenWorkflowInProduction();
		whenGetRedirections();
		thanEmptyRedirectionsReceived();
	}

	private void givenWorkflowInProduction() {
		givenApplianceType();
		Vms vm = givenWorflowWithOneVm(WorkflowType.portal);
		vm.setInternal_port_mappings(new ArrayList<PortMapping>());
		vm.setHttp_redirections(new ArrayList<VmHttpRedirection>());
	}

	private void thanEmptyRedirectionsReceived() {
		assertNotNull(redirections.getHttp());
		assertEquals(redirections.getHttp().size(), 0);

		assertNotNull(redirections.getNat());
		assertEquals(redirections.getNat().size(), 0);
	}
}
