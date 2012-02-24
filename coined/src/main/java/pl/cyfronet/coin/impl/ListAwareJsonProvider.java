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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.provider.JSONProvider;

/**
 * @author <a href="mailto:mkasztelnik@gmail.com">Marek Kasztelnik</a>
 */
public class ListAwareJsonProvider extends JSONProvider {

	private Map<String, String> collectionWrapperMap;

	/**
	 * @param collectionWrapperMap the collectionWrapperMap to set
	 */
	public void setCollectionWrapperMap(Map<String, String> collectionWrapperMap) {
		this.collectionWrapperMap = collectionWrapperMap;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected String getRootName(Class<Object> cls, Type type) throws Exception {
		if (cls.isAssignableFrom(List.class)
				&& type instanceof ParameterizedType) {
			ParameterizedType pType = (ParameterizedType) type;
			Type[] types = pType.getActualTypeArguments();

			if (types != null && types.length > 0) {
				Type actualType = types[0];
				String typeName = ((Class) actualType).getName();
				String jsonKey = collectionWrapperMap.get(typeName);
				if (jsonKey != null) {
					return "{\"" + jsonKey + "\":";
				}
			}
		}
		return super.getRootName(cls, type);
	}
}
