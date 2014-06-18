package pl.cyfronet.coin.clew.client.controller.cf.flavor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface FlavorService extends RestService {
	@GET
	@Path("virtual_machine_flavors?cpu={cpu}&memory={memory}&hdd={hdd}&appliance_type_id={appliance_type_id}")
	void getFlavors(@PathParam("appliance_type_id") String applianceTypeId, @PathParam("cpu") String cpu, @PathParam("memory") String memory,
			@PathParam("hdd") String hdd, MethodCallback<FlavorsResponse> methodCallback);

	@GET
	@Path("virtual_machine_flavors?cpu={cpu}&memory={memory}&hdd={hdd}&appliance_type_id={appliance_type_id}&compute_site_id={compute_site_id}")
	void getFlavors(@PathParam("appliance_type_id") String applianceTypeId, @PathParam("cpu") String cpu, @PathParam("memory") String memory,
			@PathParam("hdd") String hdd, @PathParam("compute_site_id") String computeSiteId, MethodCallback<FlavorsResponse> methodCallback);

	@GET
	@Path("virtual_machine_flavors?id={flavor_ids}")
	void getFlavors(@PathParam("flavor_ids") String flavorIds, MethodCallback<FlavorsResponse> methodCallback);
}