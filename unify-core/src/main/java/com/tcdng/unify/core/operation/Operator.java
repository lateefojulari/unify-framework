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
package com.tcdng.unify.core.operation;

/**
 * Operator enumeration.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public enum Operator {
	EQUAL(ParameterType.SINGLE), NOT_EQUAL(ParameterType.SINGLE), LESS_THAN(ParameterType.SINGLE), LESS_OR_EQUAL(
			ParameterType.SINGLE), GREATER(ParameterType.SINGLE), GREATER_OR_EQUAL(ParameterType.SINGLE), BETWEEN(
					ParameterType.BINARY), NOT_BETWEEN(ParameterType.BINARY), AMONGST(
							ParameterType.MULTIPLE), NOT_AMONGST(ParameterType.MULTIPLE), LIKE(
									ParameterType.SINGLE), NOT_LIKE(ParameterType.SINGLE), LIKE_BEGIN(
											ParameterType.SINGLE), NOT_LIKE_BEGIN(ParameterType.SINGLE), LIKE_END(
													ParameterType.SINGLE), NOT_LIKE_END(ParameterType.SINGLE), IS_NULL(
															ParameterType.VOID), IS_NOT_NULL(ParameterType.VOID), AND(
																	ParameterType.BINARY), OR(ParameterType.BINARY);

	private final ParameterType paramType;

	private Operator(ParameterType paramType) {
		this.paramType = paramType;
	}

	public ParameterType paramType() {
		return paramType;
	}

}
