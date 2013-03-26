package pl.cyfronet.coin.impl.utils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ValidatorTest {

	@Test
	public void shouldNotThrowsExceptionWhileIdValid() throws Exception {
		Validator.validateId("50b70f252a9524132a04cae0");
	}
	
	@DataProvider
	protected Object[][] getNotValidIds() {
		return new Object[][] {
				{null},
				{"50b70f252a9524132a04cae01"}, //to long
				{"50b70f252a9524132a04cae"}, // to short
				{"50b70f252a9524132a04cag"} // illegal char
		};
	}
	
	@Test(dataProvider="getNotValidIds")
	public void shouldThrowExceptionWhileIdIsNotValid(String notValidId) throws Exception {
		try {
			Validator.validateId(notValidId);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}
}
