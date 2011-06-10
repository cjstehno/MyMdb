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

import com.stehno.mymdb.MovieTestFixture
import com.stehno.mymdb.transfer.Exporter
import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test
import org.gmock.WithGMock

@WithGMock
class ExportServiceTests extends GrailsUnitTestCase {

    private MovieTestFixture fixture = new MovieTestFixture()
    ExportService exportService
    MockExporter exporter
    OutputStream outputStream

    @Before
    void before(){
        fixture.before()

        outputStream = mock(OutputStream)
        exporter = new MockExporter()
        
        exportService.exporter = exporter
    }

    @Test
    void genres(){
        exportService.genres( outputStream )

        assertEquals 3, exporter.size()
        assertEvent 'int', 1, 0
        assertEvent 'long', fixture.genreId, 1
        assertEvent 'utf', 'Action', 2
    }

    @Test
    void actors(){
        exportService.actors( outputStream )

        assertEquals 9, exporter.size()
        assertEvent 'int', 2, 0

        assertEvent 'long', fixture.fixtureIds.actors['Liam Neason'], 1
        assertEvent 'utf', 'Liam', 2
        assertEvent 'utf', null, 3
        assertEvent 'utf', 'Neason', 4

        assertEvent 'long', fixture.fixtureIds.actors['Michael J Fox'], 5
        assertEvent 'utf', 'Michael', 6
        assertEvent 'utf', 'J', 7
        assertEvent 'utf', 'Fox', 8
    }

    @Test
    void storageUnits(){
        exportService.storageUnits( outputStream )

        assertEquals 9, exporter.size()
        assertEvent 'int', 1, 0

        assertEvent 'long', fixture.fixtureIds.storageUnits['X'], 1
        assertEvent 'utf', 'X', 2
        assertEvent 'boolean', false, 3
        assertEvent 'int', 10, 4

        assertEvent 'int', 1, 5

        assertEvent 'int', 1, 6
        assertEvent 'int', 1, 7
        assertEvent 'long', fixture.fixtureIds.movies['A-Team: Unrated'], 8
    }

    private void assertEvent( String expectedType, expectedData, eventIndex=-1 ){
        assertEquals expectedType, exporter[eventIndex].type
        assertEquals expectedData, exporter[eventIndex].data
    }
}

class MockExporter implements Exporter<OutputStream> {

    def events = []

    void writeByte( OutputStream out, byte byt ){
        events << [ type:'byte', data:byt ]
    }

    void writeUTF( OutputStream out, String str ){
        events << [ type:'utf', data:str ]
    }

    void writeInt( OutputStream out, Integer i ){
        events << [ type:'int', data:i ]
    }

    void writeLong( OutputStream out, Long i ){
        events << [ type:'long', data:i ]
    }

    void writeBoolean( OutputStream out, boolean bool){
        events << [ type:'boolean', data:bool ]
    }

    void writeBytes( OutputStream out, byte[] data){
        events << [ type:'bytes', data:data ]
    }

    def getAt( int index ){
        events[index]
    }

    int size(){
        events.size()
    }
}