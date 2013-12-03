package pl.cyfronet.coin.clew.client.controller.cf.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface UserService extends RestService {
	@GET
	@Path("users?id={ids}")
	void getUsers(@PathParam("ids") String userIds, MethodCallback<UsersResponse> methodCallback);

	@GET
	@Path("users/{id}")
	void getUser(@PathParam("id") String userId, MethodCallback<UserRequestResponse> methodCallback);

	@GET
	@Path("users?login={login}")
	void getUserForLogin(@PathParam("login") String userLogin, MethodCallback<UsersResponse> methodCallback);
}