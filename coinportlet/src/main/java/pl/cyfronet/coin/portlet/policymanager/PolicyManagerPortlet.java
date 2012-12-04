package pl.cyfronet.coin.portlet.policymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.cyfronet.coin.portlet.cloudmanager.UploadKeyRequest;
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
	
	@RequestMapping
	public String doViewListPolicies(Model model, PortletRequest request) {
		Map<String, String> policies = new HashMap<>();
		List<String> policyNames = clientFactory.getSecurityPolicyService(request).getPoliciesNames();
		
		for(String policyName : policyNames) {
			policies.put(policyName,
					clientFactory.getSecurityPolicyService(request).getSecurityPolicy(policyName));
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
		
		if(!errors.hasErrors()) {
			clientFactory.getSecurityPolicyService(request).
					updateSecurityPolicy(uploadPolicyRequest.getPolicyName(),
							uploadPolicyRequest.getPolicyBody(), false);
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_DELETE_POLICY)
	public void doActionDeletePolicy(@RequestParam("policyName") String policyName,
			PortletRequest request) {
		log.debug("Removing policy [{}]", policyName);
		clientFactory.getSecurityPolicyService(request).deleteSecurityPolicy(policyName);
	}
}