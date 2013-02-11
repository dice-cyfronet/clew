package pl.cyfronet.coin.auth.rs;

import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

import pl.cyfronet.coin.auth.AuthService;

public class BasicRsAuthorizationHandler extends AuthHandler implements
		RequestHandler {

	@Autowired
	private AuthService authService;

	@Override
	protected Response internalHandleRequest(Message m,
			ClassResourceInfo resourceClass, Method method) {
		AuthorizationPolicy policy = (AuthorizationPolicy) m
				.get(AuthorizationPolicy.class);

		if (authService.authorize(policy.getPassword(), method)) {
			return null;
		}

		return Response.status(Response.Status.FORBIDDEN).build();
	}

	@Override
	protected String getPhaseName() {
		return "authorize";
	}
	
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
}
