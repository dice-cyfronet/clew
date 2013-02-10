package pl.cyfronet.coin.auth.rs;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.cyfronet.coin.auth.AuthenticationHandler;

//@formatter:off
@ContextConfiguration( locations={
		"classpath:service-authentication.xml"
	} )
//@formatter:on
public class BasicRsAuthenticationHandlerTest extends AuthHandlerTest {

	@Autowired
	protected AuthenticationHandler authentication;
	
	@BeforeMethod
	protected void setUp() {
		reset(authentication);
	}
	
	@Test
	public void shouldNotCheckUserForPublicMethods() throws Exception {
		whenInvokePublicMethods();
		thenAccessIsGrantedWithoutCheckingUserCredentials();
	}

	private void whenInvokePublicMethods() {
		client.publicMethod();
		client.publicFirst();
	}

	private void thenAccessIsGrantedWithoutCheckingUserCredentials() {
		verify(authentication, times(0)).isAuthenticated(anyString(),
				anyString());
	}

	@Test
	public void shouldAthenticateUser() throws Exception {
		givenExistingUser();
		whenInvokeSecuredMethods();
		thenAuthenticationInvoked();
	}

	private void givenExistingUser() {
		when(authentication.isAuthenticated("User123", "password")).thenReturn(
				true);
	}

	private void whenInvokeSecuredMethods() {
		client.withoutAnnotations();
		client.withDeveloperRole();
	}

	private void thenAuthenticationInvoked() {
		verify(authentication, times(2)).isAuthenticated(eq("User123"),
				eq("password"));
	}
}
