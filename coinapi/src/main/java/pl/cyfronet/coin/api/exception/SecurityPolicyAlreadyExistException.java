package pl.cyfronet.coin.api.exception;

import javax.ws.rs.core.Response;

public class SecurityPolicyAlreadyExistException extends CloudFacadeException {

	private static final long serialVersionUID = 1L;
	
	public SecurityPolicyAlreadyExistException() {
		super(Response.Status.CONFLICT);
	}

}
