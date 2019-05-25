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
package com.tcdng.unify.core.database.sql.policy;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javax.sql.rowset.serial.SerialBlob;

import com.tcdng.unify.core.database.sql.AbstractSqlDataTypePolicy;

/**
 * BLOB data type SQL policy.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class BlobPolicy extends AbstractSqlDataTypePolicy {

    @Override
    public void appendTypeSql(StringBuilder sb, int length, int precision, int scale) {
        sb.append(" BLOB");
    }

    @Override
    public void appendDefaultSql(StringBuilder sb, Class<?> type, String defaultVal) {

    }

    @Override
    public void executeSetPreparedStatement(Object pstmt, int index, Object data, long utcOffset) throws Exception {
        if (data == null) {
            ((PreparedStatement) pstmt).setNull(index, Types.BLOB);
        } else {
            ((PreparedStatement) pstmt).setBlob(index, new SerialBlob((byte[]) data));
        }
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, String column, long utcOffset) throws Exception {
        Blob blob = ((ResultSet) rs).getBlob(column);
        if (blob != null) {
            return blob.getBytes(1, (int) blob.length());
        }
        return null;
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, int index, long utcOffset) throws Exception {
        Blob blob = ((ResultSet) rs).getBlob(index);
        if (blob != null) {
            return blob.getBytes(1, (int) blob.length());
        }
        return null;
    }

    @Override
    public String getAltDefault() {
        return null;
    }

    @Override
    public int getSqlType() {
        return Types.BLOB;
    }

    @Override
    public boolean isFixedLength() {
        return true;
    }

}
