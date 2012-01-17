package pl.cyfronet.coin.portlet.cloudmanager;

import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	private static final String MODEL_BEAN_POSITIVE_MESSAGE = "positiveMessage";
	private static final String MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST = "saveAtomicServiceRequest";
	private static final String MODEL_BEAN_ATOMIC_SERVICE_METHOD_LIST = "atomicServiceMethodList";
	private static final String MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST = "invokeAtomicServiceRequest";
	
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_ATOMIC_SERVICE_INSTANCE_ID = "atomicServiceInstanceId";
	private static final String PARAM_INVOCATION_RESULT = "atomicServiceInvocationResult";

	private static final String ACTION_START_ATOMIC_SERVICE = "startAtomicService";
	private static final String ACTION_SAVE_ATOMIC_SERVICE = "saveAtomicService";
	private static final String ACTION_INVOKE_ATOMIC_SERVICE = "invokeAtomicService";
	
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
		
		if(!model.containsAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)) {
			model.addAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST, new StartAtomicServiceRequest());
		}
		
		return "cloudManager/atomicServiceStartupParams";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_START_ATOMIC_SERVICE)
	public void doActionStartAtomicService(@ModelAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)
			StartAtomicServiceRequest startAtomicServiceRequest, ActionResponse response) {
		log.info("Processing start atomic service request [{}]", startAtomicServiceRequest);
		String atomicServiceInstanceId = cloudFacade.startAtomicServiceInstance(
				startAtomicServiceRequest.getAtomicServiceId(), startAtomicServiceRequest.getAtomicServiceName(),
				contextIdFactory.createContextId("userName"));
		response.setRenderParameter(MODEL_BEAN_POSITIVE_MESSAGE, "Atomic service with instance id " +
				atomicServiceInstanceId + " successfully created");
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SAVE_ATOMIC_SERVICE)
	public String doViewSaveAtomicService(@RequestParam("") String atomicServiceInstanceId, Model model) {
		if(!model.containsAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST)) {
			SaveAtomicServiceRequest sasr = new SaveAtomicServiceRequest();
			sasr.setAtomicServiceInstanceId(atomicServiceInstanceId);
			model.addAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST, sasr);
		}
		
		return "cloudManager/saveAtomicService";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SAVE_ATOMIC_SERVICE)
	public void doActionSaveAtomicService(@ModelAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST)
			SaveAtomicServiceRequest saveAtomicServiceRequest, Model model) {
		AtomicService atomicService = new AtomicService();
		atomicService.setAtomicService(true);
		atomicService.setName(saveAtomicServiceRequest.getName());
		atomicService.setDescription(saveAtomicServiceRequest.getDescription());
		atomicService.setInvocationEndpoint(saveAtomicServiceRequest.getInvocationEndpoint());
		atomicService.setSemanticDescriptionEndpoint(saveAtomicServiceRequest.getDescriptionEndpoint());
		atomicService.setPorts(Arrays.asList(saveAtomicServiceRequest.getPorts().split(",")));
		cloudFacade.createAtomicService(saveAtomicServiceRequest.getAtomicServiceInstanceId(), atomicService);
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_INVOKE_ATOMIC_SERVICE)
	public String doViewInvokeAtomicService(@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID)
			String atomicServiceInstanceId, @RequestParam(required = false, value = PARAM_INVOCATION_RESULT)
			String invocationResult, Model model) {
		AtomicServiceInstance atomicServiceInstance =
				cloudFacade.getAtomicServiceInstance(atomicServiceInstanceId);
		
		if(!model.containsAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST)) {
			InvokeAtomicServiceRequest iasr = new InvokeAtomicServiceRequest();
			iasr.setAtomicServiceInstanceId(atomicServiceInstanceId);
			model.addAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST, iasr);
		}
		
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICE_METHOD_LIST,
				getAtomicServiceMethods(atomicServiceInstance));
		
		if(invocationResult != null) {
			//TODO: in future do not use request parameter to pass AS invocation result
			model.addAttribute(PARAM_INVOCATION_RESULT, invocationResult);
		}
		
		return "cloudManager/invokeAtomicService";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_INVOKE_ATOMIC_SERVICE)
	public void doActionInvokeAtomicService(@ModelAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST)
			InvokeAtomicServiceRequest invokeAtomicServiceRequest, ActionResponse response) {
		String result = invokeAtomicService(invokeAtomicServiceRequest.getAtomicServiceInstanceId(),
				invokeAtomicServiceRequest.getMethod(), invokeAtomicServiceRequest.getMessageBody());
		response.setRenderParameter(PARAM_ACTION, ACTION_INVOKE_ATOMIC_SERVICE);
		response.setRenderParameter(PARAM_INVOCATION_RESULT, result);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID,
				invokeAtomicServiceRequest.getAtomicServiceInstanceId());
	}

	private String invokeAtomicService(String atomicServiceInstanceId,
			String method, String messageBody) {
		return "hello " + messageBody;
	}

	private List<String> getAtomicServiceMethods(
			AtomicServiceInstance atomicServiceInstance) {
		return Arrays.asList(new String[] {"method1", "method2", "method3"});
	}
}