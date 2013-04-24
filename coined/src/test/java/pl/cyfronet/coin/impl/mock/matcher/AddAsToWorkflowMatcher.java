package pl.cyfronet.coin.impl.mock.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import pl.cyfronet.coin.api.beans.AddAsWithKeyToWorkflow;

public class AddAsToWorkflowMatcher extends BaseMatcher<AddAsWithKeyToWorkflow> {

	private AddAsWithKeyToWorkflow expected;

	public AddAsToWorkflowMatcher(AddAsWithKeyToWorkflow expected) {
		this.expected = expected;
	}

	@Override
	public boolean matches(Object arg0) {
		AddAsWithKeyToWorkflow given = (AddAsWithKeyToWorkflow) arg0;

		return eq(expected.getAsConfigId(), given.getAsConfigId())
				&& eq(expected.getCpu(), given.getCpu())
				&& eq(expected.getDisk(), given.getDisk())
				&& eq(expected.getKeyId(), given.getKeyId())
				&& eq(expected.getMemory(), given.getMemory())
				&& eq(expected.getName(), given.getName());
	}

	public boolean eq(Object expected, Object given) {
		if (expected != null) {
			return expected.equals(given);
		}
		return given == null;
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub

	}

}
