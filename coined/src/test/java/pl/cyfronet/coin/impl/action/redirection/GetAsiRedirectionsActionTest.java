package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.Status;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ATPortMapping;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.air.client.PortMapping;
import pl.cyfronet.coin.impl.air.client.Vms;
import pl.cyfronet.coin.impl.air.client.WorkflowDetail;

public class GetAsiRedirectionsActionTest extends ActionTest {

	private static final String ASI_ID = "siteId-vm-redirections";
	private static final String CTX_ID = "redirectionsTest";
	private static final String USERNAME = "redirectedUser";
	private static final String WORKFLOW_NAME = "redirectedWorkflow";
	private static final String WEBAPP_ATPM_ID = "http-redirection-id";
	private static final String INIT_CONF_ID = "init-conf-id";
	
	
	private List<Redirection> redirections = null;
	
	
	@Test
	public void shouldGetAsiRedirections() {
		givenAsiRedirectionsInAIR();
		whenGetAsiRedirections();
		thenAsiRedirectionsReturned();
	}

	private void givenAsiRedirectionsInAIR() {
		WorkflowDetail wd = new WorkflowDetail();
		wd.setId(CTX_ID);
		wd.setVph_username(USERNAME);
		wd.setName(WORKFLOW_NAME);
		wd.setWorkflow_type(WorkflowType.development);
		PortMapping sshMapping = new PortMapping();
		sshMapping.setVm_port(22);
		sshMapping.setHeadnode_port(222);
		sshMapping.setHeadnode_ip("headnodeIp");
		sshMapping.setService_name("ssh");
		sshMapping.setHttp(false);
		
		PortMapping sshMapping2 = new PortMapping();
		sshMapping2.setVm_port(22);
		sshMapping2.setHeadnode_port(444);
		sshMapping2.setHeadnode_ip("headnodeIp");
		sshMapping2.setService_name("ssh");
		sshMapping2.setHttp(false);

		PortMapping vncMapping = new PortMapping();
		vncMapping.setVm_port(5900);
		vncMapping.setHeadnode_port(55900);
		vncMapping.setHeadnode_ip("headnodeIp");
		vncMapping.setService_name("vnc");
		vncMapping.setHttp(false);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setAppliance_type_name("type1 name");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id(ASI_ID);
		vm1.setInternal_port_mappings(Arrays.asList(sshMapping, vncMapping));
		vm1.setUser_key("userKey1");
		vm1.setConf_id(INIT_CONF_ID);

		Vms vm2 = new Vms();
		vm2.setAppliance_type("type2");
		vm2.setAppliance_type_name("type2 name");
		vm2.setName("vm2");
		vm2.setState(Status.running);
		vm2.setVms_id("id2");
		vm2.setInternal_port_mappings(Arrays.asList(sshMapping2));
		vm2.setUser_key("userKey2");

		wd.setVms(Arrays.asList(vm1, vm2));

		when(air.getWorkflow(CTX_ID)).thenReturn(wd);

		ApplianceType applType = new ApplianceType();
		ATPortMapping httpMapping = new ATPortMapping();
		httpMapping.setPort(8080);
		httpMapping.setService_name("webapp");
		httpMapping.setHttp(true);
		httpMapping.setId(WEBAPP_ATPM_ID);
		ATPortMapping vncatpm = new ATPortMapping();
		vncatpm.setPort(5900);
		vncatpm.setService_name("vnc");
		vncatpm.setHttp(false);
		ATPortMapping sshatpm = new ATPortMapping();
		sshatpm.setPort(22);
		sshatpm.setService_name("ssh");
		sshatpm.setHttp(false);
		List<ATPortMapping> pms =  Arrays.asList(httpMapping, vncatpm, sshatpm);
		applType.setPort_mappings(pms);
		
		when(air.getTypeFromVM(ASI_ID)).thenReturn(applType);
	}

	private void whenGetAsiRedirections() {
		Action<List<Redirection>> action = new GetAsiRedirectionsAction(CTX_ID, USERNAME, ASI_ID, air);
		redirections = action.execute();
	}

	private void thenAsiRedirectionsReturned() {
		assertNotNull(redirections);
		assertEquals(redirections.size(), 3);
	}

}
