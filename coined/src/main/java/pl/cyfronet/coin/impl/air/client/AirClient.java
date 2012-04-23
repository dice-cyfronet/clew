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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

	@POST
	@Path("/upload_appliance_config")
	String addInitialConfiguration(@FormParam("config_name") String configName,
			@FormParam("appliance_type") String applianceType,
			@FormParam("config_text") String configText);

	@GET
	@Path("/get_endpoint_descriptor/{endpointId}")
	String getEndpointDescriptor(@PathParam("endpointId") String endpointId);
}
