package pl.cyfronet.coin.clew.client.controller.cf.applianceconf;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeKey;

public interface ApplianceConfigurationService extends RestService {
	@GET
	@Path("appliance_configuration_templates?appliance_type_id={applianceTypeId}&private_token=" + CloudFacadeKey.KEY)
	void getApplianceConfigurations(@PathParam("applianceTypeId") String applianceTypeId, MethodCallback<ApplianceConfigurationsResponse> methodCallback);
	
	@POST
	@Path("appliance_configuration_templates?private_token=" + CloudFacadeKey.KEY)
	void addApplianceConfiguration(NewApplianceConfigurationRequest newApplianceInstanceRequest, MethodCallback<ApplianceConfigurationRequestResponse> methodCallback);
	
	@DELETE
	@Path("appliance_configuration_templates/{id}?private_token=" + CloudFacadeKey.KEY)
	void deleteApplianceConfiguration(@PathParam("id") String applianceConfigurationId, MethodCallback<Void> methodCallback);
}