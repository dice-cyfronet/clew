package pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface AggregateApplianceTypeService extends RestService {
	@GET
	@Path("clew/appliance_types?mode={mode}")
	void getAggregateAppliances(@PathParam("mode") String mode, MethodCallback<AggregateApplianceTypesResponse> callback);
}