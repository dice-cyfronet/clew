package pl.cyfronet.coin.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/")
public interface SecurityPolicyService {

	@GET
	@Path("/{policyName}")
	String getSecurityPolicy(@PathParam("policyName") String policyName);
}
