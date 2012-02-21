package pl.cyfronet.coin.portlet.cloudmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Element;

import pl.cyfronet.coin.api.CloudFacade;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.portlet.portal.Portal;
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
	private static final String MODEL_BEAN_VIEW = "view";
	private static final String MODEL_BEAN_DEVELOPER_MODE = "developerMode";
	private static final String MODEL_BEAN_ATOMIC_SERVICES_WITH_INSTANCES = "activeAtomicServices";
	private static final String MODEL_BEAN_CURRENT_ATOMIC_SERVICE = "currentAtomicServiceId";
	
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_ATOMIC_SERVICE_INSTANCE_ID = "atomicServiceInstanceId";
	private static final String PARAM_INVOCATION_RESULT = "atomicServiceInvocationResult";
	private static final String PARAM_CURRENT_ATOMIC_SERVICE = "currentAtomicService";

	private static final String ACTION_START_ATOMIC_SERVICE = "startAtomicService";
	private static final String ACTION_SAVE_ATOMIC_SERVICE = "saveAtomicService";
	private static final String ACTION_INVOKE_ATOMIC_SERVICE = "invokeAtomicService";
	private static final String ACTION_GENERIC_INVOKER = "genericInvoker";
	private static final String ACTION_WORKFLOWS = "workflows";
	
	@Autowired private CloudFacade cloudFacade;
	@Autowired private ContextIdFactory contextIdFactory;
	@Autowired private Portal portal;
	
	@RequestMapping
	public String doView(RenderRequest request, PortletResponse response, Model model) {
		log.debug("Generating the main view");
		
		if(model.containsAttribute(MODEL_BEAN_VIEW)) {
			String view = (String) model.asMap().get(MODEL_BEAN_VIEW);
			
			if(view.equals(ACTION_GENERIC_INVOKER)) {
				return doViewGenericInvoker(request, response, model, null);
			} else {
				return doViewWorkflows(model);
			}
		} else {
			//returning default view
			return doViewGenericInvoker(request, response, model, null);
		}
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_GENERIC_INVOKER)
	public String doViewGenericInvoker(RenderRequest request, PortletResponse response, Model model,
			@RequestParam(required = false, value = PARAM_CURRENT_ATOMIC_SERVICE) String currentAtomicServiceId) {
		attachHeaders(response);
		
		if(isDeveloper(request)) {
			model.addAttribute(MODEL_BEAN_DEVELOPER_MODE, true);
		}
		
		model.addAttribute(MODEL_BEAN_VIEW, ACTION_GENERIC_INVOKER);
		
		List<AtomicServiceInstance> atomicServiceInstances =
					cloudFacade.getAtomicServiceInstances(contextIdFactory.createContextId(portal.getUserName(request)));
		List<AtomicService> atomicServices = extractAtomicServices(atomicServiceInstances);
		
		if(atomicServices.size() > 0) {
			if(currentAtomicServiceId == null) {
				currentAtomicServiceId = atomicServices.get(0).getAtomicServiceId();
			}
			
			model.addAttribute(MODEL_BEAN_ATOMIC_SERVICE_INSTANCES, 
					extractAtomicServiceInstances(atomicServiceInstances, currentAtomicServiceId));
			model.addAttribute(MODEL_BEAN_CURRENT_ATOMIC_SERVICE, currentAtomicServiceId);
		}
		
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICES_WITH_INSTANCES, atomicServices);
		
		return "cloudManager/genericInvoker";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_WORKFLOWS)
	public String doViewWorkflows(Model model) {
		model.addAttribute(MODEL_BEAN_VIEW, ACTION_WORKFLOWS);
		
		return "cloudManager/workflows";
	}

	private List<AtomicServiceInstance> extractAtomicServiceInstances(
			List<AtomicServiceInstance> atomicServiceInstances, String atomicServiceId) {
		List<AtomicServiceInstance> result = new ArrayList<AtomicServiceInstance>();
		
		for(AtomicServiceInstance asi: atomicServiceInstances) {
			if(asi.getAtomicServiceId().equals(atomicServiceId)) {
				result.add(asi);
			}
		}
		
		return result;
	}

	private void attachHeaders(PortletResponse response) {
		Element stylesheetElement = response.createElement("link");
		stylesheetElement.setAttribute("rel", "stylesheet");
		//TODO - can we discover the context path somehow?
		stylesheetElement.setAttribute("href", "/coinportlet/css/coin.css");
		stylesheetElement.setAttribute("type", "text/css");
		response.addProperty(MimeResponse.MARKUP_HEAD_ELEMENT, stylesheetElement);
	}
	
	private List<AtomicService> extractAtomicServices(
			List<AtomicServiceInstance> atomicServiceInstances) {
		List<AtomicService> result = new ArrayList<AtomicService>();
		List<AtomicService> atomicServices = cloudFacade.getAtomicServices();
		
		for(AtomicServiceInstance asi : atomicServiceInstances) {
			AtomicService atomicService = null;
			
			for(AtomicService as : atomicServices) {
				if(as.getAtomicServiceId().equals(asi.getAtomicServiceId())) {
					atomicService = as;
					break;
				}
			}
			
			if(atomicService != null && !result.contains(atomicService)) {
				result.add(atomicService);
			}
		}
		
		Collections.sort(result, new Comparator<AtomicService>() {
			@Override
			public int compare(AtomicService as1, AtomicService as2) {
				return as1.getName().compareTo(as2.getName());
			}
		});
		
		return result;
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
		atomicService.setHttp(true);
		atomicService.setName(saveAtomicServiceRequest.getName());
		atomicService.setDescription(saveAtomicServiceRequest.getDescription());
		
		Endpoint endpoint = new Endpoint();
		endpoint.setInvocationPath(saveAtomicServiceRequest.getInvocationEndpoint());
		endpoint.setServiceDescription(saveAtomicServiceRequest.getDescriptionEndpoint());
		endpoint.setPort(Integer.parseInt(saveAtomicServiceRequest.getPorts().split(",")[0]));
		atomicService.setEndpoint(new ArrayList<Endpoint>());
		atomicService.getEndpoint().add(endpoint);
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
	
	private boolean isDeveloper(RenderRequest request) {
		return portal.getUserRoles(request).contains("developer");
	}
}