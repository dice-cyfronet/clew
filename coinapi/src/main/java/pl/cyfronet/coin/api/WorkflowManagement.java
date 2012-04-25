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

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.Redirection;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowStartException;

/**
 * REST service dedicated for managing workflow lifecycle.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface WorkflowManagement {

	/**
	 * Get user workflows. Identification of the user is taken from the security
	 * infrastructure.
	 * @return List of user workflows.
	 */
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	@WebMethod(operationName = "getUserWorkflows")
	@WebResult(name = "workflows")
	UserWorkflows getWorkflows();

	/**
	 * Start new workflow. This action will trigger generation of the unique
	 * workflow id. For workflow user can add atomic services (list of required
	 * atomic services can be available while starting workflow or latter on
	 * during workflow run). There can be many workflow type Workflows but only
	 * one portal and development workflow.
	 * <p>
	 * Service will be published as a REST service which consumes JSON with
	 * following format: <code>
	 * 	{"name": "workflowName:, "requiredIds": ["atomicService1Id", ..., "atomicServiceNId"]}  
	 * </code>
	 * <p>
	 * @param workflow Workflow specification
	 * @return Workflow id.
	 * @throws WorkflowStartException Thrown when workflow cannot be started.
	 *             E.g. user tries to start second development or portal
	 *             workflow.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/start")
	String startWorkflow(WorkflowStartRequest workflow)
			throws WorkflowStartException, CloudFacadeException;

	/**
	 * Stop workflow. It will stop all atomic services executed for the
	 * workflow.
	 * @param workflowId Workflow id.
	 */
	@DELETE
	@Path("/{workflowId}")
	void stopWorkflow(@PathParam("workflowId") String workflowId)
			throws WorkflowNotFoundException, CloudFacadeException;

	/**
	 * Add atomic service for started workflow.
	 * @param workflowId Workflow id.
	 * @param asId Atomic service configuration id.
	 */
	@PUT
	@Path("/{workflowId}/as/{asConfigId}/{name}")
	void addAtomicServiceToWorkflow(@PathParam("workflowId") String workflowId,
			@PathParam("asConfigId") String asConfigId,
			@PathParam("name") String name) throws WorkflowNotFoundException,
			CloudFacadeException;

	/**
	 * Remove atomic service from running workflow.
	 * @param workflowId Workflow id.
	 * @param asId Atomic service configuration id.
	 */
	@DELETE
	@Path("/{workflowId}/as/{asConfigId}")
	void removeAtomicServiceFromWorkflow(
			@PathParam("workflowId") String workflowId,
			@PathParam("asConfigId") String asConfigId)
			throws WorkflowNotFoundException, CloudFacadeException;

	/**
	 * Get full information about workflow.
	 * @param workflowId Workflow id.
	 * @return Workflow structure (workflow name, type, atomic service instances
	 *         running in the scope of this workflow).
	 */
	@GET
	@Path("/{workflowId}")
	@Produces({ MediaType.APPLICATION_JSON })
	Workflow getWorkflow(@PathParam("workflowId") String workflowId)
			throws WorkflowNotFoundException;

	/**
	 * Adds redirection to the Atomic Service Instance. This method will throws
	 * Forbidden (403 HTTP code) if user will try to add redirection into atomic
	 * service instance executed in other context than development.
	 * @param contextId Atomic Service Instance execution context id (a.k.a.
	 *            workflow)
	 * @param asiId Atomic Service Instance id.
	 * @param redirectionInfo Bean containing information about new required
	 *            redirection.
	 * @return Redirection URI (e.g. http://url.to.redirected.http.pl for HTTP,
	 *         149.156.10.131:235334 for others).
	 */
	@POST
	@Path("/asi/{contextId}/{asiId}/redirection/add")
	@Consumes({ MediaType.APPLICATION_JSON })
	String addRedirection(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId, Redirection redirectionInfo);
}
