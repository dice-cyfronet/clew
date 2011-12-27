package pl.cyfronet.coin.portlet.cloudmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("VIEW")
public class CloudManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(CloudManagerPortlet.class);
	
	@RequestMapping
	public String doView() {
		log.debug("Generating main view for the cloud manager portlet");
		
		return "cloudManager/main";
	}
}