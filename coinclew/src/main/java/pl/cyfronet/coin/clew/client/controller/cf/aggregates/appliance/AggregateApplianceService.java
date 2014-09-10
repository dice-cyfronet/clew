package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface AggregateApplianceService extends RestService {
	@GET
	@Path("clew/appliance_instances?appliance_set_type={type}")
	void getAggregateAppliances(@PathParam("type") String workflowType, MethodCallback<AggregateAppliancesResponse> callback);
}