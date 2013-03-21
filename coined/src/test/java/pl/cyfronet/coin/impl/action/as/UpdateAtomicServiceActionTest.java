package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicServiceRequest;
import pl.cyfronet.coin.api.exception.NotAllowedException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.ApplianceType;
import pl.cyfronet.coin.impl.mock.matcher.UpdateAtomicServiceMatcher;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class UpdateAtomicServiceActionTest extends ActionTest {

	private String username = "marek";
	private String asId = "asId";
	private AtomicServiceRequest updatedAs;
	private UpdateAtomicServiceMatcher matcher;

	@Test
	public void shouldUpdateAtomicService() throws Exception {
		givenAtomicServiceToUpdate();
		whenUpdateOwnedAtomicService();
		thenAtomicServiceUpdated();
	}

	private void givenAtomicServiceToUpdate() {
		ApplianceType at = new ApplianceType();
		at.setId(asId);
		at.setName("old name");
		at.setDescription("old description");
		at.setProxy_conf_name("old/proxy/name");
		at.setPublished(false);
		at.setScalable(true);
		at.setShared(true);
		at.setAuthor(username);

		updatedAs = new AtomicServiceRequest();
		updatedAs.setName("updated name");
		updatedAs.setDescription("new description");
		updatedAs.setProxyConfigurationName("new/proxy/name");
		updatedAs.setPublished(true);
		updatedAs.setScalable(false);

		matcher = new UpdateAtomicServiceMatcher(at, updatedAs);
		when(air.getApplianceTypes()).thenReturn(Arrays.asList(at));
	}

	private void whenUpdateOwnedAtomicService() {
		whenUpdateAtomicService(username, false);
	}

	private void whenUpdateAtomicService(String username, boolean admin) {
		Action<Class<Void>> action = actionFactory
				.createUpdateAtomicServiceAction(username, asId, updatedAs,
						admin);
		action.execute();
	}

	private void thenAtomicServiceUpdated() {
		verify(air, times(1)).updateAtomicService(eq(asId), argThat(matcher));
	}

	@Test
	public void shouldUpdateNotOwnedASIfAdmin() throws Exception {
		givenAtomicServiceToUpdate();
		whenUpdateNotOwnedAtomicServiceAsAdmin();
		thenAtomicServiceUpdated();
	}

	private void whenUpdateNotOwnedAtomicServiceAsAdmin() {
		whenUpdateAtomicService("wojtek", true);
	}

	@Test
	public void shouldThrow() throws Exception {
		givenAtomicServiceToUpdate();
		try {
			whenUpdateNotOwnedAtomicService();
			fail();
		} catch (NotAllowedException e) {
			// Ok should be thrown
		}
	}

	private void whenUpdateNotOwnedAtomicService() {
		whenUpdateAtomicService("wojtek", false);
	}

	@Test
	public void shouldThrow406WhenWrongEntity() throws Exception {
		givenAirThrow400Exception();
		try {
			whenUpdateOwnedAtomicService();
			fail();
		} catch (NotAllowedException e) {
			// OK should be thrown.
		}
	}

	private void givenAirThrow400Exception() {
		givenAtomicServiceToUpdate();
		doThrow(new NotAllowedException()).when(air).updateAtomicService(
				eq(asId), argThat(matcher));
	}
}
