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
import java.util.regex.Matcher;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.regex.RegexPatternStore;
import com.tcdng.unify.web.DataTransfer;
import com.tcdng.unify.web.DataTransferBlock;

/**
 * Abstract regex page validation.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractRegexPageValidation extends AbstractPageValidation {

	private String validationCode;

	private String regexKey;

	private String errorKey;

	public AbstractRegexPageValidation(String validationCode, String regexKey, String errorKey) {
		this.validationCode = validationCode;
		this.regexKey = regexKey;
		this.errorKey = errorKey;
	}

	@Override
	public boolean validate(List<Widget> widgets, DataTransfer dataTransfer) throws UnifyException {
		boolean pass = true;
		for (Widget widget : widgets) {
			if (widget.isVisible()) {
				boolean localPass = true;
				DataTransferBlock dataTransferBlock = dataTransfer.getDataTransferBlock(widget.getId());
				if (dataTransferBlock != null) {
					String value = this.getTransferValue(String.class, dataTransferBlock);
					if (value != null) {
						Matcher matcher = ((RegexPatternStore) this
								.getComponent(ApplicationComponents.APPLICATION_REGEXPATTERNSTORE))
										.getPattern(this.getSessionLocale(), this.regexKey).matcher(value);
						if (!matcher.matches()) {
							String caption = widget.getUplAttribute(String.class, "caption");
							String message = this.getSessionMessage(this.errorKey, caption);
							this.addValidationFail((Control) widget, this.validationCode, message);
							pass = localPass = false;
						}
					}
				}

				if (localPass) {
					this.addValidationPass((Control) widget, this.validationCode);
				}
			}
		}

		return pass;
	}
}
