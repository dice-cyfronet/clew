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

import pl.cyfronet.coin.api.beans.PublicKeyInfo;
import pl.cyfronet.coin.api.exception.KeyAlreadyExistsException;
import pl.cyfronet.coin.api.exception.KeyNotFoundException;
import pl.cyfronet.coin.api.exception.WrongKeyFormatException;

/**
 * Service for managing user keys. During development mode user can choose which
 * public key should be injected into VM root account.
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@Path("/")
public interface KeyManagement {

	/**
	 * List basic information about all public keys belonging to the user which
	 * invokes this service.
	 * @return Basic information about keys registered for the user.
	 */
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	List<PublicKeyInfo> list();

	/**
	 * Add new public key.
	 * @param keyName Unique key name.
	 * @param publicKey Public key content.
	 * @return Identifier of new added key entry.
	 * @throws KeyAlreadyExistsException Thrown, when key with given name
	 *             already exists.
	 * @throws WrongKeyFormatException Thrown, when key has wrong format.
	 *             Currently only ssh-rsa and ssh-dss are supported.
	 */
	@POST
	@Path("/add")
	String add(@FormParam("keyName") String keyName,
			@FormParam("publicKey") String publicKey)
			throws KeyAlreadyExistsException, WrongKeyFormatException;

	/**
	 * Remove public key belonging to the user which invokes this service.
	 * @return Public key content.
	 * @throws KeyNotFoundException Thrown, when key with given identifier is
	 *             not found for the user which invokes the service.
	 */
	@DELETE
	@Path("/{keyId}")
	void delete(@PathParam("keyId") String keyId) throws KeyNotFoundException;

	/**
	 * Get public key belonging to the user which invokes this service.
	 * @param keyId Key identifier.
	 * @return Public key content.
	 * @throws KeyNotFoundException Thrown, when key with given identifier is
	 *             not found for the user which invokes the service.
	 */
	@GET
	@Path("/{keyId}")
	String get(@PathParam("keyId") String keyId) throws KeyNotFoundException;
}
