package pl.cyfronet.coin.auth.rs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import pl.cyfronet.coin.auth.RestServiceExt;

public class AuthHandlerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	@Qualifier("client")
	protected RestServiceExt client;
}
