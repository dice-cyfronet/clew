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

import java.io.InputStream;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
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
	@Path("as/{atomicServiceInstanceId}")
	@WebMethod(operationName = "createAtomicService")
	void createAtomicService(
			@WebParam(name = "atomicServiceInstanceId") @PathParam("atomicServiceInstanceId") String atomicServiceInstanceId,
			@WebParam(name = "atomicService") AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;
	
	/**
	 * Get initial configurations for given atomic service (a.k.a. appliance
	 * type).
	 * @param atomicServiceId Atomic service id.
	 * @return List of atomic service configurations in JSON format:
	 *         <code>{[ {"name":"configName", id: "configId"}, ... ]}</code>
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/as/{atomicServiceId}/configurations")
	List<InitialConfiguration> getInitialConfigurations(
			@PathParam("atomicServiceId") String atomicServiceId);
	
	@GET
	@Path("/")
	String getDocumentation();
}