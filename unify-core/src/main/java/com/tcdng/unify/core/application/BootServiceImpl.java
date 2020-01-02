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
package com.tcdng.unify.core.application;

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default implementation of an boot business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(ApplicationComponents.APPLICATION_DEFAULTBOOTSERVICE)
public class BootServiceImpl extends AbstractBootService<FeatureDefinition> {

    @Override
    protected List<StartupShutdownHook> getStartupShutdownHooks() throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    protected BootInstallationInfo<FeatureDefinition> prepareBootInstallation() throws UnifyException {
        return new BootInstallationInfo<FeatureDefinition>();
    }

    @Override
    protected void onStartup() throws UnifyException {

    }

    @Override
    protected void onShutdown() throws UnifyException {

    }
}
