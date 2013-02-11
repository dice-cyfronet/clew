/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pl.cyfronet.coin.impl.air.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.WorkflowType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @see MediaType
 */
public interface AirClient {

	@POST
	@Path("/workflow/start")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	String startWorkflow(@FormParam("name") String name,
			@FormParam("vph_username") String vph_username,
			@FormParam("description") String description,
			@FormParam("priority") Integer priority,
			@FormParam("workflow_type") WorkflowType type);

	@POST
	@Path("/workflow/stop")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	void stopWorkflow(@FormParam("context_id") String contextId);

	@GET
	@Path("workflow/get_user_workflows/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	List<WorkflowDetail> getUserWorkflows(@PathParam("username") String username);

	@GET
	@Path("workflow/{contextId}")
	@Produces(MediaType.APPLICATION_JSON)
	WorkflowDetail getWorkflow(@PathParam("contextId") String contextId);

	@GET
	@Path("/get_appliance_types")
	@Produces(MediaType.APPLICATION_JSON)
	List<ApplianceType> getApplianceTypes();

	@GET
	@Path("/get_appliance_config/{conf_id}")
	String getApplianceConfig(@PathParam("conf_id") String configId);

	@GET
	@Path("/get_appliance_type_for_config/{conf_id}")
	ApplianceType getTypeFromConfig(@PathParam("conf_id") String configId);

	@POST
	@Path("/add_appliance_type_json")
	@Consumes(MediaType.APPLICATION_JSON)
	String addAtomicService(AddAtomicServiceRequest addAtomicService);

	@DELETE
	@Path("/appliance_type/{asName}")
	void deleteAtomicService(@PathParam("asName") String asName);

	@POST
	@Path("/upload_appliance_config")
	String addInitialConfiguration(@FormParam("config_name") String configName,
			@FormParam("appliance_type") String applianceType,
			@FormParam("config_text") String configText);

	@GET
	@Path("/get_endpoint_descriptor/{endpointId}")
	String getEndpointDescriptor(@PathParam("endpointId") String endpointId);

	// security policies

	@GET
	@Path("/security_policy")
	String getSecurityPolicy(@QueryParam("policy_name") String policyName);

	@POST
	@Path("/upload_security_policy")
	void uploadSecurityPolicy(@FormParam("policy_name") String policyName,
			@FormParam("policy_text") String policyText,
			@FormParam("overwrite") boolean overwrite);

	@DELETE
	@Path("/security_policy")
	void deleteSecurityPolicy(@QueryParam("policy_name") String policyName);

	@GET
	@Path("/list_security_policies")
	@Produces(MediaType.APPLICATION_JSON)
	List<SecurityPolicy> getSecurityPolicies();

	// keys

	@GET
	@Path("/user_key/get_user_keys/{vph_username}")
	@Produces(MediaType.APPLICATION_JSON)
	List<UserKeyInfo> getUserKeys(@PathParam("vph_username") String vphUsername);

	@GET
	@Path("/user_key/get_public_key/{vph_username}/{key_id}")
	String getPublicKey(@PathParam("vph_username") String vphUsername,
			@PathParam("key_id") String keyId);

	@DELETE
	@Path("/user_key/{vph_username}/{key_id}")
	void deletePublicKey(@PathParam("vph_username") String vphUsername,
			@PathParam("key_id") String keyId);

	@POST
	@Path("/user_key/add")
	String addKey(@FormParam("vph_username") String vphUsername,
			@FormParam("name") String keyName,
			@FormParam("public_key") String publicKey,
			@FormParam("fingerprint") String fingerprint);

	// grants
	
	@GET
	@Path("/grants")
	List<String> getGrantsNames();

	@GET
	@Path("/grants/{grantName : .+}")
	GrantBean getGrant(@PathParam("grantName") String grantName);

	@DELETE
	@Path("/grants/{grantName : .+}")
	void deleteGrant(@PathParam("grantName") String grantName);

	@POST
	@Path("/grants")
	String updateGrant(@FormParam("name") String name,
			@FormParam("payload_get") String payload_get,
			@FormParam("payload_post") String payload_post,
			@FormParam("payload_put") String payload_put,
			@FormParam("payload_delete") String payload_delete,
			@FormParam("overwrite") boolean overwrite);
}
