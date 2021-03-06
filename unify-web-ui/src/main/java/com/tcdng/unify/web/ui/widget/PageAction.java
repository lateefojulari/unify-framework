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
package com.tcdng.unify.web.ui.widget;

/**
 * Represents a page action behavior component.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface PageAction extends Behavior {

    /**
     * Returns the page action code.
     */
    String getAction();

    /**
     * Returns the page action id.
     */
    String getId();

    /**
     * Sets the page action id.
     * 
     * @param id
     *            the id to set
     */
    void setId(String id);
}
