package pl.cyfronet.coin.clew.client.controller.cf;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class CfErrorReader {
	public String decodeError(String json) {
		JSONValue jsonValue = JSONParser.parseStrict(json);
		
		if (jsonValue.isObject() != null) {
			JSONObject jsonObject = jsonValue.isObject();
			JSONArray errors = null;
			
			if (jsonObject.containsKey("base") && (errors = jsonObject.get("base").isArray()) != null) {
				if (errors.size() > 0 && errors.get(0).isString() != null) {
					return errors.get(0).isString().stringValue();
				}
			}
		}

		return "Unknown error occurred";
	}
}