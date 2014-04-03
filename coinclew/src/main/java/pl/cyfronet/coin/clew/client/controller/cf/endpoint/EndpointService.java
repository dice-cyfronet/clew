package pl.cyfronet.coin.clew.client.controller.cf.endpoint;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface EndpointService extends RestService {
	@GET
	@Path("endpoints?port_mapping_template_id={id}")
	void getEndpoints(@PathParam("id") String portMappingTemplateId, MethodCallback<EndpointsResponse> methodCallback);

	@DELETE
	@Path("endpoints/{id}")
	void deleteEndpoint(@PathParam("id") String endpointId, MethodCallback<Void> methodCallback);
	
	@POST
	@Path("endpoints")
	void addEndpoint(NewEndpointRequest newEndpointRequest, MethodCallback<EndpointRequestResponse> methodCallback);

	@PUT
	@Path("endpoints/{id}")
	void updateEndpoint(@PathParam("id") String endpointId, NewEndpointRequest request,
			MethodCallback<EndpointRequestResponse> methodCallback);
}