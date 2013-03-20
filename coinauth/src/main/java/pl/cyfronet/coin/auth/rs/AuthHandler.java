package pl.cyfronet.coin.auth.rs;

import java.lang.reflect.Method;

import javax.ws.rs.core.Response;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.auth.annotation.Public;

public abstract class AuthHandler implements RequestHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthHandler.class);

	@Override
	public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
		try {
			Method method = getTargetMethod(m);

			logger.debug(
					"trying to {} user for {} method with following annotations {}",
					new Object[] { getPhaseName(), method,
							method.getAnnotations() });

			if (isPublic(method)) {
				return null;
			}

			return internalHandleRequest(m, resourceClass, method);
		} catch (AccessDeniedException e) {
			return Response.status(404).build();
		}
	}

	protected abstract Response internalHandleRequest(Message m,
			ClassResourceInfo resourceClass, Method method);

	protected abstract String getPhaseName();

	private Method getTargetMethod(Message m) {
		BindingOperationInfo bop = m.getExchange().get(
				BindingOperationInfo.class);
		if (bop != null) {
			MethodDispatcher md = (MethodDispatcher) m.getExchange()
					.get(Service.class).get(MethodDispatcher.class.getName());
			return md.getMethod(bop);
		}
		Method method = (Method) m.get("org.apache.cxf.resource.method");
		if (method != null) {
			return method;
		}
		throw new AccessDeniedException(
				"Method is not available : Unauthorized");
	}

	private boolean isPublic(Method method) {
		return method.getAnnotation(Public.class) != null;
	}
}
