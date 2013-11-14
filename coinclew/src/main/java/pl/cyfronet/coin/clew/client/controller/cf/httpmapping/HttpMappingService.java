package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeKey;

public interface HttpMappingService extends RestService {
	@GET
	@Path("http_mappings?appliance_id={id}&private_token=" + CloudFacadeKey.KEY)
	void getHttpMappings(@PathParam("id") String applianceId, MethodCallback<HttpMappingResponse> methodCallback);
}