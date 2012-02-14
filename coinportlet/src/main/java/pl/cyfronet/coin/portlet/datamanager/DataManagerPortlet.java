package pl.cyfronet.coin.portlet.datamanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("VIEW")
public class DataManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(DataManagerPortlet.class);
	
	@RequestMapping
	public String doView() {
		log.debug("Generating data portlet main view");
		
		return "dataManager/main";
	}
}