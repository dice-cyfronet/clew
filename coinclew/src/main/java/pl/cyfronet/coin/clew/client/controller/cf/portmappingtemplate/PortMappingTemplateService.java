package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface PortMappingTemplateService extends RestService {
	@GET
	@Path("port_mapping_templates?appliance_type_id={id}")
	void getPortMappingTemplates(@PathParam("id") String applianceTypeId, MethodCallback<PortMappingTemplatesResponse> methodCallback);

	@GET
	@Path("port_mapping_templates?id={ids}")
	void getPortMappingTemplatesForIds(@PathParam("ids") String portMappingTemplateIds, MethodCallback<PortMappingTemplatesResponse> methodCallback);

	@DELETE
	@Path("port_mapping_templates/{id}")
	void removePortMappingTemplate(@PathParam("id") String mappingId, MethodCallback<Void> methodCallback);
	
	@POST
	@Path("port_mapping_templates")
	void addPortMappingTemplate(NewPortMappingTemplateRequest portMappingTemplateRequest, MethodCallback<PortMappingTemplateRequestResponse> methodCallback);

	@GET
	@Path("port_mapping_templates?dev_mode_property_set_id={id}")
	void getPortMappingTemplatesForDevelopmentModePropertySetId(@PathParam("id") String developmentModePropertySetId, MethodCallback<PortMappingTemplatesResponse> methodCallback);
}