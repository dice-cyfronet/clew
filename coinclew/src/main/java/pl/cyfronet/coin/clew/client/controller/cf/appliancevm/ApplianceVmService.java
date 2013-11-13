package pl.cyfronet.coin.clew.client.controller.cf.appliancevm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeKey;

public interface ApplianceVmService extends RestService {
	@GET
	@Path("virtual_machines?private_token=" + CloudFacadeKey.KEY)
	void getApplianceVms(MethodCallback<ApplianceVmsResponse> methodCallback);
}