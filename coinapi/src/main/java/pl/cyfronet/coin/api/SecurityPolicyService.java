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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.exception.SecurityPolicyAlreadyExistException;
import pl.cyfronet.coin.api.exception.SecurityPolicyNotFoundException;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface SecurityPolicyService {

	@GET
	@Path("/{policyName}")
	String getSecurityPolicy(@PathParam("policyName") String policyName);

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	List<String> getPoliciesNames();

	@POST
	@Path("/{policyName}")
	void updateSecurityPolicy(@PathParam("policyName") String policyName,
			String policyContent, @QueryParam("overwrite") boolean overwrite)
			throws SecurityPolicyAlreadyExistException;

	@DELETE
	@Path("/{policyName}")
	void deleteSecurityPolicy(@PathParam("policyName") String policyName)
			throws SecurityPolicyNotFoundException;
	
	// : .+
}
