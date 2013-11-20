package pl.cyfronet.coin.clew.client.controller.cf.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface EndpointService extends RestService {
	@GET
	@Path("endpoints?port_mapping_template_id={id}appliance_id={id}")
	void getEndpoints(@PathParam("id") String portMappingTemplateId, MethodCallback<EndpointsResponse> methodCallback);
}