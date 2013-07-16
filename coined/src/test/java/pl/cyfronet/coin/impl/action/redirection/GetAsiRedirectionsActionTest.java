package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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

	@Test
	public void shouldGetHttpRedirectionsForVmWithPrivateIp() throws Exception {
		 givenWorkflowAsiWithPrivateIp();
		 whenGetRedirections();
		 thenRedirectionWithProxyUrlCreated();
	}
	
	private void givenWorkflowAsiWithPrivateIp() {
		givenApplianceType();
		Vms vm = givenWorflowWithOneVm();
		
		VmHttpRedirection httpRedirection = new VmHttpRedirection();
		httpRedirection.setUrl(httpUrl);
		httpRedirection.setVm_port(httpPort);
		
		VmHttpRedirection httpsRedirection = new VmHttpRedirection();
		httpsRedirection.setUrl(httpsUrl);
		httpsRedirection.setVm_port(httpsPort);	
		
		vm.setHttp_redirections(Arrays.asList(httpRedirection, httpsRedirection));	
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
		
		applType.setPort_mappings(Arrays.asList(httpMapping, httpsMapping, natMapping));
		
		when(air.getTypeFromVM(asiId)).thenReturn(applType);
	}
	
	private Vms givenWorflowWithOneVm() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setId(contextId);
		wd.setVph_username(username);
		wd.setWorkflow_type(WorkflowType.development);
		
		Vms vm = new Vms();
		vm.setAppliance_type("type1");
		vm.setAppliance_type_name("type1 name");
		vm.setName("vm1");
		vm.setVms_id(asiId);
		
		Specs specs = new Specs();
		specs.setIp(Arrays.asList("10.100.8.34", "149.156.8.34"));
		
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
	
	private void thenRedirectionWithUrlCreated(String r1Url, String r2Url ) {
		List<HttpRedirection> http = redirections.getHttp();
		assertNotNull(http);
		assertEquals(http.size(), 2);
		
		HttpRedirection httpRedirection = http.get(0);
		assertEquals(httpRedirection.getId(), httpId);
		assertEquals(httpRedirection.getName(), httpName);
		assertEquals(httpRedirection.getToPort(), httpPort);
		assertEquals(httpRedirection.getUrl(), r1Url);
		
		
		HttpRedirection httpsRedirection = http.get(1);
		assertEquals(httpsRedirection.getId(), httpsId);
		assertEquals(httpsRedirection.getName(), httpsName);
		assertEquals(httpsRedirection.getToPort(), httpsPort);
		assertEquals(httpsRedirection.getUrl(), r2Url);
	}

	@Test
	public void shouldGetHttpRedirectionForVmWithPublicIp() throws Exception {
		 givenWorkflowAsiWithPublicIp();
		 whenGetRedirections();
		 thenRedirectionWithDirectUrlCreated();
	}	

	private void givenWorkflowAsiWithPublicIp() {
		givenApplianceType();
		givenWorflowWithOneVm();
	}

	private void thenRedirectionWithDirectUrlCreated() {
		thenRedirectionWithUrlCreated(directHttpUrl, directHttpsUrl);
	}

	@Test
	public void shouldGetNatASIRedirections() throws Exception {
		 givenWorkflowWithNatRedirection();
		 whenGetRedirections();
		 thenNatRedirectionCreated();
	}

	private void givenWorkflowWithNatRedirection() {
		givenApplianceType();
		Vms vm = givenWorflowWithOneVm();
		
		PortMapping pm = new PortMapping();
		pm.setHeadnode_ip(headnodeIp);
		pm.setHeadnode_port(headnodePort);
		pm.setHttp(false);
		pm.setService_name(natName);
		pm.setVm_port(natPort);
		
		vm.setInternal_port_mappings(Arrays.asList(pm));
	}

	private void thenNatRedirectionCreated() {
		List<NatRedirection> nat = redirections.getNat();
		assertNotNull(nat);
		assertEquals(nat.size(), 1);
	}
}
