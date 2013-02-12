/*
 * Copyright 2013 ACC CYFRONET AGH
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantAlreadyExistException;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public interface GrantService {

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<String> listGrants();

	@GET
	@Path("/{grantName : .+}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Grant getGrant(@PathParam("grantName") String grantName);

	@POST
	@Path("/{grantName : .+}")
	@Consumes({ MediaType.APPLICATION_JSON })
	void updateGrant(@PathParam("grantName") String grantName, Grant grant,
			@QueryParam("overwrite") boolean overwrite)
			throws GrantAlreadyExistException;

	@DELETE
	@Path("/{grantName : .+}")
	void deleteGrant(@PathParam("grantName") String grantName)
			throws GrantNotFoundException;
}