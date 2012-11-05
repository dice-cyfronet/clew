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

package pl.cyfronet.coin.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;

import pl.cyfronet.coin.impl.action.Action;
import pl.cyfronet.coin.impl.action.ActionFactory;
import pl.cyfronet.coin.impl.security.mi.MasterInterfaceAuthenticationHandler;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public abstract class AbstractServiceTest extends
		AbstractTestNGSpringContextTests {

	@Autowired
	protected ActionFactory actionFactory;

	@Autowired
	private MasterInterfaceAuthenticationHandler authenticationHandler;

	protected Action<? extends Object> currentAction;

	@BeforeMethod
	protected void setUp() {
		when(authenticationHandler.getUsername("User123", "notimportant"))
				.thenReturn("User123");
	}

	protected void thenActionExecuted() {
		verify(currentAction, times(1)).execute();
	}
}
