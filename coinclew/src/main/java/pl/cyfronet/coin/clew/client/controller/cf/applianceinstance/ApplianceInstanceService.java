package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

public interface ApplianceInstanceService extends RestService {
	@GET
	@Path("appliances?private_token=XFZVRpda3VVb_K9cfLcy")
	void getApplianceInstances(MethodCallback<ApplianceInstancesResponse> methodCallback);
	
	@POST
	@Path("appliances?private_token=XFZVRpda3VVb_K9cfLcy")
	void addApplianceInstance(NewApplianceInstanceRequest newApplianceInstanceRequest, MethodCallback<ApplianceInstanceRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliances/{id}?private_token=XFZVRpda3VVb_K9cfLcy")
	void deleteApplianceInstance(@PathParam("id") String applianceInstanceId, MethodCallback<Void> methodCallback);
}