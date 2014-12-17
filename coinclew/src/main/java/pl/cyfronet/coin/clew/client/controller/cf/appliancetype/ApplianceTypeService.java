package pl.cyfronet.coin.clew.client.controller.cf.appliancetype;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface ApplianceTypeService extends RestService {
	@GET
	@Path("appliance_types/{id}")
	void getApplianceType(@PathParam("id") String id, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
	
	@GET
	@Path("appliance_types")
	void getApplianceTypes(MethodCallback<ApplianceTypesResponse> methodCallback);
	
	@POST
	@Path("appliance_types")
	void addApplianceType(SaveApplianceTypeRequest saveRequest, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
	
	@PUT
	@Path("appliance_types/{id}")
	void updateApplianceType(@PathParam("id") String id,
			ApplianceTypeRequestResponse applianceTypeRequest, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliance_types/{id}")
	void deleteApplianceType(@PathParam("id") String id, MethodCallback<Void> methodCallback);

	@GET
	@Path("appliance_types?id={ids}")
	void getApplianceTypesForIds(@PathParam("ids") String applianceTypeIds, MethodCallback<ApplianceTypesResponse> methodCallback);

	@GET
	@Path("appliance_types?mode={mode}&active={active}")
	void getApplianceTypesForModeAndActiveFlag(@PathParam("mode") String mode, @PathParam("active") boolean active, MethodCallback<ApplianceTypesResponse> methodCallback);

	@GET
	@Path("appliance_types?mode=manage")
	void getManagedApplianceTypes(MethodCallback<ApplianceTypesResponse> methodCallback);

	@PUT
	@Path("appliance_types/{id}")
	void saveInPlace(@PathParam("id") String applianceTypeId, ApplianceTypeSaveInPlaceRequest request, MethodCallback<ApplianceTypeRequestResponse> methodCallback);
}