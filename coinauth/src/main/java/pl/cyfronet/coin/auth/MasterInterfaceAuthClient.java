package pl.cyfronet.coin.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface MasterInterfaceAuthClient {

	@GET
	@Path("/validatetkt/")
	@Produces(MediaType.APPLICATION_JSON)
	public UserDetails validate(@QueryParam("ticket") String ticket);
}
