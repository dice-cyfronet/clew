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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.InitialConfiguration;
import pl.cyfronet.coin.api.beans.NewAtomicService;
import pl.cyfronet.coin.api.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.CloudFacadeException;
import pl.cyfronet.coin.api.exception.EndpointNotFoundException;
import pl.cyfronet.coin.api.exception.InitialConfigurationAlreadyExistException;

/**
 * Web service definition of the cloud facade which exposes methods allowing to
 * manage cloud infrastructure.
 * @author <a href="d.harezlak@cyfronet.pl>Daniel Harezlak</a>
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface CloudFacade {

	/**
	 * Get list of atomic services (vm templates) available for the user.
	 * @return List of available atomic services.
	 * @throws CloudFacadeException Thrown when error while receiving atomic
	 *             services list occurs.
	 */
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
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
	@POST
	@Path("/")
	@Consumes({ MediaType.APPLICATION_JSON })
	String createAtomicService(NewAtomicService newAtomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	/**
	 * Create new initial configuration for atomic service. The initial
	 * configuration will be injected into new created VM while booting up.
	 * @param atomicServiceId For this atomic service new initial configuration
	 *            will be added.
	 * @param initialConfiguration Initial configuration name and payload
	 * @return Initial configuration id.
	 * @throws AtomicServiceNotFoundException
	 * @throws CloudFacadeException
	 * @throws InitialConfigurationAlreadyExistException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/{atomicServiceId}/configurations")
	String addInitialConfiguration(
			@PathParam("atomicServiceId") String atomicServiceId,
			InitialConfiguration initialConfiguration)
			throws AtomicServiceNotFoundException,
			InitialConfigurationAlreadyExistException, CloudFacadeException;

	/**
	 * Get initial configurations for given atomic service (a.k.a. appliance
	 * type).
	 * @param atomicServiceId Atomic service id.
	 * @return List of atomic service configurations in JSON format:
	 *         <code>{[ {"name":"configName", id: "configId"}, ... ]}</code>
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{atomicServiceId}/configurations")
	List<InitialConfiguration> getInitialConfigurations(
			@PathParam("atomicServiceId") String atomicServiceId,
			@QueryParam("load_payload") boolean loadPayload);

	@GET
	@Path("/services_set")
	String getServicesSet();

	@GET
	@Path("/{atomicServiceId}/endpoint/{servicePort}/{invocationPath : .+}/get_as_id")
	String getAtomicServiceId(
			@PathParam("atomicServiceId") String atomicServiceId,
			@PathParam("servicePort") int servicePort,
			@PathParam("invocationPath") String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException;

	@GET
	@Path("/{atomicServiceId}/endpoint/{servicePort}/{invocationPath : .+}")
	String getEndpointDescriptor(
			@PathParam("atomicServiceId") String atomicServiceId,
			@PathParam("servicePort") int servicePort,
			@PathParam("invocationPath") String invocationPath)
			throws AtomicServiceInstanceNotFoundException,
			EndpointNotFoundException;
}