package pl.cyfronet.coin.portlet.policymanager;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.portlet.util.ClientFactory;

@Controller
@RequestMapping("VIEW")
public class PolicyManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(PolicyManagerPortlet.class);
	
	private static final String MODEL_BEAN_POLICIES = "policies";
	private static final String MODEL_BEAN_UPLOAD_POLICY = "uploadPolicyRequest";
	
	private static final String PARAM_ACTION = "action";
	
	private static final String ACTION_UPLOAD_POLICY = "uploadPolicy";
	private static final String ACTION_DELETE_POLICY = "deletePolicy";
	
	@Autowired private ClientFactory clientFactory;
	@Autowired private Validator validator;
	
	@RequestMapping
	public String doViewListPolicies(Model model, PortletRequest request) {
		Map<String, String> policies = new HashMap<>();
		List<String> policyNames = clientFactory.getSecurityPolicyService(request).list();
		
		for(String policyName : policyNames) {
			policies.put(policyName,
					clientFactory.getSecurityPolicyService(request).getPayload(policyName));
		}
		
		model.addAttribute(MODEL_BEAN_POLICIES, policies);
		
		if(!model.containsAttribute(MODEL_BEAN_UPLOAD_POLICY)) {
			model.addAttribute(MODEL_BEAN_UPLOAD_POLICY, new UploadPolicyRequest());
		}
		
		return "policyManager/listPolicies";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_POLICY)
	public void doActionUploadPolicy(@ModelAttribute(MODEL_BEAN_UPLOAD_POLICY) UploadPolicyRequest uploadPolicyRequest,
			BindingResult errors, Model model, PortletRequest request, ActionResponse response) {
		log.debug("Processing upload policy request for key [{}]", uploadPolicyRequest);
		validator.validate(uploadPolicyRequest, errors);
		
		if(clientFactory.getSecurityPolicyService(request).list().
				contains(uploadPolicyRequest.getPolicyName())) {
			errors.addError(new FieldError(MODEL_BEAN_UPLOAD_POLICY, "policyName", "Given policy name is already taken"));
		}
		
		if(uploadPolicyRequest.getPolicyBody() == null || uploadPolicyRequest.getPolicyBody().isEmpty()) {
			errors.addError(new FieldError(MODEL_BEAN_UPLOAD_POLICY, "policyBody", "Policy file location cannot be empty"));
		}
		
		if(!errors.hasErrors()) {
			try {
				NamedOwnedPayload newPolicy = new NamedOwnedPayload();
				newPolicy.setName(uploadPolicyRequest.getPolicyName());
				newPolicy.setPayload(new String(uploadPolicyRequest
						.getPolicyBody().getBytes()));
				clientFactory.getSecurityPolicyService(request).create(
						newPolicy);
			} catch (Exception e) {
				log.info("Could not upload security policy");
				errors.addError(new FieldError(MODEL_BEAN_UPLOAD_POLICY, "policyName", "The policy could not be uploaded"));
			}
		}
		
		if(!errors.hasErrors()) {
			log.info("Security policy [{}] succcessfully uploaded", uploadPolicyRequest.getPolicyName());
			model.addAttribute(MODEL_BEAN_UPLOAD_POLICY, new UploadPolicyRequest());
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_DELETE_POLICY)
	public void doActionDeletePolicy(@RequestParam("policyName") String policyName,
			PortletRequest request) {
		log.debug("Removing policy [{}]", policyName);
		clientFactory.getSecurityPolicyService(request).delete(policyName);
	}
	
	@ResourceMapping("downloadPolicy")
	public void doResourcePolicyDownload(@RequestParam("policyName") String policyName,
			PortletRequest request, ResourceResponse response) {
		if(policyName != null) {
			String policy = clientFactory.getSecurityPolicyService(request).getPayload(policyName);
			response.addProperty("Content-Disposition", "Attachment;Filename=\"policy.txt\"");
			response.addProperty("Pragma", "public");
			response.addProperty("Cache-Control", "must-revalidate");
			response.setContentLength(policy.length());
			
			try {
				FileCopyUtils.copy(new StringReader(policy), response.getWriter());
			} catch (Exception e) {
				log.warn("Could not serve security policy for name  [{}]", policyName);
			}
		} else {
			log.warn("Policy with name [{}] does not exist", policyName);
		}
	}
}