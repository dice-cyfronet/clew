package pl.cyfronet.coin.clew.client.controller.cf.userkey;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;

import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeDispatcher;

@Options(dispatcher = CloudFacadeDispatcher.class)
public interface UserKeyService extends RestService {
	@GET
	@Path("user_keys")
	void getUserKeys(MethodCallback<UserKeysResponse> methodCallback);
	
	@POST
	@Path("user_keys")
	void addUserKey(NewUserKeyRequest userKeyRequest, MethodCallback<UserKeyRequestResponse> methodCallback);
	
	@DELETE
	@Path("user_keys/{id}")
	void deleteUserKey(@PathParam("id") String keyId, MethodCallback<Void> methodCallback);
}