package pl.cyfronet.coin.impl.action.grant;

import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import javax.ws.rs.WebApplicationException;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.Grant;
import pl.cyfronet.coin.api.exception.GrantNotFoundException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;
import pl.cyfronet.coin.impl.air.client.GrantBean;

public class GetGrantActionTest extends ActionTest {

	private String grantName = "my/grant";
	private GrantBean grantBean;
	private Grant grant;

	@Test
	public void shouldGetGrant() throws Exception {
		givenAirWithGrant();
		whenGetGrant();
		thenGrantLoaded();
	}

	private void givenAirWithGrant() {
		grantBean = new GrantBean();
		grantBean.setPayload_get("get");
		grantBean.setPayload_post("post");
		grantBean.setPayload_put("put");
		grantBean.setPayload_delete("delete");

		when(air.getGrant(grantName)).thenReturn(grantBean);
	}

	private void whenGetGrant() {
		Action<Grant> action = actionFactory.createGetGrantAction(grantName);
		grant = action.execute();
	}

	private void thenGrantLoaded() {
		assertEquals(grant.getGet(), grantBean.getPayload_get());
		assertEquals(grant.getPost(), grantBean.getPayload_post());
		assertEquals(grant.getPut(), grantBean.getPayload_put());
		assertEquals(grant.getDelete(), grantBean.getPayload_delete());
	}

	@Test
	public void shouldThrow404WhenGrantNotFound() throws Exception {
		givenNonExistingGrant();
		try {
			whenGetGrant();
			fail();
		} catch (GrantNotFoundException e) {
			// OK should be thrown
		}

	}

	private void givenNonExistingGrant() {
		when(air.getGrant(grantName)).thenThrow(new WebApplicationException(400));
	}
}
