package pl.cyfronet.coin.auth.rs;

import java.lang.reflect.Method;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.invoker.MethodDispatcher;
import org.apache.cxf.service.model.BindingOperationInfo;

public class AuthHandler {

	protected Method getTargetMethod(Message m) {
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
}
