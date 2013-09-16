package pl.cyfronet.coin.clew.client.controller.cf;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

public interface ApplianceTypesService extends RestService {
	@GET
	@Path("appliance_types?private_token=secret")
	void getApplianceTypes(MethodCallback<ApplianceTypesResponse> methodCallback);
	
	@POST
	@Path("appliance_types?private_token=secret")
	void addApplianceType(NewApplianceType newApplianceType, MethodCallback<Void> methodCallback);
}