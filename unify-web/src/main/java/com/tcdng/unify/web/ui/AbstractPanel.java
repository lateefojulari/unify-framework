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

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.annotation.Action;

/**
 * Serves as the base class for a panel.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "backImageSrc", type = String.class),
		@UplAttribute(name = "refreshPath", type = String.class),
		@UplAttribute(name = "refreshEvery", type = int.class), @UplAttribute(name = "legend", type = String.class),
		@UplAttribute(name = "hideOnNoComponents", type = boolean.class, defaultValue = "true") })
public abstract class AbstractPanel extends AbstractContainer implements Panel {

	private List<PanelEventListener> listeners;

	private boolean allowRefresh;

	public AbstractPanel() {
		this.allowRefresh = true;
	}

	@Override
	public void resetState() throws UnifyException {

	}

	@Override
	@Action
	public void switchState() throws UnifyException {
		this.getRequestContextUtil().setPanelSwitchStateFlag(this);
	}

	@Override
	public boolean isVisible() throws UnifyException {
		if (this.getUplAttribute(boolean.class, "hideOnNoComponents")) {
			if (this.isNoReferencedComponents()) {
				return false;
			}
		}
		return super.isVisible();
	}

	@Override
	public boolean isAllowRefresh() {
		return allowRefresh;
	}

	@Override
	public void addEventListener(PanelEventListener listener) {
		if (this.listeners == null) {
			this.listeners = new ArrayList<PanelEventListener>();
		}

		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void removeEventListener(PanelEventListener listener) {
		if (this.listeners != null) {
			this.listeners.remove(listener);
		}
	}

	@Override
	public String getBackImageSrc() throws UnifyException {
		return getUplAttribute(String.class, "backImageSrc");
	}

	@Override
	public String getRefreshPath() throws UnifyException {
		return getUplAttribute(String.class, "refreshPath");
	}

	@Override
	public int getRefreshEvery() throws UnifyException {
		return getUplAttribute(int.class, "refreshEvery");
	}

	@Override
	public String getLegend() throws UnifyException {
		return getUplAttribute(String.class, "legend");
	}

	/**
	 * Sets this panel's refresh flag. If true panel would be automatically
	 * refreshed at a frequency based on 'refreshEvery' attribute.
	 * 
	 * @param allowRefresh
	 *            the refresh flag to set
	 */
	protected void setAllowRefresh(boolean allowRefresh) {
		this.allowRefresh = allowRefresh;
	}

	/**
	 * Notifies all listeners of event.
	 * 
	 * @param eventCode
	 *            the event code
	 */
	protected void notifyListeners(String eventCode) throws UnifyException {
		if (this.listeners != null) {
			for (PanelEventListener listener : this.listeners) {
				listener.notify(this, eventCode);
			}
		}
	}
}
