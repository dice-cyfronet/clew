/*
 * Copyright 2013 ACC CYFRONET AGH
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

package pl.cyfronet.coin.impl.action.as;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.testng.annotations.Test;

import pl.cyfronet.coin.api.exception.AtomicServiceNotFoundException;
import pl.cyfronet.coin.api.exception.NotAcceptableException;
import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionTest;

public class DeleteAtomicServiceFromAirActionTest extends ActionTest {

	private String asName = "atName";

	@Test
	public void shouldDeleteAtomicService() throws Exception {
		whenRemovingAtomicService();
		thenAtomicServiceAndAllInitConfsRemovedFromAir();
	}

	private void whenRemovingAtomicService() {
		Action<Class<Void>> action = actionFactory
				.createDeleteAtomicServiceFromAirAction(asName);
		action.execute();
	}

	private void thenAtomicServiceAndAllInitConfsRemovedFromAir() {
		verify(air, times(1)).deleteAtomicService(asName, true);
	}

	@Test
	public void shouldDelete2AtomicServices() throws Exception {
		whenRemoving2Ases();
		thenBothASesAndTheirInitConfsAreRemovedFromAir();
	}

	private void whenRemoving2Ases() {
		Action<Class<Void>> action = actionFactory
				.createDeleteAtomicServiceFromAirAction(Arrays.asList(asName
						+ "1", asName + "2"));
		action.execute();
	}

	private void thenBothASesAndTheirInitConfsAreRemovedFromAir() {
		verify(air, times(2)).deleteAtomicService(startsWith(asName), eq(true));
	}

	@Test
	public void shouldThrow404WhileRemovingNonExistingAs() throws Exception {
		givenAirWithoutAs();
		try {
			whenRemovingAtomicService();
			fail();
		} catch (AtomicServiceNotFoundException e) {
			// OK should be thrown.
		}
	}

	private void givenAirWithoutAs() {
		doThrow(getAirException(404)).when(air).deleteAtomicService(asName,
				true);
	}

	@Test
	public void shouldThrow406WhenRemovingASWhichIsInUse() throws Exception {
		givenAirWitUsedAs();
		try {
			whenRemovingAtomicService();
			fail();
		} catch (NotAcceptableException e) {
			// OK should be thrown.
		}
	}

	private void givenAirWitUsedAs() {
		doThrow(getAirException(400)).when(air).deleteAtomicService(asName,
				true);
	}
}
