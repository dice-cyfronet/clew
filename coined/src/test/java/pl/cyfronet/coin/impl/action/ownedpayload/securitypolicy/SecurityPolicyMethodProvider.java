package pl.cyfronet.coin.impl.action.ownedpayload.securitypolicy;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;

import pl.cyfronet.coin.api.beans.NamedOwnedPayload;
import pl.cyfronet.coin.api.beans.OwnedPayload;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.action.ownedpayload.MethodProvider;
import pl.cyfronet.coin.impl.air.client.AirClient;

public class SecurityPolicyMethodProvider implements MethodProvider {

	private AirClient air;
	private ActionFactory actionFactory;

	public SecurityPolicyMethodProvider(AirClient air,
			ActionFactory actionFactory) {
		this.air = air;
		this.actionFactory = actionFactory;
	}

	@Override
	public void mockGetOwnedPayload(String username, String payloadName,
			List<NamedOwnedPayload> result) {
		when(air.getSecurityPolicies(username, payloadName)).thenReturn(result);
	}

	@Override
	public void verifyGetOwnedPayload(int times, String username,
			String payloadName) {
		verify(air, times(times)).getSecurityPolicies(username, payloadName);
	}

	@Override
	public void verifyOwnedPayloadDeleted(int times, String username,
			String payloadName) {
		verify(air, times(times)).deleteSecurityPolicy(username, payloadName);
	}

	@Override
	public void verifyAddOwnedPayload(int times, String payloadName,
			String payloadText, List<String> owners) {
		verify(air, times(times)).addSecurityPolicy(payloadName, payloadText,
				owners);
	}

	@Override
	public void throwDeleteOwnedPayloadException(int status,
			String differentUser, String payloadName) {
		doThrow(getAirException(status)).when(air).deleteSecurityPolicy(
				differentUser, payloadName);
	}

	@Override
	public void thenVerifyAirAskedAboutOwnedPayload() {
		verify(air, times(1)).getSecurityPolicies(anyString(), anyString());

	}

	@Override
	public void whenGetOwnedPayload(String username, String ownedPayloadName,
			List<NamedOwnedPayload> ownedPayloads) {
		when(air.getSecurityPolicies(username, ownedPayloadName)).thenReturn(
				ownedPayloads);
	}

	@Override
	public void mockOwnedPayloadNotExistsInAir(String policyName) {
		when(air.getSecurityPolicies(anyString(), eq(policyName))).thenReturn(
				new ArrayList<NamedOwnedPayload>());
	}

	@Override
	public void throwExceptionWhileAddingOwnedPayload(int status,
			String payloadName, String payloadText, List<String> owners) {
		doThrow(
				new ServerWebApplicationException(Response.status(status)
						.build())).when(air).addSecurityPolicy(payloadName,
				payloadText, owners);
	}

	@Override
	public Action<Class<Void>> getDeleteOwnedPayloadAction(String username,
			String payloadName) {
		return actionFactory.getPoliciesActionFactory().createDeleteAction(
				username, payloadName);
	}

	@Override
	public Action<NamedOwnedPayload> getGetOwnedPayloadAction(String payloadName) {
		return actionFactory.getPoliciesActionFactory().createGetAction(
				payloadName);
	}

	@Override
	public Action<String> getOwnedPayloadPayloadAction(String payloadName) {
		return actionFactory.getPoliciesActionFactory().createGetPayloadAction(
				payloadName);
	}

	@Override
	public Action<List<String>> getListOwnedPayloadAction() {
		return actionFactory.getPoliciesActionFactory().createListAction();
	}

	@Override
	public Action<Class<Void>> getNewOwnedPayloadAction(String username,
			NamedOwnedPayload newPayload) {
		return actionFactory.getPoliciesActionFactory().createNewAction(
				username, newPayload);
	}

	private ServerWebApplicationException getAirException(int status) {
		return new ServerWebApplicationException(Response.status(status)
				.build());
	}

	@Override
	public Action<Class<Void>> createUpdateOwnedPayloadAction(String username,
			String payloadName, OwnedPayload newPayload) {
		return actionFactory.getPoliciesActionFactory().createUpdateAction(
				username, payloadName, newPayload);
	}

	@Override
	public void verifyUpdateOwnedPayload(int times, String username,
			String payloadName, String newPayload, List<String> newOwners) {
		verify(air, times(times)).updateSecurityPolicy(username, payloadName,
				newPayload, newOwners);
	}

	@Override
	public void throwUpdateOwnedPayloadException(int i, String differentUser,
			String policyName, String newPayload, List<String> newOwners) {
		doThrow(getAirException(404)).when(air).updateSecurityPolicy(
				differentUser, policyName, newPayload, newOwners);
	}
}
