package pl.cyfronet.coin.api.exception;

import javax.ws.rs.core.Response;
import javax.xml.ws.WebFault;

@WebFault
public class SecurityPolicyNotFoundException extends CloudFacadeException {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 6823966272806907966L;

	public SecurityPolicyNotFoundException() {
		super(Response.Status.NOT_FOUND);
	}
}
