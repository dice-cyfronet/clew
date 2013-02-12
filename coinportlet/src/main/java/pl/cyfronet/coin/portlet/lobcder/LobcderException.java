package pl.cyfronet.coin.portlet.lobcder;

public class LobcderException extends Exception {
	private static final long serialVersionUID = 6696158262285406902L;
	
	public LobcderException(String message) {
		super(message);
	}

	public LobcderException(String message, Throwable cause) {
		super(message, cause);
	}
}