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

package pl.cyfronet.coin.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AtomicServiceStatus;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowStatus;

/**
 * REST service dedicated for managing workflow lifecycle.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface WorkflowManagement {

	/**
	 * Start new workflow. This action will trigger generation of the unique
	 * workflow id. For workflow user can add atomic services (list of required
	 * atomic services can be available while starting workflow or latter on
	 * during workflow run).
	 * <p>
	 * Service will be published as a REST service which consumes JSON with
	 * following format: <code>
	 * 	{"name": "workflowName:, "requiredIds": ["atomicService1Id", ..., "atomicServiceNId"]}  
	 * </code>
	 * <p>
	 * @param workflow Workflow specification
	 * @return Workflow id.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/start")
	String startWorkflow(Workflow workflow);

	/**
	 * Stop workflow. It will stop all atomic services executed for the
	 * workflow.
	 * @param workflowId Workflow id.
	 */
	@DELETE
	@Path("/{workflowId}")
	void stopWorkflow(@PathParam("workflowId") String workflowId);

	/**
	 * Add atomic service for started workflow.
	 * @param workflowId Workflow id.
	 * @param asId Atomic service configuration id.
	 */
	@PUT
	@Path("/{workflowId}/as/{asConfigId}")
	void addAtomicServiceToWorkflow(@PathParam("workflowId") String workflowId,
			@PathParam("asConfigId") String asConfigId);

	/**
	 * Remove atomic service from running workflow.
	 * @param workflowId Workflow id.
	 * @param asId Atomic service configuration id.
	 */
	@DELETE
	@Path("/{workflowId}/as/{asConfigId}")
	void removeAtomicServiceFromWorkflow(
			@PathParam("workflowId") String workflowId,
			@PathParam("asConfigId") String asConfigId);

	/**
	 * Get status of all atomic services executed in the scope of workflow.
	 * @param workflowId Workflow id.
	 * @return Status of all atomic services executed for defined workflow in
	 *         JSON format: <code>
	 * {"name": "workflowName" ases: [
	 * 	{"id": "asId", "status": "OK", "message": "message", instances: [
	 *   	{id: "atomicServiceInstanceId", "status": "OK", message: "message"], ...
	 * 	]}, ...
	 * ]} 
	 * </code>
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{workflowId}")
	WorkflowStatus getStatus(@PathParam("workflowId") String workflowId);

	/**
	 * Get atomic service executed for defined workflow status.
	 * @param workflowId Workflow id.
	 * @param asId Atomic service id.
	 * @return Atomic service status in JSON format:
	 *         <code>{"id": "asId", "status": "OK", "message": "message", instances: [
	 *   	{id: "atomicServiceInstanceId", "status": "OK", message: "message"}, ...
	 * 	]}</code>
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{workflowId}/as/{asConfigId}")
	AtomicServiceStatus getStatus(@PathParam("workflowId") String workflowId,
			@PathParam("asConfigId") String asId);

	/**
	 * Get initial configurations for given atomic service (a.k.a. appliance
	 * type).
	 * @param atomicServiceId Atomic service id.
	 * @return List of atomic service configurations in JSON format:
	 *         <code>{[ {"name":"configName", id: "configId"}, ... ]}</code>
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{workflowId}/as/{atomicServiceId}/configurations")
	List<InitialConfiguration> getInitialConfigurations(String atomicServiceId);
}
