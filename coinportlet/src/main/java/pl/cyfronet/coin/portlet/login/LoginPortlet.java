package pl.cyfronet.coin.portlet.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("VIEW")
public class LoginPortlet {
	@RequestMapping
	public String login() {
		return "login/login";
	}
}