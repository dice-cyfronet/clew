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
		return new Object[][] { { null }, { "50b70f252a9524132a04cae01" }, // to
																			// long
				{ "50b70f252a9524132a04cae" }, // to short
				{ "50b70f252a9524132a04cag" } // illegal char
		};
	}

	@Test(dataProvider = "getNotValidIds")
	public void shouldThrowExceptionWhileIdIsNotValid(String notValidId)
			throws Exception {
		try {
			Validator.validateId(notValidId);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}

	@DataProvider
	protected Object[][] getValidRedirectionNames() {
		return new Object[][] { { "asdf" }, { "ASDF" }, {"123"},  { "-" }, { "_" },
				{ "aA_-as123" } };
	}

	@Test(dataProvider = "getValidRedirectionNames")
	public void shouldValidateCorrectRedirectionName(String redirectionName)
			throws Exception {
		Validator.validateRedirectionName(redirectionName);
	}

	@DataProvider
	protected Object[][] getInvalidRedirectionNames() {
		return new Object[][] { { "*" }, { "ASD(F" }, {"123@"}, { "-&" }, { "$_" },
				{ "aA_-as#123" } };
	}

	@Test(dataProvider = "getInvalidRedirectionNames")
	public void shouldThrownExceptionOnInvalidRedirectionName(
			String redirectionName) throws Exception {
		try {
			Validator.validateRedirectionName(redirectionName);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}
	
	@Test
	public void shouldThrowExceptionWhenAsNameToLong() throws Exception {
		String toLongName = "asdfghjklaasdfghjklaasdfghjklaasdfghjklaasdfghjklaasdfghjklaasdfg";
		try {
			Validator.validateASName(toLongName);
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 400);
		}
	}
}
