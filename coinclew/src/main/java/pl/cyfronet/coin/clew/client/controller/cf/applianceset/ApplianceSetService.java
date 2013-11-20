package pl.cyfronet.coin.clew.client.controller.cf.applianceset;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;
import pl.cyfronet.coin.clew.client.controller.cf.applianceset.NewApplianceSet.Type;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface ApplianceSetService extends RestService {
	@GET
	@Path("appliance_sets")
	void getApplianceSets(MethodCallback<ApplianceSetsResponse> methodCallback);
	
	@POST
	@Path("appliance_sets")
	void addApplianceSet(NewApplianceSetRequest newApplianceSetRequest, MethodCallback<ApplianceSetRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliance_sets/{id}")
	void deleteApplianceSet(@PathParam("id") String appliancesetId, MethodCallback<Void> methodCallback);
	
	@GET
	@Path("appliance_sets?appliance_set_type={type}")
	void getApplianceSets(@PathParam("type") Type workflowType, MethodCallback<ApplianceSetsResponse> methodCallback);
}