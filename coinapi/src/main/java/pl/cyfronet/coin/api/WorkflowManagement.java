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

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.beans.Endpoint;
import pl.cyfronet.coin.api.beans.UserWorkflows;
import pl.cyfronet.coin.api.beans.Workflow;
import pl.cyfronet.coin.api.beans.WorkflowStartRequest;
import pl.cyfronet.coin.api.beans.redirection.Redirections;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.RedirectionNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotFoundException;
import pl.cyfronet.coin.api.exception.WorkflowNotInDevelopmentModeException;
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
	@Path("/")
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
	 * @param workflow Workflow specification
	 * @return Workflow id.
	 * @throws WorkflowStartException Thrown when workflow cannot be started.
	 *             E.g. user tries to start second development or portal
	 *             workflow.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/")
	String startWorkflow(WorkflowStartRequest workflow)
			throws WorkflowStartException, CloudFacadeException;

	/**
	 * Get full information about workflow.
	 * @param contextId Workflow id.
	 * @return Workflow structure (workflow name, type, atomic service instances
	 *         running in the scope of this workflow).
	 */
	@GET
	@Path("/{contextId}")
	@Produces({ MediaType.APPLICATION_JSON })
	Workflow getWorkflow(@PathParam("contextId") String contextId)
			throws WorkflowNotFoundException;
	
	/**
	 * Stop workflow. It will stop all atomic services executed for the
	 * workflow.
	 * @param contextId Workflow id.
	 */
	@DELETE
	@Path("/{contextId}")
	void stopWorkflow(@PathParam("contextId") String contextId)
			throws WorkflowNotFoundException, CloudFacadeException;

	/**
	 * Add atomic service for started workflow. For new create AS selected key
	 * will be injected, but only when workflow is in development mode.
	 * @param contextId Workflow id.	 
	 * @param request Request with all information about required AS.
	 */
	@POST
	@Path("/{contextId}/atomic_services")
	@Consumes({ MediaType.APPLICATION_JSON })
	void addAtomicServiceToWorkflow(@PathParam("contextId") String contextId,
			AddAsWithKeyToWorkflow request) throws WorkflowNotFoundException,
			CloudFacadeException;	

	/**
	 * Remove atomic service from running workflow.
	 * @param contextId Workflow id.
	 * @param asId Atomic service configuration id.
	 */
	@DELETE
	@Path("/{contextId}/atomic_services/{asiId}")
	void removeAtomicServiceFromWorkflow(
			@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId)
			throws WorkflowNotFoundException, CloudFacadeException;		

	@GET
	@Path("/{contextId}/atomic_services/{asiId}")
	AtomicServiceInstance getWorkflowAtomicServiceInstance(
			@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException;
	
	@POST
	@Path("/{contextId}/atomic_services/{asiId}/restart")
	void restartAtomicServiceInstance(
			@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException, WorkflowNotInDevelopmentModeException;
	
	// redirections
	
	@GET
	@Path("/{contextId}/atomic_services/{asiId}/redirections")
	@Produces({ MediaType.APPLICATION_JSON })
	Redirections getRedirections(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException;

	@POST
	@Path("/{contextId}/atomic_services/{asiId}/redirections")
	String addRedirection(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId, @FormParam("name") String name,
			@FormParam("port") int port, @FormParam("type") RedirectionType type)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException;

	@DELETE
	@Path("/{contextId}/atomic_services/{asiId}/redirections/{redirectionId}")
	void deleteRedirection(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId,
			@PathParam("redirectionId") String redirectionId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, RedirectionNotFoundException;
	
	// endpoints

	@GET
	@Path("/{contextId}/atomic_services/{asiId}/endpoints")
	@Produces({ MediaType.APPLICATION_JSON })
	List<Endpoint> getEndpoints(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId) throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException;

	@POST
	@Path("/{contextId}/atomic_services/{asiId}/endpoints")
	@Consumes({ MediaType.APPLICATION_JSON })
	String addEndpoint(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId, Endpoint endpoint)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException;

	@DELETE
	@Path("/{contextId}/atomic_services/{asiId}/endpoints/{endpointId}")
	void deleteEndpoint(@PathParam("contextId") String contextId,
			@PathParam("asiId") String asiId,
			@PathParam("endpointId") String endpointId)
			throws WorkflowNotFoundException,
			AtomicServiceInstanceNotFoundException,
			WorkflowNotInDevelopmentModeException, EndpointNotFoundException;
}
