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

package com.tcdng.unify.core.database.dynamic;

import com.tcdng.unify.core.annotation.DynamicFieldType;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.EntityFieldType;

/**
 * Dynamic field information.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class DynamicFieldInfo {

    private DynamicFieldType type;

    private EntityFieldType fieldType;

    private DataType dataType;

    private String columnName;

    private String fieldName;

    private String enumClassName;

    private boolean descriptive;
    
    public DynamicFieldInfo(DynamicFieldType type, EntityFieldType fieldType, DataType dataType, String columnName,
            String fieldName, boolean descriptive) {
        this(type, fieldType, dataType, columnName, fieldName, null, descriptive);
    }

    public DynamicFieldInfo(DynamicFieldType type, EntityFieldType fieldType, DataType dataType, String columnName,
            String fieldName, String enumClassName, boolean descriptive) {
        this.type = type;
        this.fieldType = fieldType;
        this.dataType = dataType;
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.enumClassName = enumClassName;
        this.descriptive = descriptive;
    }

    public DynamicFieldType getType() {
        return type;
    }

    public EntityFieldType getFieldType() {
        return fieldType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getEnumClassName() {
        return enumClassName;
    }

    public boolean isEnum() {
        return enumClassName != null;
    }

    public boolean isDescriptive() {
        return descriptive;
    }

    public boolean isGeneration() {
        return DynamicFieldType.GENERATION.equals(type);
    }
}
