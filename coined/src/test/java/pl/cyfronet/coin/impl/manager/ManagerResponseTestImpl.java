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

package pl.cyfronet.coin.impl.manager;

import java.util.Map;

import pl.cyfronet.dyrealla.allocation.ManagerResponse;
import pl.cyfronet.dyrealla.allocation.OperationStatus;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 *
 */
public class ManagerResponseTestImpl implements ManagerResponse {

	private OperationStatus status;

	public ManagerResponseTestImpl(OperationStatus status) {
		this.status = status;
	}
 	
	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#addError(java.lang.String, java.lang.String)
	 */
	@Override
	public String addError(String arg0, String arg1) {
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#addWarning(java.lang.String, java.lang.String)
	 */
	@Override
	public String addWarning(String arg0, String arg1) {
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#getErrors()
	 */
	@Override
	public Map<String, String> getErrors() {
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#getOperationStatus()
	 */
	@Override
	public OperationStatus getOperationStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#getWarnings()
	 */
	@Override
	public Map<String, String> getWarnings() {
		return null;
	}

	/* (non-Javadoc)
	 * @see pl.cyfronet.dyrealla.allocation.ManagerResponse#setOperationStatus(pl.cyfronet.dyrealla.allocation.OperationStatus)
	 */
	@Override
	public void setOperationStatus(OperationStatus arg0) {
	}
}
