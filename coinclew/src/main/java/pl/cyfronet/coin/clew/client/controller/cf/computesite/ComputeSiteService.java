package pl.cyfronet.coin.clew.client.controller.cf.computesite;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeKey;

public interface ComputeSiteService extends RestService {
	@GET
	@Path("compute_sites?private_token=" + CloudFacadeKey.KEY)
	void getComputeSites(MethodCallback<ComputeSitesResponse> methodCallback);
	
	@GET
	@Path("compute_sites/{id}?private_token=" + CloudFacadeKey.KEY)
	void getComputeSite(@PathParam("id") String computeSiteId, MethodCallback<ComputeSiteRequestResponse> methodCallback);
}