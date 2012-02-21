package pl.cyfronet.coin.portlet.portal;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.jetspeed.CommonPortletServices;
import org.apache.jetspeed.security.Role;
import org.apache.jetspeed.security.RoleManager;
import org.apache.jetspeed.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Portal {
	private static final Logger log = LoggerFactory.getLogger(Portal.class);
	
	public List<String> getUserRoles(PortletRequest request) {
		List<String> roles = new ArrayList<String>();
		String user = request.getUserPrincipal().getName();
		RoleManager roleManager = (RoleManager) request.getPortletSession().getPortletContext().
				getAttribute(CommonPortletServices.CPS_ROLE_MANAGER_COMPONENT);
		
		try {
			for(Role role : roleManager.getRolesForUser(user)) {
				roles.add(role.getName());
			}
		} catch (SecurityException e1) {
			log.warn("Could not retrieve roles for user {}", user);
		}
		
		return roles;
	}

	public String getUserName(RenderRequest request) {
		return request.getUserPrincipal().getName();
	}
}