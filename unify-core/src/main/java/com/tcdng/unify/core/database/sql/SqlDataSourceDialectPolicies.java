/*
 * Copyright 2018-2020 The Code Department.
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

import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.criterion.RestrictionType;

/**
 * SQl data source dialect policies.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SqlDataSourceDialectPolicies {

    Map<ColumnType, SqlDataTypePolicy> getSqlDataTypePolicies();

    Map<RestrictionType, SqlCriteriaPolicy> getSqlCriteriaPolicies();

    SqlDataTypePolicy getSqlTypePolicy(ColumnType columnType);

    SqlCriteriaPolicy getSqlCriteriaPolicy(RestrictionType restrictionType);

    String translateValue(Object param) throws UnifyException;

    int getMaxClauseValues();

    String generateLikeParameter(SqlLikeType type, Object param) throws UnifyException;
}