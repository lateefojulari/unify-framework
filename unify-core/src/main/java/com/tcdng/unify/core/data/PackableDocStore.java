/*
 * Copyright 2014 The Code Department
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
package com.tcdng.unify.core.data;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.format.Formatter;

/**
 * Packable document value store.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class PackableDocStore extends AbstractValueStore<PackableDoc> {

	public PackableDocStore(PackableDoc packableDoc) {
		this(packableDoc, -1);
	}

	public PackableDocStore(PackableDoc packableDoc, int dataIndex) {
		super(packableDoc, dataIndex);
	}

	@Override
	public boolean isGettable(String name) throws UnifyException {
		return storage != null && storage.isField(name);
	}

	@Override
	public boolean isSettable(String name) throws UnifyException {
		return storage != null && storage.isField(name);
	}

	@Override
	protected Object doRetrieve(String property) throws UnifyException {
		return storage.readFieldValue(property);
	}

	@Override
	protected void doStore(String property, Object value, Formatter<?> formatter) throws UnifyException {
		storage.writeFieldValue(property, value, formatter);
	}

}
