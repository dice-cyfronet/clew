package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplateproperty;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface PortMappingTemplatePropertyService extends RestService {
	@GET
	@Path("port_mapping_properties?port_mapping_template_id={id}")
	void getPortMappings(@PathParam("id") String portMappingTemplateId, MethodCallback<PortMappingTemplatePropertiesResponse> methodCallback);

	@POST
	@Path("port_mapping_properties")
	void addPortMappingTemplateProperty(NewPortMappingTemplatePropertyRequest portMappingTemplatePropertyRequest, MethodCallback<PortMappingTemplatePropertyRequestResponse> methodCallback);
}