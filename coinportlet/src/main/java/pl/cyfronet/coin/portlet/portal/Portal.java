package pl.cyfronet.coin.portlet.portal;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.jetspeed.CommonPortletServices;
import org.apache.jetspeed.administration.PortalAdministration;
import org.apache.jetspeed.administration.RegistrationException;
import org.apache.jetspeed.security.PasswordCredential;
import org.apache.jetspeed.security.Role;
import org.apache.jetspeed.security.RoleManager;
import org.apache.jetspeed.security.SecurityAttribute;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.User;
import org.apache.jetspeed.security.UserManager;
import org.apache.jetspeed.userinfo.UserInfoManager;
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

	public void registerUser(String userName, String token, PortletRequest request) {
		UserManager userManager = (UserManager) request.getPortletSession().getPortletContext().
				getAttribute(CommonPortletServices.CPS_USER_MANAGER_COMPONENT);
		PortalAdministration portalAdministration = (PortalAdministration) request.getPortletSession().getPortletContext().
				getAttribute(CommonPortletServices.CPS_PORTAL_ADMINISTRATION);
		try {
			if(!userManager.userExists(userName)) {
				//creating new user
				portalAdministration.registerUser(userName, portalAdministration.generatePassword());
			}
			
			//updating user password
			User user = userManager.getUser(userName);
			PasswordCredential pc = userManager.getPasswordCredential(user);
			pc.setPassword(token, false);
			userManager.storePasswordCredential(pc);
			
			//setting the token as one of the user's attributes
			SecurityAttribute tokenAttribute = user.getSecurityAttributes().getAttribute("token", true);
			tokenAttribute.setStringValue(token);
			userManager.updateUser(user);
		} catch (SecurityException e) {
			log.error("Could not register or update user [{}]", userName, e);
		} catch (RegistrationException e) {
			log.error("Could not register or update user [{}]", userName, e);
		}
	}
}