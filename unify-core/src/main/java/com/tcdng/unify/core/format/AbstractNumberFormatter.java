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
package com.tcdng.unify.core.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;

/**
 * Abstract base class for a number formatter.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "precision", type = int.class), @UplAttribute(name = "scale", type = int.class),
		@UplAttribute(name = "useGrouping", type = boolean.class) })
public abstract class AbstractNumberFormatter<T extends Number> extends AbstractFormatter<T>
		implements NumberFormatter<T> {

	private NumberType type;

	private NumberFormat nf;

	private NumberSymbols numberSymbols;

	private int precision;

	private int scale;

	private boolean groupingUsed;

	private String pattern;

	public AbstractNumberFormatter(Class<T> dataType, NumberType type) {
		super(dataType);
		this.type = type;
	}

	@Override
	public String format(T value) throws UnifyException {
		return this.getNumberFormat().format(value);
	}

	@Override
	public String getPattern() throws UnifyException {
		this.getNumberFormat();
		return this.pattern;
	}

	@Override
	public int getPrecision() throws UnifyException {
		if (precision > 0) {
			return precision;
		}
		return this.getUplAttribute(int.class, "precision");
	}

	@Override
	public void setPrecision(int precision) {
		this.precision = precision;
		this.nf = null;
	}

	@Override
	public int getScale() throws UnifyException {
		if (scale > 0) {
			return scale;
		}
		return this.getUplAttribute(int.class, "scale");
	}

	@Override
	public void setScale(int scale) {
		this.scale = scale;
		this.nf = null;
	}

	@Override
	public boolean isGroupingUsed() throws UnifyException {
		if (groupingUsed) {
			return groupingUsed;
		}
		return this.getUplAttribute(boolean.class, "useGrouping");
	}

	@Override
	public void setGroupingUsed(boolean groupingUsed) {
		this.groupingUsed = groupingUsed;
		this.nf = null;
	}

	@Override
	public NumberSymbols getNumberSymbols() throws UnifyException {
		this.getNumberFormat();
		return this.numberSymbols;
	}

	protected NumberFormat getNumberFormat() throws UnifyException {
		if (nf == null) {
			this.pattern = null;
			Locale locale = this.getLocale();
			this.numberSymbols = this.getFormatHelper().getNumberSymbols(type, locale);

			switch (this.type) {
			case INTEGER:
				nf = NumberFormat.getIntegerInstance(locale);
				break;
			case PERCENT:
				nf = NumberFormat.getPercentInstance(locale);
				break;
			case DECIMAL:
			default:
				nf = NumberFormat.getNumberInstance(locale);
				break;
			}

			DecimalFormat df = (DecimalFormat) nf;
			nf.setGroupingUsed(this.isGroupingUsed());
			if (NumberType.INTEGER.equals(type)) {
				df.setParseBigDecimal(false);
				if (this.getPrecision() > 0) {
					nf.setMaximumIntegerDigits(getPrecision());
				}
			} else {
				df.setParseBigDecimal(true);
				int precision = this.getPrecision();
				int scale = this.getScale();
				if (precision > 0) {
					if (scale > 0) {
						precision -= scale;
					}
					if (precision > 0) {
						df.setMaximumIntegerDigits(precision);
					}
				}
				if (scale > 0) {
					df.setMaximumFractionDigits(scale);
					df.setMinimumFractionDigits(scale);
				}
			}

			this.pattern = df.toPattern();
		}
		return nf;
	}
}
