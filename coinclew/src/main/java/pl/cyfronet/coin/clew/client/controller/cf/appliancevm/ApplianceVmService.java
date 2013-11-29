package pl.cyfronet.coin.clew.client.controller.cf.appliancevm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface ApplianceVmService extends RestService {
	@GET
	@Path("virtual_machines?appliance_id={id}")
	void getApplianceVms(@PathParam("id") String applianceInstanceId, MethodCallback<ApplianceVmsResponse> methodCallback);
}