/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.mymdb.transfer

/**
 * 
 *
 * @author cjstehno
 */
class BinaryImporter implements Importer<DataInputStream> {

    Integer readInt(DataInputStream ins, Integer defaultValue=0) {
        ins.readInt() ?: defaultValue
    }

    Long readLong(DataInputStream ins, Long defaultValue=0) {
        ins.readLong() ?: defaultValue
    }

    byte[] readBytes( DataInputStream ins, int size ){
        def content = new byte[size]
        ins.readFully(content)
        content
    }

    byte readByte(DataInputStream ins) {
        ins.readByte()
    }

    String readUTF(DataInputStream ins) {
        ins.readUTF()
    }

    boolean readBoolean(DataInputStream ins) {
        ins.readBoolean()
    }
}
