package pl.cyfronet.coin.impl.action.grant;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

public class ListGrantsActionTest extends ActionTest {

	List<String> twoGrantsNames = Arrays.asList("grant1", "grant2");
	List<String> grants;

	@Test
	public void shouldListGrants() throws Exception {
		givenAIRWith2Grants();
		whenListGrantsNames();
		then2GrantsNamesLoaded();
	}

	private void givenAIRWith2Grants() {
		when(air.getGrantsNames()).thenReturn(twoGrantsNames);
	}

	private void whenListGrantsNames() {
		Action<List<String>> action = actionFactory.createListGrantsAction();
		grants = action.execute();
	}

	private void then2GrantsNamesLoaded() {
		assertEquals(grants, twoGrantsNames);
	}

	@Test
	public void shouldListEmptyGrantsList() throws Exception {
		givenAIRWithoutGrants();
		whenListGrantsNames();
		thenEmptyGrantsListLoaded();
	}

	private void givenAIRWithoutGrants() {
		when(air.getGrantsNames()).thenReturn(new ArrayList<String>());
	}

	private void thenEmptyGrantsListLoaded() {
		assertEquals(grants.size(), 0);
	}
}
