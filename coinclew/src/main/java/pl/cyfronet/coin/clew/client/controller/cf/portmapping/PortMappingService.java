package pl.cyfronet.coin.clew.client.controller.cf.portmapping;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface PortMappingService extends RestService {
	@GET
	@Path("port_mappings?virtual_machine_id={id}")
	void getPortMappingsForVirtualMachineId(@PathParam("id") String virtualMachineId, MethodCallback<PortMappingResponse> methodCallback);

	@GET
	@Path("port_mappings?port_mapping_template_id={id}")
	void getPortMappingsForPortMappingTemplateId(@PathParam("id") String portMappingTemplateId, MethodCallback<PortMappingResponse> methodCallback);
}