package pl.cyfronet.coin.api.exception;

import javax.ws.rs.core.Response;

public class NotAllowedException extends CloudFacadeException {
	
	private static final long serialVersionUID = -7465666909769381326L;

	public NotAllowedException() {
		super(Response.Status.FORBIDDEN);
	}

	public NotAllowedException(String msg) {
		super(msg, Response.Status.FORBIDDEN);
	}
}
