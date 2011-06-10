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

package com.stehno.mymdb.service

import com.stehno.mymdb.domain.StorageUnit
import com.stehno.mymdb.transfer.BinaryImporter
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

@WithGMock
class ImportServiceTests extends GrailsUnitTestCase {

    ImportService importService
    TracedImporter importer

    @Before
    void before(){
        importer = new TracedImporter()

        importService.importer = importer
    }

    @Test
    void genres(){
        def source = new ByteArrayOutputStream()

        new DataOutputStream(source).withStream { outs->
            outs.writeInt 2

            outs.writeLong 100
            outs.writeUTF 'Horror'

            outs.writeLong 200
            outs.writeUTF 'Drama'
        }

        def context = [:]
        
        new DataInputStream(new ByteArrayInputStream(source.toByteArray())).withStream { ins->
            importService.genres ins, context
        }

        assertEquals 5, importer.size()

        assertEvent 'int', 2, 0

        assertEvent 'long', 100, 1
        assertEvent 'utf', 'Horror', 2

        assertEvent 'long', 200, 3
        assertEvent 'utf', 'Drama', 4
    }

    @Test
    void storageUnits_IndexedLimited(){
        def source = new ByteArrayOutputStream()

        new DataOutputStream(source).withStream { outs->
            outs.writeInt 1

            outs.writeLong 100
            outs.writeUTF 'Y'
            outs.writeBoolean true
            outs.writeInt 10

            outs.writeInt 1

            outs.writeInt 1
            outs.writeInt 1

            outs.writeLong 300
        }

        def context = [:]

        new DataInputStream(new ByteArrayInputStream(source.toByteArray())).withStream { ins->
            importService.storageUnits ins, context
        }

        assertEquals 9, importer.size()

        assertEvent 'int', 1, 0

        assertEvent 'long', 100, 1
        assertEvent 'utf', 'Y', 2
        assertEvent 'boolean', true, 3
        assertEvent 'int', 10, 4

        assertEvent 'int', 1, 5

        assertEvent 'int', 1, 6
        assertEvent 'int', 1, 7

        assertEvent 'long', 300, 8

        def storageUnit = StorageUnit.findByName('Y')
        assertNotNull storageUnit
        assertTrue storageUnit.indexed
        assertEquals 10, storageUnit.capacity

        assertEquals 1, context.movieStorage[300L].index
        assertNotNull context.movieStorage[300L].unit
    }

    @Test
    void storageUnits_UnindexedLimited(){
        def source = new ByteArrayOutputStream()

        new DataOutputStream(source).withStream { outs->
            outs.writeInt 1

            outs.writeLong 100
            outs.writeUTF 'Y'
            outs.writeBoolean false
            outs.writeInt 13

            outs.writeInt 1

            outs.writeInt 1
            outs.writeInt 1

            outs.writeLong 300
        }

        def context = [:]

        new DataInputStream(new ByteArrayInputStream(source.toByteArray())).withStream { ins->
            importService.storageUnits ins, context
        }

        assertEquals 9, importer.size()

        assertEvent 'int', 1, 0

        assertEvent 'long', 100, 1
        assertEvent 'utf', 'Y', 2
        assertEvent 'boolean', false, 3
        assertEvent 'int', 13, 4

        assertEvent 'int', 1, 5

        assertEvent 'int', 1, 6
        assertEvent 'int', 1, 7

        assertEvent 'long', 300, 8

        def storageUnit = StorageUnit.findByName('Y')
        assertNotNull storageUnit
        assertFalse storageUnit.indexed
        assertEquals 13, storageUnit.capacity

        assertEquals 1, context.movieStorage[300L].index
        assertNotNull context.movieStorage[300L].unit
    }

    private void assertEvent( String expectedType, expectedData, eventIndex=-1 ){
        assertEquals expectedType, importer[eventIndex].type
        assertEquals expectedData, importer[eventIndex].data
    }
}

class TracedImporter extends BinaryImporter {

    def events = []

    byte readByte(DataInputStream ins) {
        recordAndReturn 'byte', super.readByte(ins)
    }

    String readUTF(DataInputStream ins) {
        recordAndReturn 'utf', super.readUTF(ins)
    }

    boolean readBoolean(DataInputStream ins) {
        recordAndReturn 'boolean', super.readBoolean(ins)
    }

    Integer readInt(DataInputStream ins, Integer defaultValue=0) {
        recordAndReturn 'int', super.readInt(ins,defaultValue)
    }

    Long readLong(DataInputStream ins, Long defaultValue=0) {
        recordAndReturn 'long', super.readLong(ins,defaultValue)
    }

    byte[] readBytes(DataInputStream ins, int size) {
        recordAndReturn 'bytes', super.readBytes(ins,size)
    }

    def getAt( int index ){
        events[index]
    }

    int size(){
        events.size()
    }

    private recordAndReturn( typ, dat ){
        events << [type:typ, data:dat]
        dat
    }
}
