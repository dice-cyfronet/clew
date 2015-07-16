package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.JsonEncoderDecoder.DecodingException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;

public class CfErrorReader {
	public interface ErrorCodec extends JsonEncoderDecoder<CloudFacadeError> {}
	
	private ErrorCodec codec;
	
	public CfErrorReader() {
		codec = GWT.create(ErrorCodec.class);
	}
	
	public CloudFacadeError decodeError(Response response) {
		try {
			CloudFacadeError error = codec.decode(JSONParser.parseStrict(response.getText()));
			
			if(response.getHeader("X-Request-Id") != null) {
				error.setRequestId(response.getHeader("X-Request-Id"));
			}
			
			return error;
		} catch(DecodingException | IllegalArgumentException e) {
			CloudFacadeError error = new CloudFacadeError();
			error.setType("unknown");
			error.setMessage("Unknown error");
			
			return error;
		}
	}
}