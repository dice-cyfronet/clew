package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

public interface ApplianceTypeService extends RestService {
	@GET
	@Path("appliance_types?private_token=secret")
	void getApplianceTypes(MethodCallback<ApplianceTypesResponse> methodCallback);
	
	@POST
	@Path("appliance_types?private_token=secret")
	void addApplianceType(NewApplianceTypeRequest newApplianceType, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
	
	@PUT
	@Path("appliance_types/{id}?private_token=secret")
	void updateApplianceType(@PathParam("id") String id,
			ApplianceTypeRequestResponse applianceType, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliance_types/{id}?private_token=secret")
	void deleteApplianceType(@PathParam("id") String id, MethodCallback<Void> methodCallback);
}