package pl.cyfronet.coin.clew.client.controller.cf.devmodepropertyset;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface DevelopmentModePropertySetService extends RestService {
	@GET
	@Path("dev_mode_property_sets?appliance_id={id}")
	void getDevelopmentModePropertySet(@PathParam("id") String applianceId, MethodCallback<DevelopmentModePropertySetsResponse> methodCallback);
	
	@PUT
	@Path("dev_mode_property_sets/{id}")
	void updateDevelopmentModePropertySet(@PathParam("id") String developmentModePropertySetId, MethodCallback<DevelopmentModePropertySetRequestResponse> methodCallback);
}