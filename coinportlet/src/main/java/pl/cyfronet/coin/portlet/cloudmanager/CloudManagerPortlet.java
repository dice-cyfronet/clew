package pl.cyfronet.coin.portlet.cloudmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.util.HtmlUtils;

import pl.cyfronet.coin.api.RedirectionType;
import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.EndpointType;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowBaseInfo;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.WorkflowType;
import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;
import pl.cyfronet.coin.api.exception.WrongKeyFormatException;
import pl.cyfronet.coin.portlet.portal.Portal;
import pl.cyfronet.coin.portlet.util.ClientFactory;
import pl.cyfronet.coin.portlet.util.HttpUtil;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({CloudManagerPortlet.MODEL_BEAN_DEVELOPER_MODE, CloudManagerPortlet.MODEL_BEAN_VIEW})
public class CloudManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(CloudManagerPortlet.class);
	
	static final String MODEL_BEAN_ATOMIC_SERVICE_INSTANCES = "atomicServiceInstances";
	static final String MODEL_BEAN_ATOMIC_SERVICES = "atomicServices";
	static final String MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST = "startAtomicServiceRequest";
	static final String MODEL_BEAN_POSITIVE_MESSAGE = "positiveMessage";
	static final String MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST = "saveAtomicServiceRequest";
	static final String MODEL_BEAN_ATOMIC_SERVICE_METHOD_LIST = "atomicServiceMethodList";
	static final String MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST = "invokeAtomicServiceRequest";
	static final String MODEL_BEAN_VIEW = "view";
	static final String MODEL_BEAN_DEVELOPER_MODE = "developerMode";
	static final String MODEL_BEAN_ATOMIC_SERVICES_WITH_INSTANCES = "activeAtomicServices";
	static final String MODEL_BEAN_CURRENT_ATOMIC_SERVICE_ID = "currentAtomicServiceId";
	static final String MODEL_BEAN_CURRENT_ATOMIC_SERVICE = "currentAtomicService";
	static final String MODEL_BEAN_AS_INVOCATION_POSSIBLE = "atomicServiceInvokable";
	static final String MODEL_BEAN_NEGATIVE_MESSAGE = "negativeMessage";
	static final String MODEL_BEAN_INVOCATION_PATH = "invocationPath";
	static final String MODEL_BEAN_WEBAPP_ENDPOINTS = "webappEndpoints";
	static final String MODEL_BEAN_USER_KEYS = "userKeyList";
	static final String MODEL_BEAN_UPLOAD_KEY_REQUEST = "uploadKeyRequest";
	static final String MODEL_BEAN_ASI_REDIRECTIONS = "asiRedirections";
	static final String MODEL_BEAN_WS_ENDPOINTS = "wsEndpoints";
	static final String MODEL_BEAN_ENDPOINTS = "endpoints";
	static final String MODEL_BEAN_ADD_ENDPOINT_REQUEST = "addEndpointRequest";
	static final String MODEL_BEAN_ENDPOINT_TYPES = "endpointTypes";
	static final String MODEL_BEAN_REDIRECTIONS = "redirections";
	static final String MODEL_BEAN_REDIRECTION_TYPES = "redirectionTypes";
	static final String MODEL_BEAN_ADD_REDIRECTION_REQUEST = "addRedirectionRequest";
	static final String MODEL_BEAN_REDIRECTION_SELECTION = "redirectionSelection";
	static final String MODEL_BEAN_ENDPOINT_LINKS = "endpointLinks";
	static final String MODEL_BEAN_AUTH_ENDPOINT_LINKS = "authEndpointLinks";
	static final String MODEL_BEAN_BREAKING_ENDPOINT_LINKS = "breakingEndpointLinks";
	static final String MODEL_BEAN_USER_NAME = "userName";
	
	static final String PARAM_ACTION = "action";
	static final String PARAM_ATOMIC_SERVICE_INSTANCE_ID = "atomicServiceInstanceId";
	static final String PARAM_INVOCATION_RESULT = "atomicServiceInvocationResult";
	static final String PARAM_CURRENT_ATOMIC_SERVICE = "currentAtomicService";
	static final String PARAM_ATOMIC_SERVICE_ID = "atomicServiceId";
	static final String PARAM_WORKFLOW_ID = "workflowId";
	static final String PARAM_WORKFLOW_TYPE = "workflowType";
	static final String PARAM_INVOCATION_CODE = "atomicServiceInvocationCode";
	static final String PARAM_USER_KEY_ID = "userKeyId";
	static final String PARAM_ENDPOINT_ID = "endpointId";
	static final String PARAM_REDIRECTION_ID = "redirectionId";
	static final String PARAM_AS_REMOVAL_ERROR = "asRemovalError";

	static final String ACTION_START_ATOMIC_SERVICE = "startAtomicService";
	static final String ACTION_SAVE_ATOMIC_SERVICE = "saveAtomicService";
	static final String ACTION_INVOKE_ATOMIC_SERVICE = "invokeAtomicService";
	static final String ACTION_GENERIC_INVOKER = "genericInvoker";
	static final String ACTION_WORKFLOWS = "workflows";
	static final String ACTION_DEVELOPMENT = "development";
	static final String ACTION_STOP_WORKFLOW = "stopWorkflow";
	static final String ACTION_AS_SAVING_STATE = "asSavingState";
	static final String ACTION_STOP_DEV_INSTANCE = "stopDevInstance";
	static final String ACTION_STOP_INVOKER_INSTANCE = "stopInvokerInstance";
	static final String ACTION_USER_KEYS = "userKeys";
	static final String ACTION_UPLOAD_KEY = "uploadKey";
	static final String ACTION_REMOVE_KEY = "removeUserKey";
	static final String ACTION_PICK_USER_KEY = "pickUserKey";
	static final String ACTION_EDIT_ENDPOINTS = "editEndpoints";
	static final String ACTION_ADD_ENDPOINT = "addEndpoint";
	static final String ACTION_REMOVE_ENDPOINT = "removeEndpoint";
	static final String ACTION_ADD_REDIRECTION = "addRedirection";
	static final String ACTION_REMOVE_REDIRECTION = "removeRedirection";
	static final String ACTION_REMOVE_AS = "removeAs";
	static final String ACTION_EDIT_AS = "editAs";
	
	@Autowired private ClientFactory clientFactory;
	@Autowired private Portal portal;
	@Autowired private MessageSource messages;
	@Autowired private Validator validator;
	@Autowired private HttpUtil httpUtil;
	
	@RequestMapping
	public String doView(Model model, RenderRequest request, PortletResponse response) {
		log.debug("Generating the main view");
		
		if(portal.getUserRoles(request).contains("developer")) {
			model.addAttribute(MODEL_BEAN_DEVELOPER_MODE, true);
		} else {
			model.addAttribute(MODEL_BEAN_DEVELOPER_MODE, false);
		}
		
		if(model.containsAttribute(MODEL_BEAN_VIEW)) {
			String view = (String) model.asMap().get(MODEL_BEAN_VIEW);
			
			if(view.equals(ACTION_GENERIC_INVOKER)) {
				return doViewGenericInvoker(null, model, request, response);
			} else if(view.equals(ACTION_DEVELOPMENT)) {
				return doViewDevelopment(null, model, request, response);
			} else {
				return doViewWorkflows(model, request, response);
			}
		} else {
			//returning default view
			return doViewGenericInvoker(null, model, request, response);
		}
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_DEVELOPMENT)
	public String doViewDevelopment(@RequestParam(required = false, value = PARAM_CURRENT_ATOMIC_SERVICE) String currentAtomicServiceId,
			Model model, RenderRequest request, PortletResponse response) {
		model.addAttribute(MODEL_BEAN_VIEW, ACTION_DEVELOPMENT);
		
		List<String> portalWorkflowIds = getWorkflowIds(WorkflowType.development, request);
		Map<AtomicService, List<AtomicServiceInstance>> atomicServiceInstances = null;
		
		if(portalWorkflowIds.size() > 0) {
			atomicServiceInstances =
					getAtomicServiceInstances(portalWorkflowIds.get(0), request);
			
			if(atomicServiceInstances.size() > 0) {
				model.addAttribute(PARAM_WORKFLOW_ID, portalWorkflowIds.get(0));
			}
		}
		
		if(atomicServiceInstances == null) {
			atomicServiceInstances = new HashMap<AtomicService, List<AtomicServiceInstance>>();
		}
		
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICES_WITH_INSTANCES, atomicServiceInstances);
		
		return "cloudManager/development";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_GENERIC_INVOKER)
	public String doViewGenericInvoker(@RequestParam(required = false, value = PARAM_CURRENT_ATOMIC_SERVICE) String currentAtomicServiceId,
			Model model, RenderRequest request, PortletResponse response) {
		model.addAttribute(MODEL_BEAN_VIEW, ACTION_GENERIC_INVOKER);
		
		List<String> portalWorkflowIds = getWorkflowIds(WorkflowType.portal, request);
		Map<AtomicService, List<AtomicServiceInstance>> atomicServiceInstances = null;
		
		if(portalWorkflowIds.size() > 0) {
			atomicServiceInstances =
					getAtomicServiceInstances(portalWorkflowIds.get(0), request);
			
			if(atomicServiceInstances.size() > 0) {
				model.addAttribute(MODEL_BEAN_ATOMIC_SERVICES_WITH_INSTANCES, atomicServiceInstances);
				model.addAttribute(PARAM_WORKFLOW_ID, portalWorkflowIds.get(0));
			}
		} else {
			atomicServiceInstances = new HashMap<AtomicService, List<AtomicServiceInstance>>();
		}

		return "cloudManager/genericInvoker";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_WORKFLOWS)
	public String doViewWorkflows(Model model, RenderRequest request, PortletResponse response) {
		model.addAttribute(MODEL_BEAN_VIEW, ACTION_WORKFLOWS);
		
		return "cloudManager/workflows";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_STOP_WORKFLOW)
	public void doActionStopWorkflow(@RequestParam("workflowType") WorkflowType workflowType,
			@RequestParam(value = PARAM_WORKFLOW_ID, required = false) String workflowId,
			PortletRequest request, ActionResponse response) {
		log.info("Stopping workflows of type [{}]", workflowType);
		List<String> workflowIds = getWorkflowIds(workflowType, request);
		
		if(workflowIds.size() > 0) {
			for(String wId : workflowIds) {
				if(workflowId != null) {
					if(workflowId.equals(wId)) {
						clientFactory.getWorkflowManagement(request).stopWorkflow(wId);
						break;
					}
				} else {
					clientFactory.getWorkflowManagement(request).stopWorkflow(wId);
				}
			}
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_STOP_DEV_INSTANCE)
	public void doActionStopAsDevelopmentInstance(@RequestParam("workflowId") String workflowId,
			@RequestParam("atomicServiceInstanceId") String atomicServiceInstanceId,
			PortletRequest request, ActionResponse response) {
		log.info("Stopping AS instance with id [{}] from workflow with id [{}]", atomicServiceInstanceId, workflowId);
		clientFactory.getWorkflowManagement(request).removeAtomicServiceInstanceFromWorkflow(workflowId, atomicServiceInstanceId);
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_STOP_INVOKER_INSTANCE)
	public void doActionStopAsInvokerInstance(@RequestParam("workflowId") String workflowId,
			@RequestParam("atomicServiceId") String atomicServiceId,
			PortletRequest request, ActionResponse response) {
		List<InitialConfiguration> initialConfigurations =
				clientFactory.getCloudFacade(request).getInitialConfigurations(atomicServiceId, false);
		
		if(initialConfigurations != null && initialConfigurations.size() > 0) {
			String initialConfiguration = initialConfigurations.get(0).getId();
			log.info("Stopping AS instances for AS with id [{}] and initial configuration [{}] from workflow with id [{}]",
					new String[] {atomicServiceId, initialConfiguration, workflowId});
			clientFactory.getWorkflowManagement(request).removeAtomicServiceFromWorkflow(workflowId, initialConfiguration);
		} else {
			log.warn("Could not find initial configuration id to stop AS instances for id [{}] and workflow id [{}]",
					atomicServiceId, workflowId);
		}
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_START_ATOMIC_SERVICE)
	public String doViewStartAtomicService(@RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType,
			@RequestParam(value = PARAM_AS_REMOVAL_ERROR, required = false) String asRemovalError, Model model, PortletRequest request) {
		log.debug("Generating the atomic service startup parameters view for workflow type [{}]", workflowType);
		
		List<AtomicService> atomicServices = getSortedAtomicServices(request);
		filterAtomicService(atomicServices, workflowType);
		model.addAttribute(MODEL_BEAN_ATOMIC_SERVICES, atomicServices);
		model.addAttribute(PARAM_WORKFLOW_TYPE, workflowType);
		model.addAttribute(MODEL_BEAN_USER_NAME, portal.getUserName(request));
		
		if(!model.containsAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)) {
			model.addAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST, new StartAtomicServiceRequest());
		}
		
		if(asRemovalError != null) {
			model.addAttribute(PARAM_AS_REMOVAL_ERROR, asRemovalError);
		}
		
		return "cloudManager/atomicServiceStartupParams";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_START_ATOMIC_SERVICE)
	public void doActionStartAtomicService(@RequestParam(PARAM_ATOMIC_SERVICE_ID)
			String atomicServiceId, @RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType,
			@RequestParam(required = false, value = PARAM_USER_KEY_ID) String userKeyId,
			PortletRequest request, ActionResponse response) {
		log.info("Processing start atomic service request for [{}]", atomicServiceId);
		
		List<AtomicService> ass = getSortedAtomicServices(request);
		AtomicService atomicService = null;
		
		for(AtomicService as : ass) {
			if(as.getAtomicServiceId().equals(atomicServiceId)) {
				atomicService = as;
				break;
			}
		}
		
		List<String> workflowIds = getWorkflowIds(workflowType, request);
		String workflowId = null;
		
		if(workflowIds.size() == 0) {
			WorkflowStartRequest wsr = new WorkflowStartRequest();
			wsr.setName(workflowType.name() + " workflow");
			wsr.setType(workflowType);
			
			try {
				workflowId = clientFactory.getWorkflowManagement(request).startWorkflow(wsr);
			} catch (WorkflowStartException e) {
				log.warn("Error while starting workflow", e);
			}
		} else {
			workflowId = workflowIds.get(0);
		}
		
		log.debug("Retrieving initial configurations for atomic service with id [{}]", atomicServiceId);
		
		List<InitialConfiguration> initialconfigurations =
				clientFactory.getCloudFacade(request).getInitialConfigurations(atomicServiceId, false);
		
		if(initialconfigurations != null && initialconfigurations.size() == 0) {
			//no initial configurations available, creating default one
			clientFactory.getCloudFacade(request).addInitialConfiguration(atomicServiceId,
					createDefaultInitialConfiguration(atomicServiceId));
			//fetching the list again
			initialconfigurations = clientFactory.getCloudFacade(request).getInitialConfigurations(atomicServiceId, false);
		}
		
		if(initialconfigurations != null && initialconfigurations.size() > 0 &&
				initialconfigurations.get(0).getId() != null) {
			log.info("Starting atomic service instance for workflow [{}], configuration [{}]" +
					" and user key with id [{}]",
					new String[] {workflowId, initialconfigurations.get(0).getId(), userKeyId});
			
			clientFactory.getWorkflowManagement(request).addAtomicServiceToWorkflow(workflowId,
					initialconfigurations.get(0).getId(), atomicService.getName(), userKeyId);
		} else {
			log.warn("Configuration problem occurred during starting atomic service with id [{}]", atomicServiceId);
		}

		response.setRenderParameter(PARAM_CURRENT_ATOMIC_SERVICE, atomicServiceId);
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SAVE_ATOMIC_SERVICE)
	public String doViewSaveAtomicService(@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String atomicServiceInstanceId, Model model) {
		if(!model.containsAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST)) {
			SaveAtomicServiceRequest sasr = new SaveAtomicServiceRequest();
			sasr.setAtomicServiceInstanceId(atomicServiceInstanceId);
			model.addAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST, sasr);
		}
		
		return "cloudManager/saveAtomicService";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SAVE_ATOMIC_SERVICE)
	public void doActionSaveAtomicService(@ModelAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST)
			SaveAtomicServiceRequest saveAtomicServiceRequest, BindingResult errors, Model model,
			PortletRequest request, ActionResponse response) {
		validator.validate(saveAtomicServiceRequest, errors);
		
		if(errors.hasErrors()) {
			response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, saveAtomicServiceRequest.getAtomicServiceInstanceId());
			response.setRenderParameter(PARAM_ACTION, ACTION_SAVE_ATOMIC_SERVICE);
		} else {
			NewAtomicService atomicService = new NewAtomicService();
			atomicService.setSourceAsiId(saveAtomicServiceRequest.getAtomicServiceInstanceId());
			atomicService.setName(saveAtomicServiceRequest.getName());
			atomicService.setDescription(saveAtomicServiceRequest.getDescription());
			atomicService.setProxyConfigurationName(saveAtomicServiceRequest.getProxyConfiguration());
			atomicService.setPublished(saveAtomicServiceRequest.isPublished());
			atomicService.setScalable(saveAtomicServiceRequest.isScalable());
			atomicService.setShared(saveAtomicServiceRequest.isShared());
			
			log.info("Saving new atomic service for instance id [{}]", saveAtomicServiceRequest.getAtomicServiceInstanceId());				
			
			String atomicServiceId = clientFactory.getCloudFacade(request).createAtomicService(atomicService);
			
			if(atomicServiceId != null) {
				log.debug("Successfully retrieved new atomic service id of value [{}]", atomicServiceId);
				
				InitialConfiguration ic = createDefaultInitialConfiguration(atomicServiceId);
				
				try {
					clientFactory.getCloudFacade(request).addInitialConfiguration(atomicServiceId, ic);
				} catch (Exception e) {
					log.warn("Could not save initial cofiguration for atomic service with id [{}]", atomicServiceId, e);
				}
				
				response.setRenderParameter(PARAM_ACTION, ACTION_AS_SAVING_STATE);
				response.setRenderParameter(PARAM_ATOMIC_SERVICE_ID, atomicServiceId);
			} else {
				log.warn("New atomic service id is null");
				errors.addError(new FieldError(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST, "name",
						"Could not create new AS. Maybe the name is not unique?"));
				response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, saveAtomicServiceRequest.getAtomicServiceInstanceId());
				response.setRenderParameter(PARAM_ACTION, ACTION_SAVE_ATOMIC_SERVICE);
			}
		}
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_INVOKE_ATOMIC_SERVICE)
	public String doViewInvokeAtomicService(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String atomicServiceInstanceId,
			@RequestParam(required = false, value = PARAM_INVOCATION_RESULT)
			String invocationResult, @RequestParam(required = false, value = PARAM_INVOCATION_CODE)
			String invocationCode, Model model, PortletRequest request) {
		log.debug("Atomic service invocation request called for AS id [{}] and AS instance id [{}]", atomicServiceId, atomicServiceInstanceId);
		
		List<AtomicService> atomicServices = clientFactory.getCloudFacade(request).getAtomicServices();
		AtomicService atomicService = null;
		
		for(AtomicService as : atomicServices) {
			if(as.getAtomicServiceId().equals(atomicServiceId)) {
				atomicService = as;
				break;
			}
		}
		
		String configurationId = null;
		String workflowId = null;
		List<InitialConfiguration> initialConfigurations = clientFactory.getCloudFacade(request).
				getInitialConfigurations(atomicServiceId, false);
		
		if(initialConfigurations != null && initialConfigurations.size() > 0) {
			configurationId = initialConfigurations.get(0).getId();
		}
		
		if(getWorkflowIds(WorkflowType.portal, request) != null &&
				getWorkflowIds(WorkflowType.portal, request).size() > 0) {
			workflowId = getWorkflowIds(WorkflowType.portal, request).get(0);
		}
		
		if(atomicService != null && configurationId != null && workflowId != null) {
			List<Endpoint> webAppEndpoints = new ArrayList<>();
			List<Endpoint> wsEndpoints = new ArrayList<>();
			List<Endpoint> endpoints = clientFactory.getWorkflowManagement(request).getEndpoints(workflowId, atomicServiceInstanceId);
			List<Redirection> redirections = clientFactory.getWorkflowManagement(request).getRedirections(workflowId, atomicServiceInstanceId);

			if(endpoints != null) {
				for(Endpoint endpoint : endpoints) {
					fixPaths(endpoint);
					
					switch(endpoint.getType()) {
						case WEBAPP:
							webAppEndpoints.add(endpoint);

							//temporary fix to pass NoMachine port
							if(endpoint.getInvocationPath().contains("/plugin")) {
								if(redirections != null) {
									for(Redirection redirect : redirections) {
										if(redirect.getName().equalsIgnoreCase("ssh")) {
											model.addAttribute("additionalQuery", "?nxport=" + redirect.getFromPort() +
													"&nxhost=" + redirect.getHost());
											
											break;
										}
									}
								}
							}
						break;
						case REST:
							model.addAttribute(MODEL_BEAN_ATOMIC_SERVICE_METHOD_LIST, Arrays.asList(
									endpoint.getInvocationPath()));
							InvokeAtomicServiceRequest iasr = null;
							Redirection redirection = findRedirection(endpoint.getPort(), redirections);
							
							if(!model.containsAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST)) {
								iasr = new InvokeAtomicServiceRequest();
								iasr.setAtomicServiceInstanceId(atomicServiceInstanceId);
								iasr.setWorkflowId(workflowId);
								iasr.setConfigurationId(configurationId);
								iasr.setAtomicServiceId(atomicServiceId);
								iasr.setFormFields(new ArrayList<FormField>());
								iasr.setPostfix(redirection.getPostfix());
								iasr.setPort(redirection.getFromPort());
								iasr.setHost(redirection.getHost());
								
								if(endpoint.getInvocationPath() != null) {
									Pattern pattern = Pattern.compile("\\{(.+?)\\}");
									Matcher matcher = pattern.matcher(endpoint.getInvocationPath());
									
									while(matcher.find()) {
										FormField formField = new FormField();
										formField.setName(matcher.group(1));
										formField.setValue("");
										iasr.getFormFields().add(formField);
									}
									
									iasr.setInvocationPath(endpoint.getInvocationPath());
								}
								
								log.trace("For REST invocation the following parameters were found: [{}]", iasr.getFormFields());
								
								model.addAttribute(MODEL_BEAN_AS_INVOCATION_POSSIBLE, true);
								model.addAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST, iasr);
							} else {
								iasr = (InvokeAtomicServiceRequest) model.asMap().get(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST);
							}
							
							
							
							model.addAttribute(MODEL_BEAN_INVOCATION_PATH, createInvocationPath(redirection.getHost(), redirection.getFromPort(),
									redirection.getPostfix()));
						break;
						case WS:
							wsEndpoints.add(endpoint);
						break;
					}
				}
			}
			
			model.addAttribute(MODEL_BEAN_ENDPOINT_LINKS, createEndpointLinks(endpoints, redirections, false, null, false));
			model.addAttribute(MODEL_BEAN_AUTH_ENDPOINT_LINKS, createEndpointLinks(endpoints, redirections, true, request, false));
			model.addAttribute(MODEL_BEAN_WEBAPP_ENDPOINTS, webAppEndpoints);
			model.addAttribute(MODEL_BEAN_WS_ENDPOINTS, wsEndpoints);
			model.addAttribute(MODEL_BEAN_ASI_REDIRECTIONS, redirections);
		} else {
			model.addAttribute(MODEL_BEAN_AS_INVOCATION_POSSIBLE, false);
			model.addAttribute(MODEL_BEAN_NEGATIVE_MESSAGE,
					messages.getMessage("cloud.manager.portlet.no.atomic.service.found", null, null));
		}
		
		if(invocationResult != null) {
			model.addAttribute(MODEL_BEAN_AS_INVOCATION_POSSIBLE, true);
			model.addAttribute(PARAM_INVOCATION_RESULT, invocationResult);
			model.addAttribute(PARAM_INVOCATION_CODE, invocationCode);
		}
		
		return "cloudManager/invokeAtomicService";
	}

	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_INVOKE_ATOMIC_SERVICE)
	public void doActionInvokeAtomicService(@ModelAttribute(MODEL_BEAN_INVOKE_ATOMIC_SERVICE_REQUEST)
			InvokeAtomicServiceRequest invokeAtomicServiceRequest, PortletRequest request, ActionResponse response) {
		String result = null;
		String resultCode = null;
		log.info("Invoking service with the following request [{}]", invokeAtomicServiceRequest);
		
		try {
			String responseValue = invokeAtomicService(request, invokeAtomicServiceRequest);
			log.trace("AS invocation result is [{}]", responseValue);
			resultCode = responseValue.substring(0, responseValue.indexOf(":"));
			
			if(resultCode.equals("401")) {
				resultCode += " - Forbidden";
			} else if(resultCode.equals("200")) {
				resultCode += " - OK";
			}
			
			result = responseValue.substring(responseValue.indexOf(":") + 1);
		} catch (Exception e) {
			log.warn("AS invocation error occurred!", e);
			result = "Error occurred: " + e.getMessage();
			resultCode = "none";
		}
		
		response.setRenderParameter(PARAM_ACTION, ACTION_INVOKE_ATOMIC_SERVICE);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_ID, invokeAtomicServiceRequest.getAtomicServiceId());
		response.setRenderParameter(PARAM_INVOCATION_RESULT, HtmlUtils.htmlEscape(result));
		response.setRenderParameter(PARAM_INVOCATION_CODE, resultCode);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID,
				invokeAtomicServiceRequest.getAtomicServiceInstanceId());
	}
	
	@ResourceMapping("instanceStatus")
	public void checkInstanceStatus(@RequestParam(PARAM_WORKFLOW_ID) String workflowId,
			@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String instanceId,
			PortletRequest request, ResourceResponse response) {
		log.trace("Processing atomic service instance status request for workflow [{}], atomic service [{}] and instance [{}]",
				new String[] {workflowId, atomicServiceId, instanceId});
		
		Workflow workflow = clientFactory.getWorkflowManagement(request).getWorkflow(workflowId);
		boolean statusRetrieved = false;
		
		if(workflow != null && workflow.getAtomicServiceInstances() != null) {
			for(AtomicServiceInstance asi : workflow.getAtomicServiceInstances()) {
				if(asi.getId() != null && asi.getId().equals(instanceId)) {
					try {
						response.getWriter().write(asi.getStatus().toString());
					} catch (IOException e) {
						log.warn("Could not write instance status to the http writer", e);
					}
					
					statusRetrieved = true;
					
					break;
				}
			}
		}
		
		if(!statusRetrieved) {
			log.warn("Could not retrieve status for workflow [{}] and atomic service [{}]. Returning empty status.", workflowId, atomicServiceId);
			
			try {
				response.getWriter().write("");
			} catch (IOException e) {
				log.warn("Could not write instance status to the http writer", e);
			}
		}
	}
	
	@ResourceMapping("accessMethods")
	public void getAccessMethods(@RequestParam(PARAM_WORKFLOW_ID) String workflowId,
			@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String instanceId,
			PortletRequest request, ResourceResponse response) {
		log.trace("Processing atomic service instance access methods request for workflow [{}] and instance [{}]",
				new String[] {workflowId, instanceId});
		
		Workflow workflow = clientFactory.getWorkflowManagement(request).getWorkflow(workflowId);
		boolean accessMethodsRetrieved = false;
		
		if(workflow != null && workflow.getAtomicServiceInstances() != null) {
			for(AtomicServiceInstance asi : workflow.getAtomicServiceInstances()) {
				if(asi.getId() != null && asi.getId().equals(instanceId)) {
					try {
						if(asi.getRedirections() != null && asi.getRedirections().size() > 0) {
							StringBuilder builder = new StringBuilder();
							
							for(Redirection redirection : asi.getRedirections()) {
								if(redirection.getName() != null) {
									builder.append(redirection.getName()).append(":")
											.append(redirection.getHost()).append(":")
											.append(redirection.getFromPort());

									if(redirection.getName().equals("ssh") && asi.getPublicKeyId() != null) {
										List<PublicKeyInfo> keys = clientFactory.getKeyManagement(request).list();
										String keyName = null;
										
										for(PublicKeyInfo pki : keys) {
											if(pki.getId().equals(asi.getPublicKeyId())) {
												keyName = pki.getKeyName();
												break;
											}
										}
										
										if(keyName != null) {
											builder.append(":").append(keyName);
										}
									}
								}
								
								builder.append("|");
							}
							
							if(builder.length() > 0) {
								builder.deleteCharAt(builder.length() - 1);
							}
							
							log.trace("Returning the following redirection sequence: [{}]", builder.toString());
							response.getWriter().write(builder.toString());
							accessMethodsRetrieved = true;
						}
					} catch (IOException e) {
						log.warn("Could not write instance status to the http writer", e);
					}

					break;
				}
			}
		}
		
		if(!accessMethodsRetrieved) {
			log.warn("Could not retrieve access methods for workflow [{}] and atomic service instance [{}]. " +
					"Returning empty status.", workflowId, instanceId);
			
			try {
				response.getWriter().write("");
			} catch (IOException e) {
				log.warn("Could not write instance status to the http writer", e);
			}
		}
	}
	
	@ResourceMapping("workflows")
	public void doResourceGetWorkflows(PortletRequest request, ResourceResponse response) {
		List<String> workflowIds = getWorkflowIds(WorkflowType.workflow, request);
		StringBuilder builder = new StringBuilder();
		
		if(workflowIds.size() > 0) {
			for(String workflowId : workflowIds) {
				Map<AtomicService, List<AtomicServiceInstance>> atomicServiceInstances =
						getAtomicServiceInstances(workflowId, request);
				builder.append(workflowId);
				
				PortletURL stopUrl = response.createActionURL();
				stopUrl.setParameter(PARAM_ACTION, ACTION_STOP_WORKFLOW);
				stopUrl.setParameter(PARAM_WORKFLOW_TYPE, WorkflowType.workflow.name());
				stopUrl.setParameter(PARAM_WORKFLOW_ID, workflowId);
				builder.append(";" + stopUrl.toString());
				
				for(AtomicService atomicService : atomicServiceInstances.keySet()) {
					if(atomicServiceInstances.get(atomicService) != null &&
							atomicServiceInstances.get(atomicService).size() > 0) {
						builder.append(";").append(atomicService.getName());
						
						for(AtomicServiceInstance asi : atomicServiceInstances.get(atomicService)) {
							builder.append(":").append(asi.getId()).append(":").append(asi.getStatus());
						}
					}
				}
				
				builder.append("|");
			}
			
			builder.deleteCharAt(builder.length() - 1);
		} else {
			builder.append("");
		}
		
		try {
			response.getWriter().write(builder.toString());
		} catch (IOException e) {
			log.warn("Could not write to http response writer during workflow information retireval", e);
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_AS_SAVING_STATE)
	public String doViewAsSavingState(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			Model model) {
		log.trace("Generating AS saving state view for id [{}]", atomicServiceId);
		model.addAttribute(PARAM_ATOMIC_SERVICE_ID, atomicServiceId);
		
		return "cloudManager/asSavingState";
	}
	
	@ResourceMapping("asSavingStatus")
	public void checkAsStatus(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			PortletRequest request, ResourceResponse response) {
		List<AtomicService> atomicServices = clientFactory.getCloudFacade(request).getAtomicServices();
		
		for(AtomicService as : atomicServices) {
			if(as.getAtomicServiceId() != null && as.getAtomicServiceId().equals(atomicServiceId)) {
				try {
					if(as.isActive()) {
						response.getWriter().write("done");
					} else {
						response.getWriter().write("saving");
					}
					
					return;
				} catch (IOException e) {
					log.warn("Could not write atomic service status to the http writer", e);
				}
			}
		}
		
		try {
			response.getWriter().write("unknown AS with id " + atomicServiceId);
		} catch (IOException e) {
			log.warn("Could not write atomic service status to the http writer", e);
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_USER_KEYS)
	public String doViewListUserKeys(PortletRequest portletRequest, Model model) {
		List<PublicKeyInfo> keys = clientFactory.getKeyManagement(portletRequest).list();
		model.addAttribute(MODEL_BEAN_USER_KEYS, keys);
		
		if(!model.containsAttribute(MODEL_BEAN_UPLOAD_KEY_REQUEST)) {
			model.addAttribute(MODEL_BEAN_UPLOAD_KEY_REQUEST, new UploadKeyRequest());
		}
		
		return "cloudManager/userKeys";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_KEY)
	public void doActionUploadKey(@ModelAttribute(MODEL_BEAN_UPLOAD_KEY_REQUEST) UploadKeyRequest uploadKeyRequest,
			BindingResult errors, Model model, PortletRequest request, ActionResponse response) {
		log.debug("Processing upload key request for key [{}]", uploadKeyRequest);
		validator.validate(uploadKeyRequest, errors);
		
		if(uploadKeyRequest.getKeyBody() == null || uploadKeyRequest.getKeyBody().isEmpty()) {
			errors.addError(new FieldError(MODEL_BEAN_UPLOAD_KEY_REQUEST, "keyBody",
					"The key file has to be chosen"));
		}
		
		if(errors.hasErrors()) {
			response.setRenderParameter(PARAM_ACTION, ACTION_USER_KEYS);
		} else {
			if(!errors.hasErrors()) {
				StringWriter keyWriter = new StringWriter();
				
				try {
					IOUtils.copy(uploadKeyRequest.getKeyBody().getInputStream(), keyWriter);
					clientFactory.getKeyManagement(request).add(uploadKeyRequest.getKeyName(), keyWriter.toString());
				} catch (IOException e) {
					log.warn("Could not copy public key contents for key with name [" +
							uploadKeyRequest.getKeyName() + "]", e);
					errors.addError(new FieldError(MODEL_BEAN_UPLOAD_KEY_REQUEST, "keyBody",
							"Could not copy key contents"));
				} catch (WrongKeyFormatException e) {
					log.warn("Wrong key format for key with name [" +
							uploadKeyRequest.getKeyName() + "]", e);
					errors.addError(new FieldError(MODEL_BEAN_UPLOAD_KEY_REQUEST, "keyBody",
							"Format of the given key is invalid"));
				} catch (KeyAlreadyExistsException e) {
					log.warn("Key name [" +
							uploadKeyRequest.getKeyName() + "] already exists", e);
					errors.addError(new FieldError(MODEL_BEAN_UPLOAD_KEY_REQUEST, "keyName",
							"Given key name is already taken"));
				}
				
				if(errors.hasErrors()) {
					response.setRenderParameter(PARAM_ACTION, ACTION_USER_KEYS);
				} else {
	 				response.setRenderParameter(PARAM_ACTION, ACTION_USER_KEYS);
					model.addAttribute(MODEL_BEAN_UPLOAD_KEY_REQUEST, new UploadKeyRequest());
				}
			}
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_REMOVE_KEY)
	public void doActionRemoveKey(@RequestParam(PARAM_USER_KEY_ID) String keyId,
			PortletRequest request, ActionResponse response) {
		log.debug("Removing user key for id [{}]", keyId);
		clientFactory.getKeyManagement(request).delete(keyId);
		response.setRenderParameter(PARAM_ACTION, ACTION_USER_KEYS);
	}
	
	@ResourceMapping("getUserKey")
	public void getUserKey(@RequestParam("keyId") String keyId, PortletRequest request, ResourceResponse response) {
		String keyBody = clientFactory.getKeyManagement(request).get(keyId);
		
		if(keyBody != null) {
			response.addProperty("Content-Disposition", "Attachment;Filename=\"key.txt\"");
			response.addProperty("Pragma", "public");
			response.addProperty("Cache-Control", "must-revalidate");
			response.setContentLength(keyBody.length());
			
			try {
				FileCopyUtils.copy(new StringReader(keyBody), response.getWriter());
			} catch (Exception e) {
				log.warn("Could not serve user key for id  [{}]", keyId);
			}
		} else {
			log.warn("User key with id [{}] does not exist", keyId);
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_PICK_USER_KEY)
	public String doViewPickUserKey(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			@RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType, Model model, PortletRequest request) {
		model.addAttribute(PARAM_ATOMIC_SERVICE_ID, atomicServiceId);
		model.addAttribute(PARAM_WORKFLOW_TYPE, workflowType);
		
		List<PublicKeyInfo> keys = clientFactory.getKeyManagement(request).list();
		Map<String, String> keyMap = new HashMap<String, String>();
		
		for(PublicKeyInfo keyInfo : keys) {
			keyMap.put(keyInfo.getId(), keyInfo.getKeyName());
		}
		
		model.addAttribute(MODEL_BEAN_USER_KEYS, keyMap);
		
		if(!model.containsAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST)) {
			StartAtomicServiceRequest startRequest = new StartAtomicServiceRequest();
			startRequest.setAtomicServiceId(atomicServiceId);
			startRequest.setWorkflowType(workflowType);
			
			if(keyMap.size() > 0) {
				startRequest.setUserKeyId(keyMap.keySet().iterator().next());
			}
			
			model.addAttribute(MODEL_BEAN_START_ATOMIC_SERVICE_REQUEST, startRequest);
		}
		
		return "cloudManager/pickUserKey";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_EDIT_ENDPOINTS)
	public String doViewEditEndpoints(@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String atomicServiceInstanceId,
			@RequestParam(PARAM_WORKFLOW_ID) String workflowId, Model model, PortletRequest request) {
		log.debug("Generating endpoint and redirection management view for workflow id [{}] and atomic service instance id [{}]", workflowId, atomicServiceInstanceId);
		List<Endpoint> endpoints = clientFactory.getWorkflowManagement(request).getEndpoints(workflowId, atomicServiceInstanceId);
		List<Redirection> redirections = clientFactory.getWorkflowManagement(request).getRedirections(workflowId, atomicServiceInstanceId);
		model.addAttribute(MODEL_BEAN_ENDPOINTS, endpoints);
		model.addAttribute(MODEL_BEAN_REDIRECTIONS, redirections);;
		
		Map<String, String> endpointTypes = new HashMap<>();
		
		for(EndpointType type : EndpointType.values()) {
			endpointTypes.put(type.name(), messages.getMessage("cloud.manager.portlet.endpoint." + type + ".label", null, null));
		}
		
		model.addAttribute(MODEL_BEAN_ENDPOINT_TYPES, endpointTypes);
		
		Map<String, String> redirectionTypes = new HashMap<>();
		
		for(RedirectionType type : RedirectionType.values()) {
			if(type != RedirectionType.UDP) {
				redirectionTypes.put(type.name(), messages.getMessage("cloud.manager.portlet.redirection." + type + ".label", null, null));
			}
		}
		
		model.addAttribute(MODEL_BEAN_REDIRECTION_TYPES, redirectionTypes);
		
		Map<String, String> redirectionSelection = new HashMap<>();
		
		for(Redirection redirection : redirections) {
			if(redirection.getType() == RedirectionType.HTTP) {
				redirectionSelection.put(String.valueOf(redirection.getToPort()), redirection.getName() + "(" + redirection.getToPort() + ")");
			}
		}
		
		model.addAttribute(MODEL_BEAN_REDIRECTION_SELECTION, redirectionSelection);
		
		Map<String, String> endpointLinks = createEndpointLinks(endpoints,
				redirections, false, null, false);
		Map<String, String> breakingEndpointLinks = createEndpointLinks(endpoints,
				redirections, false, null, true);
		
		model.addAttribute(MODEL_BEAN_ENDPOINT_LINKS, endpointLinks);
		model.addAttribute(MODEL_BEAN_BREAKING_ENDPOINT_LINKS, breakingEndpointLinks);
		
		if(!model.containsAttribute(MODEL_BEAN_ADD_ENDPOINT_REQUEST)) {
			AddEndpointRequest addEndpointRequest = new AddEndpointRequest();
			addEndpointRequest.setAtomicServiceInstanceId(atomicServiceInstanceId);
			addEndpointRequest.setWorkflowId(workflowId);
			model.addAttribute(MODEL_BEAN_ADD_ENDPOINT_REQUEST, addEndpointRequest);
		}
		
		if(!model.containsAttribute(MODEL_BEAN_ADD_REDIRECTION_REQUEST)) {
			AddRedirectionRequest addRedirectionRequest = new AddRedirectionRequest();
			addRedirectionRequest.setAtomicServiceInstanceId(atomicServiceInstanceId);
			addRedirectionRequest.setWorkflowId(workflowId);
			model.addAttribute(MODEL_BEAN_ADD_REDIRECTION_REQUEST, addRedirectionRequest);
		}
		
		return "cloudManager/editEndpoints";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_ADD_ENDPOINT)
	public void doActionAddEndpoint(@ModelAttribute(MODEL_BEAN_ADD_ENDPOINT_REQUEST) AddEndpointRequest addEndpointRequest,
			BindingResult errors, Model model, PortletRequest request, ActionResponse response) {
		log.debug("Adding new endpoint with request [{}]", addEndpointRequest);
		validator.validate(addEndpointRequest, errors);
		
		if(!errors.hasErrors()) {
			List<Endpoint> endpoints = clientFactory.getWorkflowManagement(request).
					getEndpoints(addEndpointRequest.getWorkflowId(), addEndpointRequest.getAtomicServiceInstanceId());
			
			for(Endpoint endpoint : endpoints) {
				if(endpoint.getInvocationPath().equals(addEndpointRequest.getInvocationPath())) {
					errors.addError(new FieldError(MODEL_BEAN_ADD_ENDPOINT_REQUEST, "invocationPath",
							messages.getMessage("cloud.manager.portlet.endpoint.invocation.path.taken.error.message", null, null)));
				}
			}
			
			if(!errors.hasErrors()) {
				Endpoint endpoint = new Endpoint();
				endpoint.setInvocationPath(addEndpointRequest.getInvocationPath());
				endpoint.setDescription(addEndpointRequest.getDescription());
				endpoint.setDescriptor(addEndpointRequest.getDescriptor());
				endpoint.setPort(addEndpointRequest.getPort());
				endpoint.setType(addEndpointRequest.getType());
				clientFactory.getWorkflowManagement(request).addEndpoint(addEndpointRequest.getWorkflowId(),
						addEndpointRequest.getAtomicServiceInstanceId(), endpoint);
				
				AddEndpointRequest newEndpointRequest = new AddEndpointRequest();
				newEndpointRequest.setAtomicServiceInstanceId(addEndpointRequest.getAtomicServiceInstanceId());
				newEndpointRequest.setWorkflowId(addEndpointRequest.getWorkflowId());
				model.addAttribute(MODEL_BEAN_ADD_ENDPOINT_REQUEST, newEndpointRequest);
			}
		}
		
		response.setRenderParameter(PARAM_ACTION, ACTION_EDIT_ENDPOINTS);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, addEndpointRequest.getAtomicServiceInstanceId());
		response.setRenderParameter(PARAM_WORKFLOW_ID, addEndpointRequest.getWorkflowId());
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_REMOVE_ENDPOINT)
	public void doActionRemoveEndpoint(@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String atomicServiceInstanceId,
			@RequestParam(PARAM_ENDPOINT_ID) String endpointId, @RequestParam(PARAM_WORKFLOW_ID) String workflowId,
			PortletRequest request, ActionResponse response) {
		log.debug("Removing endpoint for atomic service instance [{}] and endpoint id [{}]", atomicServiceInstanceId, endpointId);
		clientFactory.getWorkflowManagement(request).deleteEndpoint(workflowId, atomicServiceInstanceId, endpointId);
		response.setRenderParameter(PARAM_ACTION, ACTION_EDIT_ENDPOINTS);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, atomicServiceInstanceId);
		response.setRenderParameter(PARAM_WORKFLOW_ID, workflowId);
	}
	
	@ExceptionHandler(Exception.class)
	public String handleExceptions(Exception e) {
		log.error("Unexpected exception occurred", e);
		
		return "fatal/error";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_ADD_REDIRECTION)
	public void doActionAddRedirection(@ModelAttribute(MODEL_BEAN_ADD_REDIRECTION_REQUEST) AddRedirectionRequest addRedirectionRequest,
			BindingResult errors, Model model, PortletRequest request, ActionResponse response) {
		log.debug("Adding new redirection with request [{}]", addRedirectionRequest);
		validator.validate(addRedirectionRequest, errors);
		
		if(!errors.hasErrors()) {
			List<Redirection> redirections = clientFactory.getWorkflowManagement(request).
					getRedirections(addRedirectionRequest.getWorkflowId(), addRedirectionRequest.getAtomicServiceInstanceId());
			for(Redirection redirection : redirections) {
				if(redirection.getName().equals(addRedirectionRequest.getName())) {
					errors.addError(new FieldError(MODEL_BEAN_ADD_REDIRECTION_REQUEST, "name",
							messages.getMessage("cloud.manager.portlet.redirection.name.taken.error.message", null, null)));
				}
				
				if(redirection.getToPort().equals(addRedirectionRequest.getToPort())) {
					errors.addError(new FieldError(MODEL_BEAN_ADD_REDIRECTION_REQUEST, "toPort",
							messages.getMessage("cloud.manager.portlet.redirection.to.port.taken.error.message", null, null)));
				}
			}
			
			if(!errors.hasErrors()) {
				clientFactory.getWorkflowManagement(request).addRedirection(addRedirectionRequest.getWorkflowId(),
						addRedirectionRequest.getAtomicServiceInstanceId(), addRedirectionRequest.getName(),
						addRedirectionRequest.getToPort(), addRedirectionRequest.getType());
				
				AddRedirectionRequest newAddRedirectionRequest = new AddRedirectionRequest();
				newAddRedirectionRequest.setAtomicServiceInstanceId(addRedirectionRequest.getAtomicServiceInstanceId());
				newAddRedirectionRequest.setWorkflowId(addRedirectionRequest.getWorkflowId());
				model.addAttribute(MODEL_BEAN_ADD_REDIRECTION_REQUEST, newAddRedirectionRequest);
			}
		}
		
		response.setRenderParameter(PARAM_ACTION, ACTION_EDIT_ENDPOINTS);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, addRedirectionRequest.getAtomicServiceInstanceId());
		response.setRenderParameter(PARAM_WORKFLOW_ID, addRedirectionRequest.getWorkflowId());
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_REMOVE_REDIRECTION)
	public void doActionRemoveRedirection(@RequestParam(PARAM_ATOMIC_SERVICE_INSTANCE_ID) String atomicServiceInstanceId,
			@RequestParam(PARAM_REDIRECTION_ID) String redirectionId, @RequestParam(PARAM_WORKFLOW_ID) String workflowId,
			PortletRequest request, ActionResponse response) {
		log.debug("Removing redirection for atomic service instance id [{}] and redirection id [{}]", atomicServiceInstanceId, redirectionId);
		
		List<Endpoint> endpoints = clientFactory.getWorkflowManagement(request).getEndpoints(workflowId, atomicServiceInstanceId);
		List<Redirection> redirections = clientFactory.getWorkflowManagement(request).getRedirections(workflowId, atomicServiceInstanceId);
		Redirection redirection = null;
		
		for(Redirection r : redirections) {
			if(r.getId().equals(redirectionId)) {
				redirection = r;
				
				break;
			}
		}
		
		if(redirection != null) {
			for(Endpoint endpoint : endpoints) {
				if(endpoint.getPort().equals(redirection.getToPort())) {
					clientFactory.getWorkflowManagement(request).deleteEndpoint(workflowId, atomicServiceInstanceId, endpoint.getId());
				}
			}
		}
		
		clientFactory.getWorkflowManagement(request).deleteRedirection(workflowId, atomicServiceInstanceId, redirectionId);
		response.setRenderParameter(PARAM_ACTION, ACTION_EDIT_ENDPOINTS);
		response.setRenderParameter(PARAM_ATOMIC_SERVICE_INSTANCE_ID, atomicServiceInstanceId);
		response.setRenderParameter(PARAM_WORKFLOW_ID, workflowId);
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_REMOVE_AS)
	public void doActionRemoveAs(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			@RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType, PortletRequest request,
			ActionResponse response) {
		log.debug("Processing atomic service removal action for {}", atomicServiceId);
		
		try {
			clientFactory.getCloudFacade(request).deleteAtomicService(atomicServiceId);
		} catch(Exception e) {
			log.warn("Could not remove atomic service with id {}", atomicServiceId);
			response.setRenderParameter(PARAM_AS_REMOVAL_ERROR, "true");
		}
		
		response.setRenderParameter(PARAM_ACTION, ACTION_START_ATOMIC_SERVICE);
		response.setRenderParameter(PARAM_WORKFLOW_TYPE, workflowType.name());
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_EDIT_AS)
	public String doViewEditAs(@RequestParam(PARAM_ATOMIC_SERVICE_ID) String atomicServiceId,
			@RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType,
			Model model, PortletRequest request) {
		model.addAttribute(PARAM_WORKFLOW_TYPE, workflowType);
		
		if(!model.containsAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST)) {
			AtomicService as = getAtomicService(atomicServiceId, clientFactory.getCloudFacade(request).getAtomicServices());
			SaveAtomicServiceRequest sasr = new SaveAtomicServiceRequest();
			sasr.setName(as.getName());
			sasr.setDescription(as.getDescription());
			sasr.setProxyConfiguration(as.getProxyConfigurationName());
			sasr.setPublished(as.isPublished());
			sasr.setScalable(as.isScalable());
			sasr.setShared(as.isShared());
			sasr.setAtomicServiceId(atomicServiceId);
			sasr.setAtomicServiceInstanceId(" ");
			model.addAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST, sasr);
		}
		
		return "cloudManager/saveAtomicService";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_EDIT_AS)
	public void doActionEditAs(@RequestParam(PARAM_WORKFLOW_TYPE) WorkflowType workflowType,
			@ModelAttribute(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST) SaveAtomicServiceRequest editAs,
			BindingResult errors, PortletRequest request, ActionResponse response) {
		validator.validate(editAs, errors);
		
		if(!validateProxyConfiguration(editAs, request)) {
			errors.addError(new FieldError(MODEL_BEAN_SAVE_ATOMIC_SERVICE_REQUEST, "proxyConfiguration",
					messages.getMessage("cloud.manager.portlet.wrong.proxy.configuration.name.message", null, null)));
		}
		
		if(!errors.hasErrors()) {
			AtomicServiceRequest asr = new AtomicServiceRequest();
			asr.setName(editAs.getName());
			asr.setDescription(editAs.getDescription());
			asr.setProxyConfigurationName(editAs.getProxyConfiguration());
			asr.setPublished(editAs.isPublished());
			asr.setScalable(editAs.isScalable());
			asr.setShared(editAs.isShared());
			clientFactory.getCloudFacade(request).updateAtomicService(editAs.getAtomicServiceId(), asr);
			response.setRenderParameter(PARAM_ACTION, ACTION_START_ATOMIC_SERVICE);
			response.setRenderParameter(PARAM_WORKFLOW_TYPE, workflowType.name());
		} else {
			response.setRenderParameter(PARAM_ACTION, ACTION_EDIT_AS);
			response.setRenderParameter(PARAM_ATOMIC_SERVICE_ID, editAs.getAtomicServiceId());
			response.setRenderParameter(PARAM_WORKFLOW_TYPE, workflowType.name());
		}
	}

	private boolean validateProxyConfiguration(SaveAtomicServiceRequest editAs, PortletRequest request) {
		List<String> securityConfigurations = clientFactory.getSecurityConfigurationService(request).list();
		
		if(securityConfigurations.contains(editAs.getProxyConfiguration())) {
			return true;
		} else {
			return false;
		}
	}

	private void filterAtomicService(List<AtomicService> atomicServices, WorkflowType workflowType) {
		for(Iterator<AtomicService> i = atomicServices.iterator(); i.hasNext();) {
			AtomicService atomicService = i.next();
			
			if(atomicService.isDevelopment()) {
				i.remove();
				continue;
			}
			
			if(workflowType != WorkflowType.development) {
				if(!atomicService.isPublished()) {
					i.remove();
				}
			}
		}
	}

	private String invokeAtomicService(PortletRequest portletRequest, InvokeAtomicServiceRequest request) throws NoSuchMessageException, IOException {
		String urlPath = createInvocationPath(request.getHost(), request.getPort(), request.getPostfix()) +
				request.getInvocationPath().trim();
		
		for(FormField field : request.getFormFields()) {
			urlPath = urlPath.replace("{" + field.getName() + "}", field.getValue());
		}
		
		log.debug("URL for service invocation is [{}]", urlPath);
		
		int responseCode = 0;
		StringBuilder response = new StringBuilder();
		HttpURLConnection connection = null;
		
		try {
			URL asUrl = new URL(urlPath);
			connection = (HttpURLConnection) asUrl.openConnection();
			connection.setRequestProperty(ClientFactory.HEADER_AUTHORIZATION, httpUtil.createBasicAuthenticationHeaderValue(null, portal.getUserToken(portletRequest)));
			connection.setDoOutput(true);
			
	//		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	//		out.write(request.getMessageBody());
	//		out.flush();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			
			while((line = in.readLine()) != null) {
				response.append(line).append("\n");
			}
			
	//		out.close();
			in.close();
			
			responseCode = connection.getResponseCode();
			log.debug("Service invocation headear names and values: [{}]", connection.getHeaderFields());
		} catch (IOException e) {
			if(connection != null) {
				responseCode = connection.getResponseCode();
				response.append(connection.getResponseMessage());
			}
		}
		
		return String.valueOf(responseCode) + ":" + response.toString();
	}
	
	private String createInvocationPath(String host, Integer port, String postfix) {
		String urlPath = messages.getMessage("cloud.manager.portlet.hello.as.postfix.endpoint.template", null, null).
				replace("{host}", host).replace("{port}", String.valueOf(port)).replace("{postfix}", postfix);
		
		return urlPath.trim();
	}
	
	private List<String> getWorkflowIds(WorkflowType workflowType, PortletRequest request) {
		List<String> result = new ArrayList<String>();
		UserWorkflows userWorkflows = clientFactory.getWorkflowManagement(request).getWorkflows();
		
		if(userWorkflows != null) {
			if(userWorkflows.getWorkflows() != null) {
				for(WorkflowBaseInfo workflow : userWorkflows.getWorkflows()) {
					if(workflow.getType() == workflowType) {
						result.add(workflow.getId());
					}
				}
			}
		} else {
			log.warn("User workflows bean is null for workflow type [{}]", workflowType);
		}
		
		return result;
	}
	
	private Map<AtomicService, List<AtomicServiceInstance>> getAtomicServiceInstances(String workflowId, PortletRequest request) {
		log.debug("Retrieving atomic service instances for workflow id [{}]", workflowId);
		
		Map<AtomicService, List<AtomicServiceInstance>> result = new LinkedHashMap<AtomicService, List<AtomicServiceInstance>>();
		Workflow workflow = clientFactory.getWorkflowManagement(request).getWorkflow(workflowId);
		List<AtomicService> atomicServices = clientFactory.getCloudFacade(request).getAtomicServices();
		
		if(workflow != null && workflow.getAtomicServiceInstances() != null) {
			for(AtomicServiceInstance asi : workflow.getAtomicServiceInstances()) {
				AtomicService as = getAtomicService(asi.getAtomicServiceId(), atomicServices);
				
				if(!result.keySet().contains(as)) {
					result.put(as, new ArrayList<AtomicServiceInstance>());
				}
				
				result.get(as).add(asi);
			}
		} else {
			log.warn("Received empty workflow status for workflow id [{}]", workflowId);
		}
		
		return result;
	}

	private AtomicService getAtomicService(String atomicServiceId,
			List<AtomicService> atomicServices) {
		for(AtomicService as : atomicServices) {
			if(as.getAtomicServiceId() != null && as.getAtomicServiceId().equals(atomicServiceId)) {
				return as;
			}
		}
		
		return null;
	}
	
	private List<AtomicService> getSortedAtomicServices(PortletRequest request) {
		List<AtomicService> atomicServices = clientFactory.getCloudFacade(request).getAtomicServices();
		Collections.sort(atomicServices, new Comparator<AtomicService>() {
			@Override
			public int compare(AtomicService as1, AtomicService as2) {
				return as1.getName().compareToIgnoreCase(as2.getName());
			}
		});
		
		return atomicServices;
	}
	
	private void fixPaths(Endpoint endpoint) {
		if(endpoint.getInvocationPath() != null){
			endpoint.setInvocationPath(endpoint.getInvocationPath().trim());
			
			if(!endpoint.getInvocationPath().startsWith("/")) {
				endpoint.setInvocationPath("/" + endpoint.getInvocationPath());
			}
			
			if(endpoint.getInvocationPath().endsWith("/")) {
				endpoint.setInvocationPath(endpoint.getInvocationPath().substring(0, endpoint.getInvocationPath().length() - 1));
			}
		}
		
		if(endpoint.getInvocationPath() != null) {
			endpoint.setInvocationPath(endpoint.getInvocationPath().trim());
			
			if(!endpoint.getInvocationPath().startsWith("/")) {
				endpoint.setInvocationPath("/" + endpoint.getInvocationPath());
			}
			
			if(endpoint.getInvocationPath().endsWith("/")) {
				endpoint.setInvocationPath(endpoint.getInvocationPath().substring(0, endpoint.getInvocationPath().length() - 1));
			}
		}
	}
	
	private Map<String, String> createEndpointLinks(List<Endpoint> endpoints,
			List<Redirection> redirections, boolean addAuthentication, PortletRequest request, boolean wbr) {
		Map<String, String> endpointLinks = new HashMap<>();
		
		for(Endpoint endpoint : endpoints) {
			Redirection redirection = null;
			
			for(Redirection r : redirections) {
				if(r.getToPort() != null && r.getToPort().equals(endpoint.getPort())) {
					redirection = r;
					
					break;
				}
			}
			String auth = null;
			
			if(addAuthentication) {
				auth = portal.getUserName(request) + ":" + portal.getUserToken(request) + "@";
			}
			
			if(redirection != null) {
				String link = "http://" + (addAuthentication ? auth : "") + redirection.getHost().trim() + ":" + redirection.getFromPort() + 
						"/" + redirection.getPostfix().trim() +
						(!redirection.getPostfix().trim().endsWith("/") && !endpoint.getInvocationPath().trim().startsWith("/") ? "/" : "") +
						endpoint.getInvocationPath().trim();
				
				if(wbr) {
					link = link.replaceAll("/", "/<wbr/>");
				}
				
				endpointLinks.put(endpoint.getId(), link);
			}
		}
		
		return endpointLinks;
	}
	
	private Redirection findRedirection(Integer port, List<Redirection> redirections) {
		for(Redirection redirection : redirections) {
			if(redirection.getToPort().equals(port)) {
				return redirection;
			}
		}
		
		return null;
	}
	
	private InitialConfiguration createDefaultInitialConfiguration(String atomicServiceId) {
		InitialConfiguration ic = new InitialConfiguration();
		ic.setName(atomicServiceId + " initial configuration");
		ic.setPayload("<initialConfiguration/>");
		
		return ic;
	}
}