/*
 * Copyright 2018-2019 The Code Department.
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
package com.tcdng.unify.core.system.entities;

import com.tcdng.unify.core.database.Query;

/**
 * Parameter values record query.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ParameterValuesQuery extends Query<ParameterValues> {

    public ParameterValuesQuery() {
        super(ParameterValues.class);
    }

    public ParameterValuesQuery typeName(String typeName) {
        return (ParameterValuesQuery) addEquals("typeName", typeName);
    }

    public ParameterValuesQuery instTypeName(String instTypeName) {
        return (ParameterValuesQuery) addEquals("instTypeName", instTypeName);
    }

    public ParameterValuesQuery instId(Long instId) {
        return (ParameterValuesQuery) addEquals("instId", instId);
    }

}
