package pl.cyfronet.coin.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public interface RestService {

	@GET
	@Path("/publicMethod")
	public void publicMethod();

	@GET
	@Path("/publicFirst")
	public void publicFirst();
	
	@GET
	@Path("/withoutAnnotations")
	public void withoutAnnotations();
	
	@GET
	@Path("/withDeveloperRole")
	public void withDeveloperRole();
}
