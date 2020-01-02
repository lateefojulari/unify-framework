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
package com.tcdng.unify.core.convert;

import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.format.Formatter;

/**
 * Byte array converter.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ByteArrayConverter extends AbstractConverter<byte[]> {

    @Override
    protected byte[] doConvert(Object value, Formatter<?> formatter) throws Exception {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        if (value instanceof UploadedFile) {
            return ((UploadedFile) value).getData();
        }

        if (value instanceof UploadedFile[]) {
            return ((UploadedFile[]) value)[0].getData();
        }
        return null;
    }

}
