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
	public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
		Method method = getTargetMethod(m);
		AuthorizationPolicy policy = (AuthorizationPolicy) m
				.get(AuthorizationPolicy.class);

		if (authService.authorize(policy.getPassword(), method)) {
			return null;
		}

		return Response.status(Response.Status.FORBIDDEN).build();
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
}
