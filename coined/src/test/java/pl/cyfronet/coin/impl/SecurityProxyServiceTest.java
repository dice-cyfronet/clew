package pl.cyfronet.coin.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.test.context.ContextConfiguration;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.impl.action.Action;

//@formatter:off
@ContextConfiguration(locations={
		"classpath:securityproxy/properties.xml",
		"classpath:securityproxy/client.xml",
		"classpath:rest-test-imports.xml",
		"classpath:rest-test-mocks.xml",		
		"classpath:META-INF/spring/rest-services.xml"
	})
//@formatter:on
public class SecurityProxyServiceTest extends AbstractOwnedPayloadServiceTest {

	@Override
	protected void mockListAction(Action<List<String>> action) {
		when(actionFactory.getProxiesActionFactory().createListAction())
				.thenReturn(action);
	}

	@Override
	protected void mockGetAction(String policyName,
			Action<NamedOwnedPayload> action) {
		when(
				actionFactory.getProxiesActionFactory().createGetAction(
						policyName)).thenReturn(action);
	}

	@Override
	protected void mockGetPayloadAction(String name, Action<String> action) {
		when(
				actionFactory.getProxiesActionFactory().createGetPayloadAction(
						name)).thenReturn(action);
	}

	@Override
	protected void mockNewAction(String username, NamedOwnedPayload payload,
			Action<Class<Void>> action) {
		when(
				actionFactory.getProxiesActionFactory().createNewAction(
						username, payload)).thenReturn(action);
	}

	@Override
	protected void mockDeleteAction(String username, String name,
			Action<Class<Void>> action) {
		when(
				actionFactory.getProxiesActionFactory().createDeleteAction(
						username, name)).thenReturn(action);
	}

	@Override
	protected void verifyNewAction(String username, NamedOwnedPayload payload) {
		verify(actionFactory.getProxiesActionFactory(), times(1))
				.createNewAction(username, payload);
	}

	@Override
	protected void verifyDeleteAction(String username, String name) {
		verify(actionFactory.getProxiesActionFactory(), times(1))
				.createDeleteAction(username, name);
	}
}
