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

package pl.cyfronet.coin.api.beans;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
@XmlRootElement
public class NewAtomicService extends AtomicServiceRequest {

	private String sourceAsiId;

	public String getSourceAsiId() {
		return sourceAsiId;
	}

	public void setSourceAsiId(String sourceAsiId) {
		this.sourceAsiId = sourceAsiId;
	}

	@Override
	public String toString() {
		return "NewAtomicService [sourceAsiId=" + sourceAsiId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((sourceAsiId == null) ? 0 : sourceAsiId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewAtomicService other = (NewAtomicService) obj;
		if (sourceAsiId == null) {
			if (other.sourceAsiId != null)
				return false;
		} else if (!sourceAsiId.equals(other.sourceAsiId))
			return false;
		return true;
	}
}
