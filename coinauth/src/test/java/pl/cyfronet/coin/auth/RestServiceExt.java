package pl.cyfronet.coin.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface RestServiceExt extends RestService {

	@GET
	@Path("/nonexisting")
	void nonExisting();
}
