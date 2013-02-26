package pl.cyfronet.coin.portlet.util;

import javax.portlet.PortletRequest;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.KeyManagement;
import pl.cyfronet.coin.api.SecurityPolicyService;
import pl.cyfronet.coin.api.WorkflowManagement;
import pl.cyfronet.coin.portlet.portal.Portal;

public class ClientFactory {
	public static final String HEADER_AUTHORIZATION = "Authorization";
	
	private CloudFacade cloudFacade;
	private WorkflowManagement workflowManagement;
	private KeyManagement keyManagement;
	private SecurityPolicyService securityPolicyService;
	
	@Autowired private Portal portal;
	
	public void setCloudFacade(CloudFacade cloudFacade) {
		this.cloudFacade = cloudFacade;
	}
	public void setWorkflowManagement(WorkflowManagement workflowManagement) {
		this.workflowManagement = workflowManagement;
	}
	public void setKeyManagement(KeyManagement keyManagement) {
		this.keyManagement = keyManagement;
	}
	public void setSecurityPolicyService(SecurityPolicyService securityPolicyService) {
		this.securityPolicyService = securityPolicyService;
	}

	public CloudFacade getCloudFacade(PortletRequest request) {
		attachBasicAuth(request, cloudFacade);
			
		return cloudFacade;
	}
	
	public WorkflowManagement getWorkflowManagement(PortletRequest request) {
		attachBasicAuth(request, workflowManagement);
		
		return workflowManagement;
	}
	
	public SecurityPolicyService getSecurityPolicyService(PortletRequest request) {
		attachBasicAuth(request, workflowManagement);
		
		return securityPolicyService;
	}
	
	private String createBasicAuthHeader(PortletRequest request) {
		String token = portal.getUserToken(request);

		return "Basic " 
			    + org.apache.cxf.common.util.Base64Utility.encode((
			    		request.getUserPrincipal().getName() + ":" + token).getBytes());
	}
	
	private void attachBasicAuth(PortletRequest request, Object proxy) {
		if(request.getUserPrincipal() == null) {
			throw new IllegalArgumentException("Portlet request does not contain user principal");
		}

		Client client = WebClient.client(proxy);
		client.reset();
		client.header(HEADER_AUTHORIZATION, createBasicAuthHeader(request));
	}

	public KeyManagement getKeyManagement(PortletRequest request) {
		attachBasicAuth(request, keyManagement);
		
		return keyManagement;
	}
}