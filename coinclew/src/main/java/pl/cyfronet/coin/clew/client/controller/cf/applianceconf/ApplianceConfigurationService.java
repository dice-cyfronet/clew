package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

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
public interface ApplianceConfigurationService extends RestService {
	@GET
	@Path("appliance_configuration_templates?appliance_type_id={applianceTypeId}")
	void getApplianceConfigurations(@PathParam("applianceTypeId") String applianceTypeId,
			MethodCallback<ApplianceConfigurationsResponse> methodCallback);

	@POST
	@Path("appliance_configuration_templates")
	void addApplianceConfiguration(
			NewApplianceConfigurationRequest newApplianceConfigurationRequest,
			MethodCallback<ApplianceConfigurationRequestResponse> methodCallback);

	@DELETE
	@Path("appliance_configuration_templates/{id}")
	void deleteApplianceConfiguration(@PathParam("id") String applianceConfigurationId,
			MethodCallback<Void> methodCallback);

	@PUT
	@Path("appliance_configuration_templates/{id}")
	void updateApplianceConfiguration(@PathParam("id") String configId,
			ApplianceConfigurationRequestResponse applianceConfigurationRequest,
			MethodCallback<ApplianceConfigurationRequestResponse> methodCallback);

	//TODO: remove the token parameter when
	//https://bugs.chromium.org/p/chromium/issues/detail?id=633696 is fixed
	@GET
	@Path("appliance_configuration_templates?_={token}&id={ids}")
	void getApplianceConfigurationsForIds(@PathParam("ids") String ids,
			@PathParam("token") long date,
			MethodCallback<ApplianceConfigurationsResponse> methodCallback);
}
