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
class BinaryExporter implements Exporter<DataOutputStream> {

    void writeByte( DataOutputStream out, byte byt ){
        out.writeByte byt
    }

    void writeUTF( DataOutputStream out, String str ){
        out.writeUTF( str ?: '' )
    }

    void writeInt( DataOutputStream out, Integer i ){
        out.writeInt( i ?: 0 )
    }

    void writeLong( DataOutputStream out, Long i ){
        out.writeLong( i ?: 0 )
    }

    void writeBoolean( DataOutputStream out, boolean bool){
        out.writeBoolean bool
    }

    void writeBytes( DataOutputStream out, byte[] data){
        out.write( data, 0, data.size() )
    }
}
