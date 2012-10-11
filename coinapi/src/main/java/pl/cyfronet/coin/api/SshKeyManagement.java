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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.SSHKeyPair;
import pl.cyfronet.coin.api.exception.SSHKeyAlreadyExistsException;
import pl.cyfronet.coin.api.exception.SSHKeyNotFoundException;

/**
 * Service for managing user keys. During development mode user can choose which
 * public key should be injected into VM root account.
 * 
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface SshKeyManagement {

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	List<String> list();

	@POST
	@Path("/add")
	void add(@FormParam("keyName") String keyName,
			@FormParam("publicKey") String publicKey,
			@FormParam("privateKey") String privateKey)
			throws SSHKeyAlreadyExistsException;

	@DELETE
	@Path("/{keyName}")
	void remove(@PathParam("keyName") String keyName) throws SSHKeyNotFoundException;

	@GET
	@Path("/{keyName}")
	@Produces({ MediaType.APPLICATION_JSON })
	SSHKeyPair get(@PathParam("keyName") String keyName) throws SSHKeyNotFoundException;
}
