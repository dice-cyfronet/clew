package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.JsonEncoderDecoder.DecodingException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONParser;

public class CfErrorReader {
	public interface ErrorCodec extends JsonEncoderDecoder<CloudFacadeError> {}
	
	private ErrorCodec codec;
	
	public CfErrorReader() {
		codec = GWT.create(ErrorCodec.class);
	}
	
	public CloudFacadeError decodeError(String json) {
		try {
			return codec.decode(JSONParser.parseStrict(json));
		} catch(DecodingException e) {
			CloudFacadeError error = new CloudFacadeError();
			error.setType("unknown");
			error.setMessage("Unknown error");
			
			return error;
		}
	}
}