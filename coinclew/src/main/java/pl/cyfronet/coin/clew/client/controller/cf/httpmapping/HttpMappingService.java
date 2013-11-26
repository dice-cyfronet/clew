package pl.cyfronet.coin.clew.client.controller.cf.httpmapping;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface HttpMappingService extends RestService {
	@GET
	@Path("http_mappings?appliance_id={id}")
	void getHttpMappings(@PathParam("id") String applianceId, MethodCallback<HttpMappingResponse> methodCallback);

	@GET
	@Path("http_mappings?port_mapping_template_id={id}")
	void getHttpMappingsForPortMappingTemplateId(@PathParam("id") String portMappingTemplateId, MethodCallback<HttpMappingResponse> methodCallback);
}