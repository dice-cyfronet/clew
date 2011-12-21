package pl.cyfronet.coinapi;

public class CloudFacadeException extends Exception {
	private static final long serialVersionUID = -1200644885931600000L;
	
	private ErrorCode errorCode;
	
	public enum ErrorCode {
		UNKNOWN;
	}
	
	public CloudFacadeException() {
		errorCode = ErrorCode.UNKNOWN;
	}
	
	public CloudFacadeException(String message) {
		super(message);
		errorCode = ErrorCode.UNKNOWN;
	}
	
	public CloudFacadeException(ErrorCode errorCode) {
		super();
	}
	
	public CloudFacadeException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}