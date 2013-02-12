package pl.cyfronet.coin.auth.rs;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.ws.rs.WebApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pl.cyfronet.coin.auth.AuthService;

//@formatter:off
@ContextConfiguration( locations={
		"classpath:service-authorization.xml"
	} )
//@formatter:on
public class BasicRsAuthorizationHandlerTest extends AuthHandlerTest {

	@Autowired
	AuthService authService;

	@BeforeMethod
	protected void setUp() {
		reset(authService);
	}

	@Test
	public void shouldGrantAccess() throws Exception {
		whenAuthorizationSucessfull();
		whenInvokeMethod();
		thenAccessGranted();
	}

	private void whenAuthorizationSucessfull() {
		when(authService.authorize(eq("ticket"), any(Method.class)))
				.thenReturn(true);
	}

	private void whenInvokeMethod() {
		client.withDeveloperRole();
	}

	private void thenAccessGranted() {
		verify(authService, times(1))
				.authorize(eq("ticket"), any(Method.class));
	}

	@Test
	public void shouldForbidAccess() throws Exception {
		givenAuthorizationFailed();
		try {
			whenInvokeMethod();
			fail();
		} catch (WebApplicationException e) {
			assertEquals(e.getResponse().getStatus(), 403);
		}
	}

	private void givenAuthorizationFailed() {
		when(authService.authorize(eq("ticket"), any(Method.class)))
				.thenReturn(false);
	}

}
