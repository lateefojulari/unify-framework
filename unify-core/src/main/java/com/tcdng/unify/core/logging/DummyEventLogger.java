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
package com.tcdng.unify.core.logging;

import java.util.List;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Dummy event logger.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class DummyEventLogger extends AbstractUnifyComponent implements EventLogger {

    @Override
    public boolean logUserEvent(String eventCode, String... detail) throws UnifyException {
        return false;
    }

    @Override
    public boolean logUserEvent(String eventCode, List<String> details) throws UnifyException {
        return false;
    }

    @Override
    public boolean logUserEvent(EventType eventType, Class<? extends Entity> entityClass) throws UnifyException {
        return false;
    }

    @Override
    public boolean logUserEvent(EventType eventType, Entity record, boolean isNewRecord) throws UnifyException {
        return false;
    }

    @Override
    public <T extends Entity> boolean logUserEvent(EventType eventType, T oldRecord, T newRecord)
            throws UnifyException {
        return false;
    }

    @Override
    public boolean logUserEvent(EventType eventType, Class<? extends Entity> entityClass, Object id,
            List<FieldAudit> fieldAuditList) throws UnifyException {
        return false;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }
}
