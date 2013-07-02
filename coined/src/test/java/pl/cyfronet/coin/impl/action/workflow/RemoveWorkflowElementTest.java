/*
 * Copyright 2012 ACC CYFRONET AGH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package pl.cyfronet.coin.impl.action.workflow;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public abstract class RemoveWorkflowElementTest extends WorkflowActionTest {

	protected void givenAiRWithoutWorkflow() {
		mockGetNonExistingWorkflow(air, contextId);
	}
	
	protected void thenWorkflowElementRemoved() throws Exception {
		verify(air, times(1)).getWorkflow(contextId);
		verifyElementRemovedFromAtmosphere(1);
	}
	
	protected void thenOnlyAirActionInvoked() throws Exception {
		verify(air, times(1)).getWorkflow(contextId);
		verifyElementRemovedFromAtmosphere(0);
	}
	
	protected abstract void verifyElementRemovedFromAtmosphere(int times) throws Exception;
}
