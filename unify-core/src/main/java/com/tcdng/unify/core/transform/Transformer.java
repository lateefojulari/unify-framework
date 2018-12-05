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
package com.tcdng.unify.core.transform;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Used for transforming between two types.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface Transformer<U, V> extends UnifyComponent {
	/**
	 * Forward transform a value.
	 * 
	 * @param value
	 *            the value to transform
	 * @return the transformed value
	 * @throws UnifyException
	 *             if an error occurs
	 */
	V forwardTransform(U value) throws UnifyException;

	/**
	 * Reverse transform a value.
	 * 
	 * @param value
	 *            the value to reverse transform
	 * @return the reversed value
	 * @throws UnifyException
	 *             - If an error occurs
	 */
	U reverseTransform(V value) throws UnifyException;
}
