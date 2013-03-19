package pl.cyfronet.coin.impl.action.redirection;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.RedirectionType;
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

//TODO add test for wf of different types (postfix is build in different way), check exceptions

public class GetAsiRedirectionsActionTest extends ActionTest {

	private static final String HTTP_SRV_NAME = "webapp";
	private static final int HTTP_TO_PORT = 8080;
	private static final String VNC_SRV_NAME = "vnc";
	private static final int VNC_TO_PORT = 5900;
	private static final int VNC_FROM_PORT = 55900;
	private static final String SSH_ID = "sshId";
	private static final String SSH_SRV_NAME = "ssh";
	private static final String ASI_ID = "siteId-vm-redirections";
	private static final String CTX_ID = "redirectionsTest";
	private static final String USERNAME = "redirectedUser";
	private static final String WORKFLOW_NAME = "redirectedWorkflow";
	private static final String WEBAPP_ATPM_ID = "http-redirection-id";
	private static final String INIT_CONF_ID = "init-conf-id";
	private static final int PROXY_PORT = 8000;
	private static final String PROXY_HOST = "149.156.10.133";
	private static final String HEADNODE_IP = "149.156.10.133";
	private static final int SSH_FROM_PORT = 222;
	private static final int SSH_FROM_PORT_2 = 444;
	private static final int SSH_TO_PORT = 22;
	private static final String SSH_ID2 = "ssh2Id";
	private static final String VNC_ID = "vncId";

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
		sshMapping.setId(SSH_ID);
		sshMapping.setVm_port(SSH_TO_PORT);
		sshMapping.setHeadnode_port(SSH_FROM_PORT);
		sshMapping.setHeadnode_ip(HEADNODE_IP);
		sshMapping.setService_name(SSH_SRV_NAME);
		sshMapping.setHttp(false);

		PortMapping sshMapping2 = new PortMapping();
		sshMapping2.setId(SSH_ID2);
		sshMapping2.setVm_port(SSH_TO_PORT);
		sshMapping2.setHeadnode_port(SSH_FROM_PORT_2);
		sshMapping2.setHeadnode_ip(HEADNODE_IP);
		sshMapping2.setService_name(SSH_SRV_NAME);
		sshMapping2.setHttp(false);

		PortMapping vncMapping = new PortMapping();
		vncMapping.setId(VNC_ID);
		vncMapping.setVm_port(VNC_TO_PORT);
		vncMapping.setHeadnode_port(VNC_FROM_PORT);
		vncMapping.setHeadnode_ip(HEADNODE_IP);
		vncMapping.setService_name(VNC_SRV_NAME);
		vncMapping.setHttp(false);

		Vms vm1 = new Vms();
		vm1.setAppliance_type("type1");
		vm1.setAppliance_type_name("type1 name");
		vm1.setName("vm1");
		vm1.setState(Status.booting);
		vm1.setVms_id(ASI_ID);
		vm1.setInternal_port_mappings(Arrays.asList(sshMapping, vncMapping));
		vm1.setUser_key("userKey1");
		vm1.setConfiguration_id(INIT_CONF_ID);

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
		httpMapping.setPort(HTTP_TO_PORT);
		httpMapping.setService_name(HTTP_SRV_NAME);
		httpMapping.setHttp(true);
		httpMapping.setId(WEBAPP_ATPM_ID);
		ATPortMapping vncatpm = new ATPortMapping();
		vncatpm.setPort(VNC_FROM_PORT);
		vncatpm.setService_name(VNC_SRV_NAME);
		vncatpm.setHttp(false);
		ATPortMapping sshatpm = new ATPortMapping();
		sshatpm.setPort(SSH_FROM_PORT);
		sshatpm.setService_name(SSH_SRV_NAME);
		sshatpm.setHttp(false);
		List<ATPortMapping> pms = Arrays.asList(httpMapping, vncatpm, sshatpm);
		applType.setPort_mappings(pms);

		when(air.getTypeFromVM(ASI_ID)).thenReturn(applType);
	}

	private void whenGetAsiRedirections() {
		Action<List<Redirection>> action = new GetAsiRedirectionsAction(
				actionFactory, CTX_ID, USERNAME, ASI_ID, PROXY_HOST, PROXY_PORT);
		redirections = action.execute();
	}

	private void thenAsiRedirectionsReturned() {
		assertNotNull(redirections);
		assertEquals(redirections.size(), 3);
		checkSshRedirection(redirections);
		checkVncRedirection(redirections);
		checkHttpRedirection(redirections);
	}

	private void checkSshRedirection(List<Redirection> redirections) {
		Redirection redirection = null;
		for (Redirection r : redirections) {
			if (r.getName().equals(SSH_SRV_NAME)) {
				redirection = r;
				break;
			}
		}
		if (redirection == null) {
			fail("Expected SSH redirection not found");
		}
		assertEquals(redirection.getId(), SSH_ID);
		assertEquals(redirection.getFromPort().intValue(), SSH_FROM_PORT);
		assertEquals(redirection.getHost(), HEADNODE_IP);
		assertEquals(redirection.getName(), SSH_SRV_NAME);
		assertNull(redirection.getPostfix());
		assertTrue(redirection.getToPort() == SSH_TO_PORT);
		assertTrue(redirection.getType() == RedirectionType.TCP);
	}

	private void checkHttpRedirection(List<Redirection> redirections) {
		Redirection redirection = null;
		for (Redirection r : redirections) {
			if (r.getName().equals(HTTP_SRV_NAME)) {
				redirection = r;
				break;
			}
		}
		if (redirection == null) {
			fail("Expected HTTP redirection not found");
		}
		assertEquals(redirection.getId(), WEBAPP_ATPM_ID);
		assertTrue(redirection.getFromPort() == PROXY_PORT);
		assertEquals(redirection.getHost(), PROXY_HOST);
		assertEquals(redirection.getName(), HTTP_SRV_NAME);
		assertEquals(redirection.getPostfix(), CTX_ID + "/" + ASI_ID + "/"
				+ HTTP_SRV_NAME);
		assertTrue(redirection.getToPort() == HTTP_TO_PORT);
		assertTrue(redirection.getType() == RedirectionType.HTTP);
	}

	private void checkVncRedirection(List<Redirection> redirections) {
		Redirection redirection = null;
		for (Redirection r : redirections) {
			if (r.getName().equals(VNC_SRV_NAME)) {
				redirection = r;
				break;
			}
		}
		if (redirection == null) {
			fail("Expected VNC redirection not found");
		}
		assertEquals(redirection.getId(), VNC_ID);
		assertTrue(redirection.getFromPort() == VNC_FROM_PORT);
		assertEquals(redirection.getHost(), HEADNODE_IP);
		assertEquals(redirection.getName(), VNC_SRV_NAME);
		assertNull(redirection.getPostfix());
		assertTrue(redirection.getToPort() == VNC_TO_PORT);
		assertTrue(redirection.getType() == RedirectionType.TCP);
	}
}
