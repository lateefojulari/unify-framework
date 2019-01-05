/*
 * Copyright 2018 The Code Department
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

/**
 * Interface with policy methods for an SQL data type.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SqlDataTypePolicy {

    /**
     * Gets the java SQL data type.
     * 
     * @return the java SQL type value
     */
    int getSqlType();

    /**
     * Checks if policy type is for fixed length type.
     * 
     * @return a true value if for fixed length otherwise false
     */
    boolean isFixedLength();

    /**
     * Appends a type SQL to string buffer.
     * 
     * @param sb
     *            the string buffer
     * @param length
     *            the data length
     * @param precision
     *            the number precision
     * @param scale
     *            the number scale
     */
    void appendTypeSql(StringBuilder sb, int length, int precision, int scale);

    /**
     * Appends default value SQL
     * 
     * @param sb
     *            the builder to append to
     * @param type
     *            the column type
     * @param defaultVal
     *            the default value
     */
    void appendSpecifyDefaultValueSql(StringBuilder sb, Class<?> type, String defaultVal);

    /**
     * Executes the setter of a prepared statement.
     * 
     * @param pstmt
     *            the prepared statement
     * @param index
     *            the data index
     * @param data
     *            the data to set
     * @throws Exception
     *             if an error occurs
     */
    void executeSetPreparedStatement(Object pstmt, int index, Object data) throws Exception;

    /**
     * Executes resultset getter using supplied column name.
     * 
     * @param rs
     *            the result set
     * @param type
     *            the result type
     * @param column
     *            tthe result column
     * @return the result value
     * @throws Exception
     *             if an error occurs
     */
    Object executeGetResult(Object rs, Class<?> type, String column) throws Exception;

    /**
     * Executes resultset getter using supplied column index.
     * 
     * @param rs
     *            the result set
     * @param type
     *            the result type
     * @param index
     *            the result column index
     * @return the result value
     * @throws Exception
     *             if an error occurs
     */
    Object executeGetResult(Object rs, Class<?> type, int index) throws Exception;
}
