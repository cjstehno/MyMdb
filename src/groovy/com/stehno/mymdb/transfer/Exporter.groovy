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
public interface Exporter<T extends OutputStream> {

    void writeByte( T out, byte byt )

    void writeUTF( T out, String str )

    void writeInt( T out, Integer i )

    void writeLong( T out, Long i )

    void writeBoolean( T out, boolean bool)

    void writeBytes( T out, byte[] data)
}