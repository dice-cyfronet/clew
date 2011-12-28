package pl.cyfronet.coin.portlet.cloudmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.portlet.util.ContextIdFactory;

@Controller
@RequestMapping("VIEW")
public class CloudManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(CloudManagerPortlet.class);
	
	private static final String MODEL_BEAN_ATOMIC_SERVICE_INSTANCES = "atomicServiceInstances";
	private static final String MODEL_BEAN_ATOMIC_SERVICES = "atomicServices";
	private static final String MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST = "startAtomicServiceRequest";
	private static final String MODEL_AVAILABLE_STORAGE_CHOICES = "storageSizeValues";
	
	private static final String PARAM_ACTION = "action";

	private static final String ACTION_START_ATOMIC_SERVICE = "startAtomicService";
	
	@Autowired private CloudFacade cloudFacade;
	@Autowired private ContextIdFactory contextIdFactory;
	
	@RequestMapping
	public String doView(Model model) {
		log.debug("Generating the main view");
		
		List<AtomicServiceInstance> atomicServiceInstances =
				cloudFacade.getAtomicServiceInstances(contextIdFactory.createContextId("userName"));
		//TODO - obtain user name from the portal's user manager (somehow)
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICE_INSTANCES, atomicServiceInstances);
		
		return "cloudManager/main";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_START_ATOMIC_SERVICE)
	public String doViewStartAtomicService(Model model) {
		log.debug("Generating the atomic service startup parameters view");
		
		List<AtomicService> atomicServices = cloudFacade.getAtomicServices();
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICES, atomicServices);
		model.addAttribute(MODEL_AVAILABLE_STORAGE_CHOICES, Arrays.asList(new Integer[] {10, 20, 30}));
		
		if(!model.containsAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)) {
			model.addAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST, new StartAtomicServiceRequest());
		}
		
		return "cloudManager/atomicServiceStartupParams";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_START_ATOMIC_SERVICE)
	public void doActionStartAtomicService(@ModelAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)
			StartAtomicServiceRequest startAtomicServiceRequest) {
		log.info("Processing start atomic service request [{}]", startAtomicServiceRequest);
	}
}