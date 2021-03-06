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

package com.tcdng.unify.core.criterion;

import java.util.Set;

/**
 * Convenient abstract base class for simple restrictions.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractSimpleRestriction extends AbstractRestriction implements SimpleRestriction {

    private String fieldName;

    public AbstractSimpleRestriction(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void writeRestrictedFields(Set<String> restrictedFields) {
        restrictedFields.add(fieldName);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean isRestrictedField(String fieldName) {
        return this.fieldName.equals(fieldName);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return true;
    }
}
