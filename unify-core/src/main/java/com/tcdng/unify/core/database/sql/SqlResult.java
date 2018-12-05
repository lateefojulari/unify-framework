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
package com.tcdng.unify.core.database.sql;

import java.lang.reflect.Method;

import com.tcdng.unify.core.transform.Transformer;

/**
 * SQL result object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class SqlResult {

	private SqlDataTypePolicy sqlDataTypePolicy;

	private SqlFieldInfo sqlFieldInfo;

	public SqlResult(SqlDataTypePolicy sqlDataTypePolicy, SqlFieldInfo sqlFieldInfo) {
		this.sqlDataTypePolicy = sqlDataTypePolicy;
		this.sqlFieldInfo = sqlFieldInfo;
	}

	public SqlDataTypePolicy getSqlDataTypePolicy() {
		return sqlDataTypePolicy;
	}

	public Class<?> getType() {
		return sqlFieldInfo.getFieldClass();
	}

	public String getColumn() {
		return sqlFieldInfo.getColumn();
	}

	public String getName() {
		return sqlFieldInfo.getName();
	}

	public Transformer<?, ?> getTransformer() {
		return sqlFieldInfo.getTransformer();
	}

	public boolean isTransformed() {
		return sqlFieldInfo.isTransformed();
	}

	public Method getGetter() {
		return sqlFieldInfo.getGetter();
	}

	public Method getSetter() {
		return sqlFieldInfo.getSetter();
	}
}
