package pl.cyfronet.coin.portlet.login;

import java.util.Map;

import javax.annotation.Resource;
import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.cyfronet.coin.auth.mi.MasterInterfaceAuthClient;
import pl.cyfronet.coin.auth.mi.UserDetails;
import pl.cyfronet.coin.portlet.portal.Portal;

@Controller
@RequestMapping("VIEW")
public class LoginPortlet {
	private static final Logger log = LoggerFactory.getLogger(LoginPortlet.class);
	
	private static final String MODEL_BEAN_LOGIN_SERVLET_PATH = "loginServletPath";
	private static final String MODEL_BEAN_LOGIN_PARAMETER_NAME = "loginParameterName";
	private static final String MODEL_BEAN_PASSWORD_PARAMETER_NAME = "passwordParameterName";
	private static final String MODEL_BEAN_DESTINATION_PARAMETER_NAME = "destinationParameterName";
	private static final String MODEL_BEAN_USER_LOGIN = "userLogin";
	private static final String MODEL_BEAN_USER_TOKEN = "userToken";
	private static final String MODEL_BEAN_USER_DESTINATION = "userDestination";
	
	@Value("${login.portlet.login.servlet.path}") private String loginServletPath;
	@Value("${login.portlet.login.parameter.name}") private String loginParameterName;
	@Value("${login.portlet.password.parameter.name}") private String passwordParameterName;
	@Value("${login.portlet.destination.parameter.name}") private String destinationParameterName;
	
	@Resource(name = "pageMapping") Map<String, String> pageMapping;
	
	@Autowired Portal portal;
	@Autowired MasterInterfaceAuthClient ticketService;
	
	@RequestMapping
	public String login(Model model, PortletRequest request) {
		String user = request.getParameter("user");
		String token = request.getParameter("token");
		String destination = request.getParameter("destination");
		log.info("Processing login request for user [{}], token [{}] and destination [{}]",
				new String[] {user, token, destination});
		
		if(user != null && token != null && destination != null) {
			UserDetails userDetails = ticketService.validate(token);
			
			if(userDetails == null) {
				throw new IllegalArgumentException("User token [" + token + "] is not valid");
			}
			
			portal.updateUser(user, token, userDetails.getRole(), request);
			model.addAttribute(MODEL_BEAN_USER_LOGIN, user);
			model.addAttribute(MODEL_BEAN_USER_TOKEN, token);
			model.addAttribute(MODEL_BEAN_USER_DESTINATION, pageMapping.get(destination));
			model.addAttribute(MODEL_BEAN_LOGIN_SERVLET_PATH, loginServletPath);
			model.addAttribute(MODEL_BEAN_LOGIN_PARAMETER_NAME, loginParameterName);
			model.addAttribute(MODEL_BEAN_PASSWORD_PARAMETER_NAME, passwordParameterName);
			model.addAttribute(MODEL_BEAN_DESTINATION_PARAMETER_NAME, destinationParameterName);
		} else {
			throw new IllegalArgumentException("User login, token or destination missing");
		}
		
		return "login/login";
	}
}