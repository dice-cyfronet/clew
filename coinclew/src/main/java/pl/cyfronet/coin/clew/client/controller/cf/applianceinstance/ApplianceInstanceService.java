package pl.cyfronet.coin.clew.client.controller.cf.applianceinstance;

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
public interface ApplianceInstanceService extends RestService {
	@GET
	@Path("appliances")
	void getApplianceInstances(MethodCallback<ApplianceInstancesResponse> methodCallback);
	
	@POST
	@Path("appliances")
	void addApplianceInstance(NewApplianceInstanceRequest newApplianceInstanceRequest, MethodCallback<ApplianceInstanceRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliances/{id}")
	void deleteApplianceInstance(@PathParam("id") String applianceInstanceId, MethodCallback<Void> methodCallback);

	@GET
	@Path("appliances?appliance_set_id={id}")
	void getApplianceInstances(@PathParam("id") String appliancesetId, MethodCallback<ApplianceInstancesResponse> methodCallback);
}