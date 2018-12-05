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
package com.tcdng.unify.web.ui.panel;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.AbstractPanel;
import com.tcdng.unify.web.ui.Widget;

/**
 * Switch panels display a single widget content at any time allowing an actor
 * to switch between different contents.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-switchpanel")
public class SwitchPanel extends AbstractPanel {

	private String currentComponent;

	public void switchContent(String shortName) throws UnifyException {
		currentComponent = shortName;
	}

	public Widget getCurrentWidget() throws UnifyException {
		if (currentComponent != null) {
			return getWidgetByShortName(currentComponent);
		}
		return null;
	}

	@Override
	public void onPageInitialize() throws UnifyException {
		super.onPageInitialize();

		for (String longName : getLayoutWidgetLongNames()) {
			Widget widget = getWidgetByLongName(longName);
			if (!widget.isHidden()) {
				currentComponent = widget.getShortName();
				break;
			}
		}
	}
}
