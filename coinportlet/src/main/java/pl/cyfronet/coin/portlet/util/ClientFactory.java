package pl.cyfronet.coin.portlet.util;

import javax.portlet.PortletRequest;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.jetspeed.CommonPortletServices;
import org.apache.jetspeed.security.SecurityAttribute;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.KeyManagement;
import pl.cyfronet.coin.api.WorkflowManagement;

public class ClientFactory {
	private static final Logger log = LoggerFactory.getLogger(ClientFactory.class);
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
	
	private CloudFacade cloudFacade;
	private WorkflowManagement workflowManagement;
	private KeyManagement keyManagement;
	
	public void setCloudFacade(CloudFacade cloudFacade) {
		this.cloudFacade = cloudFacade;
	}

	public void setWorkflowManagement(WorkflowManagement workflowManagement) {
		this.workflowManagement = workflowManagement;
	}
	
	public void setKeyManagement(KeyManagement keyManagement) {
		this.keyManagement = keyManagement;
	}

	public CloudFacade getCloudFacade(PortletRequest request) {
		attachBasicAuth(request, cloudFacade);
			
		return cloudFacade;
	}
	
	public WorkflowManagement getWorkflowManagement(PortletRequest request) {
		attachBasicAuth(request, workflowManagement);
		
		return workflowManagement;
	}
	
	public String createBasicAuthHeader(PortletRequest request) {
		UserManager userManager = (UserManager) request.getPortletSession().getPortletContext().
				getAttribute(CommonPortletServices.CPS_USER_MANAGER_COMPONENT);
		String token = null;
		
		try {
			SecurityAttribute tokenAttribute = userManager.getUser(request.getUserPrincipal().getName()).
					getSecurityAttributes().getAttribute("token");
			
			if(tokenAttribute != null) {
				token = tokenAttribute.getStringValue();
			}
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Could not obtain user token from security attribute map");
		}
		
		if(token == null) {
			throw new IllegalArgumentException("Could not obtaing user token from portlet session " +
					"with id " + request.getPortletSession().getId());
		}

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