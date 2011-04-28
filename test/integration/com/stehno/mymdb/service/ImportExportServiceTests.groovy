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
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.domain.StorageUnit

/**
 * 
 *
 * @author cjstehno
 */
class ImportExportServiceTests extends GrailsUnitTestCase {

    ExportService exportService
    ImportService importService

    private MovieTestFixture fixture

    @Before
    void before(){
        this.fixture = new MovieTestFixture()
        this.fixture.before()
    }

    @Test
    void execute(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream()
        
        exportService.exportCollection stream

        fixture.clean()

        importService.importCollection( new ByteArrayInputStream(stream.toByteArray()) )

        assertEquals 1, Genre.count()
        assertEquals 2, Actor.count()
        assertEquals 2, StorageUnit.count()
    }
}
