package pl.cyfronet.coin.impl.action.ownedpayload;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.exception.NotFoundException;
import pl.cyfronet.coin.impl.action.Action;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class GetOwnedPayloadActionTest extends OwnedPayloadActionTest {

	private NamedOwnedPayload payload;

	@Test
	public void shouldGetOwnedPayload() throws Exception {
		givenOwnedPayloadStoredInAirOwnerNotImportant();
		whenGetOwnedPayload();
		thenOwnedPayloadLoaded();
	}

	private void whenGetOwnedPayload() {
		Action<NamedOwnedPayload> action = getMethodProvider()
				.getGetOwnedPayloadAction(policyName);
		payload = action.execute();
	}

	private void thenOwnedPayloadLoaded() {
		assertEquals(payload.getName(), policyName);
		assertEquals(payload.getPayload(), policyText);
		assertEquals(payload.getOwners(), Arrays.asList(username));
	}

	@Test
	public void shouldThrow404WhenPolicyNotFound() throws Exception {
		givenAirWithoutAskedOwnedPayload(null);
		try {
			whenGetOwnedPayload();
			fail();
		} catch (NotFoundException e) {
			// OK should be thrown.
		}
	}
}
