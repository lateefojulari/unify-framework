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
package com.tcdng.unify.web.ui;

import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.web.WebApplicationComponents;

/**
 * A convenient base class for controls that make use of a list.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "list", type = String.class, mandatory = true),
		@UplAttribute(name = "listParams", type = String[].class), @UplAttribute(name = "listKey", type = String.class),
		@UplAttribute(name = "listDescription", type = String.class),
		@UplAttribute(name = "listParamType", type = ListParamType.class, defaultValue = "control"),
		@UplAttribute(name = "flow", type = boolean.class) })
public abstract class AbstractListControl extends AbstractControl implements ListControl {

	@Override
	public ListControlJsonData getListControlJsonData(boolean indexes, boolean keys, boolean labels)
			throws UnifyException {
		return this.getListControlUtils().getListControlJsonData(this, indexes, keys, labels);
	}

	@Override
	public List<? extends Listable> getListables() throws UnifyException {
		return this.getListControlUtils().getListables(this);
	}

	@Override
	public Map<String, String> getListMap() throws UnifyException {
		return this.getListControlUtils().getListMap(this);
	}

	@Override
	public String getList() throws UnifyException {
		return this.getUplAttribute(String.class, "list");
	}

	@Override
	public String[] getListParams() throws UnifyException {
		return this.getUplAttribute(String[].class, "listParams");
	}

	@Override
	public ListParamType getListParamType() throws UnifyException {
		return this.getUplAttribute(ListParamType.class, "listParamType");
	}

	@Override
	public String getListKey() throws UnifyException {
		return this.getUplAttribute(String.class, "listKey");
	}

	@Override
	public String getListDescription() throws UnifyException {
		return this.getUplAttribute(String.class, "listDescription");
	}

	private ListControlUtils getListControlUtils() throws UnifyException {
		return (ListControlUtils) this.getComponent(WebApplicationComponents.APPLICATION_LISTCONTROLUTIL);
	}
}
