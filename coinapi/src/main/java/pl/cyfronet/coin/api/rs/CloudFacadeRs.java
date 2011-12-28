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

package pl.cyfronet.coin.api.rs;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.api.beans.AtomicServiceInstance;
import pl.cyfronet.coin.api.ws.exception.AtomicServiceInstanceNotFoundException;
import pl.cyfronet.coin.api.ws.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.ws.exception.CloudFacadeException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface CloudFacadeRs {

	@GET
	@Path("/asi/{contextId}/list")
	@Produces({ MediaType.APPLICATION_JSON })
	List<AtomicServiceInstance> getAtomicServiceInstances(
			@PathParam("contextId") String contextId)
			throws CloudFacadeException;

	@PUT
	@Path("/asi/new/{atomicServiceId}/in/{contextId}")
	@Produces(MediaType.TEXT_PLAIN)
	String startAtomicServiceInstance(
			@PathParam("atomicServiceId") String atomicServiceId,
			@PathParam("contextId") String contextId)
			throws AtomicServiceNotFoundException, CloudFacadeException;

	@DELETE
	@Path("/asi/{atomicServiceInstance}")
	void stopAtomicServiceInstance(
			@PathParam("atomicServiceInstance") String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	@GET
	@Path("/asi/{atomicServiceInstanceId}")
	AtomicServiceInstance getAtomicServiceInstanceStatus(
			@PathParam("atomicServiceInstanceId") String atomicServiceInstanceId)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;

	@GET
	@Path("as/list")
	@Produces({ MediaType.APPLICATION_JSON })
	List<AtomicService> getAtomicServices() throws CloudFacadeException;

	@PUT
	@Path("as/new/{atomicServiceInstanceId}")
	void createAtomicService(
			@PathParam("atomicServiceInstanceId") String atomicServiceInstanceId,
			AtomicService atomicService)
			throws AtomicServiceInstanceNotFoundException, CloudFacadeException;
}
