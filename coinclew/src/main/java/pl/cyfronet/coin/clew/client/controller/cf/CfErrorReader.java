package pl.cyfronet.coin.clew.client.controller.cf;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONParser;

public class CfErrorReader {
	public interface ErrorCodec extends JsonEncoderDecoder<CloudFacadeError> {}
	
	private ErrorCodec codec;
	
	public CfErrorReader() {
		codec = GWT.create(ErrorCodec.class);
	}
	
	public CloudFacadeError decodeError(String json) {
		return codec.decode(JSONParser.parseStrict(json));
	}
}