package pl.cyfronet.coin.clew.client.controller.cf.portmappingtemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeKey;

public interface PortMappingTemplateService extends RestService {
	@GET
	@Path("port_mapping_templates?appliance_type_id={id}&private_token=" + CloudFacadeKey.KEY)
	void getPortMappingTemplates(@PathParam("id") String applianceTypeId, MethodCallback<PortMappingTemplatesResponse> methodCallback);
}