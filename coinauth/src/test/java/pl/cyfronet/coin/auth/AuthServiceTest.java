package pl.cyfronet.coin.auth;

import static org.testng.AssertJUnit.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pl.cyfronet.coin.auth.mi.MasterInterfaceAuthClient;
import pl.cyfronet.coin.auth.mi.UserDetails;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class AuthServiceTest {

	private Method method;
	private AuthService authService;
	private MasterInterfaceAuthClient miAuthClient;
	private boolean authenticated;
	private String userToken = "userToken";
	
	@BeforeMethod
	protected void setUp() {
		authService = new AuthService();
		miAuthClient = mock(MasterInterfaceAuthClient.class);
		authService.setAuthClient(miAuthClient);
	}

	@DataProvider
	protected Object[][] getPublicMethods() {
		return new Object[][] { { "publicMethod" }, { "publicFirst" } };
	}

	@Test(dataProvider = "getPublicMethods")
	public void shouldAuthorizePublicMethod(String methodName) throws Exception {
		givenServiceWithPublicMethod(methodName);
		whenAuthorizeMethod();
		thenAuthorizationSucessfull(0);
	}

	private void givenServiceWithPublicMethod(String methodName)
			throws Exception {
		method = RestServiceImpl.class.getMethod(methodName);
	}

	private void whenAuthorizeMethod() {
		authenticated = authService.authorize(userToken, method);
	}

	private void thenAuthorizationSucessfull(int authInvokeSize) {
		assertTrue(authenticated);
		verify(miAuthClient, times(authInvokeSize)).validate(anyString());
	}
	
	@Test
	public void shouldThrowPermissionDeniedWhenRoleMissing() throws Exception {
		givenMethodRequiredDeveloperRole();
		givenUserWithRoles("normal", "user");		
		whenAuthorizeMethod();
		thenAuthorizationFailed();
	}
	
	private void givenMethodRequiredDeveloperRole() throws Exception {
		method = RestServiceImpl.class.getMethod("withDeveloperRole");
	}
	
	private void givenUserWithRoles(String... roles) throws Exception {		
		UserDetails ud = new UserDetails();
		ud.setRole(Arrays.asList(roles));
		
		when(miAuthClient.validate(userToken)).thenReturn(ud);
	}

	private void thenAuthorizationFailed() {
		assertFalse(authenticated);
		verify(miAuthClient, times(1)).validate(userToken);
	}
	
	@Test
	public void shouldAuthorizeWheUserHasRequiredRoles() throws Exception {
		givenMethodRequiredDeveloperRole();
		givenUserWithRoles("developer");		
		whenAuthorizeMethod();
		thenAuthorizationSucessfull(1);
	}
}
