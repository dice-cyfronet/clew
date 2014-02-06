package pl.cyfronet.coin.clew.client.test;

import java.util.HashMap;

import org.fusesource.restygwt.client.JsonEncoderDecoder;

import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.NewApplianceInstance;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

public class MapSerializationGwtTest extends GWTTestCase {
	public interface NewApplianceInstanceCodec extends JsonEncoderDecoder<NewApplianceInstance> {}

	@Override
	public String getModuleName() {
		return "pl.cyfronet.coin.clew.clew";
	}

	public void testNewApplianceInstanceSerialization() {
		NewApplianceInstanceCodec codec = GWT.create(NewApplianceInstanceCodec.class);
		NewApplianceInstance applianceInstance = new NewApplianceInstance();
		applianceInstance.setApplianceSetId("id");
		applianceInstance.setName("appliance");
		applianceInstance.setConfigurationTemplateId("configId");
		applianceInstance.setParams(new HashMap<String, String>() {{
			put("param1", "value1");
			put("param2", "value2");
		}});
		JSONValue value = codec.encode(applianceInstance);
		NewApplianceInstance decodedInstance = codec.decode(value);
		assertTrue(decodedInstance.getParams().keySet().toString() + " should contain param1 without additional quote marks",
				decodedInstance.getParams().keySet().contains("param1"));
		assertTrue(decodedInstance.getParams().keySet().toString() + " should contain param2 without additional quote marks",
				decodedInstance.getParams().keySet().contains("param2"));
	}
}
