/*
 * Copyright 2011 ACC CYFRONET AGH
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
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;

/**
 * Web service definition of the cloud facade which exposes methods allowing to
 * manage cloud infrastructure.
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@WebService(targetNamespace = "http://cyfronet.pl/coin")
@Path("/")
public interface CloudFacade {

	/**
	 * Get list of atomic service instances created in the context scope.
	 * @param contextId Context id (e.g. workflow id or master interface atomic
	 *            service wizard).
	 * @return List of atomic service instances created in the context scope
	 * @throws CloudFacadeException Thrown, when error while received
	 *             information about atomic service instances occurs.
	 */
	@GET
	@Path("/asi/{contextId}/list")
	@Produces({ MediaType.APPLICATION_JSON })
	@WebMethod(operationName = "getAtomicServiceInstances")
	@WebResult(name = "atomicServiceInstances")
	List<AtomicServiceInstance> getAtomicServiceInstances(
			@WebParam(name = "contextId") @PathParam("contextId") String contextId)
			throws CloudFacadeException;

	/**
	 * Get list of atomic services (vm templates) available for the user.
	 * @return List of available atomic services.
	 * @throws CloudFacadeException Thrown when error while receiving atomic
	 *             services list occurs.
	 */
	@GET
	@Path("as/list")
	@Produces({ MediaType.APPLICATION_JSON })
	@WebMethod(operationName = "getAtomicServices")
	@WebResult(name = "atomicServices")
	List<AtomicService> getAtomicServices() throws CloudFacadeException;

	/**
	 * Start atomic service instance.
	 * @param atomicServiceId Atomic service (vm template) id.
	 * @param contextId Context in which atomic service instance should be
	 *            created.
	 * @return New created atomic service instance id.
	 * @throws AtomicServiceNotFoundException Thrown when atomic service (vm
	 *             template) which should be started is not found.
	 * @throws CloudFacadeException Thrown when error occurs while starting
	 *             atomic service instance.
	 */
	@PUT
	@Path("/asi/new/{atomicServiceId}/in/{contextId}")
	@Produces(MediaType.TEXT_PLAIN)
	@WebMethod(operationName = "startAtomicServiceInstance")
	@WebResult(name = "atomicServiceInstanceId")
	String startAtomicServiceInstance(
			@WebParam(name = "atomicServiceId") @PathParam("atomicServiceId") String atomicServiceId,
			@WebParam(name = "contextId") @PathParam("contextId") String contextId)
			throws AtomicServiceNotFoundException, CloudFacadeException;

	/**
	 * Get atomic service instance status.
	 * @param atomicServiceInstanceId Atomic service instance id.
	 * @return Atomic service instance status.
	 * @throws AtomicServiceInstanceNotFoundException Thrown when atomic service
	 *             instance with given id is not found.
	 * @throws CloudFacadeException Thrown when error while getting instance
	 *             information occurs.
	 */
	@GET
	@Path("/asi/{atomicServiceInstanceId}")
	@WebMethod(operationName = "getAtomicServiceInstance")
	@WebResult(name = "atomicServiceInstance")
	AtomicServiceInstance getAtomicServiceInstance(
			@WebParam(name = "atomicServiceInstanceId") @PathParam("atomicServiceInstanceId") String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	/**
	 * Stop atomic service instance.
	 * @param atomicServiceInstanceId Atomic service instance id.
	 * @throws AtomicServiceInstanceNotFoundException Thrown when atomic service
	 *             is not found.
	 * @throws CloudFacadeException Thrown while error while stopping atomic
	 *             service instance occurs.
	 */
	@DELETE
	@Path("/asi/{atomicServiceInstance}")
	@WebMethod(operationName = "stopAtomicServiceInstanceId")
	void stopAtomicServiceInstance(
			@WebParam(name = "atomicServiceInstance") @PathParam("atomicServiceInstance") String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	/**
	 * Create atomic service (vm template) from atomic service instance (running
	 * vm).
	 * @param atomicServiceInstanceId Atomic service instance id.
	 * @param atomicService Atomic service name (vm name).
	 * @throws AtomicServiceInstanceNotFoundException Thrown when atomic service
	 *             instance which should be used to create atomic service is not
	 *             found.
	 * @throws CloudFacadeException Thrown when error while creating atomic
	 *             service (vm template) occurs.
	 */
	@PUT
	@Path("as/new/{atomicServiceInstanceId}")
	@WebMethod(operationName = "createAtomicService")
	void createAtomicService(
			@WebParam(name = "atomicServiceInstanceId") @PathParam("atomicServiceInstanceId") String atomicServiceInstanceId,
			@WebParam(name = "atomicService") AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;
}