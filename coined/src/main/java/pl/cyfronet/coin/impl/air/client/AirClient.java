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

package pl.cyfronet.coin.impl.air.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import pl.cyfronet.coin.api.beans.WorkflowType;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 * @see MediaType
 */
public interface AirClient {

	@POST
	@Path("/workflow/start")
	@Consumes("multipart/form-data")
	String startWorkflow(@FormParam("name") String name,
			@FormParam("vph_username") String vph_username,
			@FormParam("description") String description,
			@FormParam("priority") Integer priority,
			@FormParam("type") WorkflowType type);
	
	@POST
	@Path("/workflow/stop")
	@Consumes("multipart/form-data")
	void stopWorkflow(@FormParam("context_id") String contextId);
}
