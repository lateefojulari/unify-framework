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
package com.tcdng.unify.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * NameUtils test.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class NameUtilsTest {

	@Test
	public void testDescribeName() throws Exception {
		assertNull(NameUtils.describeName(null));
		assertEquals("", NameUtils.describeName(""));
		assertEquals("Run On Time", NameUtils.describeName("runOnTime"));
		assertEquals("The Sky Is Blue", NameUtils.describeName("The sky is blue"));
	}

	@Test
	public void testDescribeNestedName() throws Exception {
		assertEquals("Run On Time", NameUtils.describeName("run.OnTime"));
		assertEquals("User Bio Age", NameUtils.describeName("userBio.age"));
	}

	@Test
	public void testDescribeCode() throws Exception {
		assertNull(NameUtils.describeCode(null));
		assertEquals("", NameUtils.describeCode(""));
		assertEquals("Account Number", NameUtils.describeCode("ACCOUNT_NUMBER"));
		assertEquals("Loan Id", NameUtils.describeCode("_LOAN_ID"));
	}
}
