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
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.AbstractContainer;
import com.tcdng.unify.web.ui.Panel;
import com.tcdng.unify.web.ui.PanelEventListener;

/**
 * Abstract base for menu panels.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractMenuPanel extends AbstractContainer implements Panel {

	public AbstractMenuPanel() {
		super(false);
	}

	@Override
	@Action
	public void switchState() throws UnifyException {

	}

	@Override
	public void resetState() throws UnifyException {

	}

	@Override
	public void addEventListener(PanelEventListener listener) {

	}

	@Override
	public void removeEventListener(PanelEventListener listener) {

	}

}
