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
	void deleteSecurityPolicy(String policyName)
			throws SecurityPolicyNotFoundException;
}
