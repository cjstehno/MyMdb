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
import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import com.stehno.mymdb.domain.*
import static org.junit.Assume.assumeTrue

class ImportExportServiceTests extends GrailsUnitTestCase {

    ExportService exportService
    ImportService importService

    private MovieTestFixture fixture = new MovieTestFixture()

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    void before(){
        fixture.before()
    }
    
    @Test
    void execute(){
        def datfile = folder.newFile('testexport.dat')
        
        exportService.exportCollection new FileOutputStream(datfile)

        importService.clearDatabase()

        assertDatabaseEmpty()

        assertTrue datfile.exists()

        // TODO: this is an assume because this test only passes when run by itself (not -integration or all)
        assumeTrue datfile.size() > 0

        importService.importCollection new FileInputStream(datfile)

        assertEquals 1, Genre.count()
        assertEquals 2, Actor.count()
        assertEquals 1, StorageUnit.count()
        assertEquals 1, Storage.count()
        assertEquals 1, Poster.count()
        assertEquals 1, WebSite.count()
        assertEquals 2, Movie.count()

        def movie = Movie.findByTitle('A-Team: Unrated')
        assertEquals 2010, movie.releaseYear
        assertEquals 'They were acused of a crime they didnt commit', movie.description
        assertEquals MpaaRating.UNRATED, movie.mpaaRating
        assertEquals Format.BLURAY, movie.format
        assertEquals Broadcast.MOVIE, movie.broadcast

        assertEquals 'A-Team', movie.poster.title
        assertEquals 'fakedata'.getBytes(), movie.poster.content

        assertEquals 1, movie.genres.size()
        assertEquals 1, movie.sites.size()
        assertEquals 1, movie.actors.size()

        def unit = StorageUnit.findByName('X')
        assertFalse unit.indexed
        assertEquals 10, unit.capacity
        assertEquals 1, unit.slots.size()
        unit.slots.each { s->
            assertEquals 1, s.index
            assertEquals 1, s.movies.size()
        }
    }

    private void assertDatabaseEmpty(){
        assertEquals 0, Genre.count()
        assertEquals 0, Actor.count()
        assertEquals 0, StorageUnit.count()
        assertEquals 0, Storage.count()
        assertEquals 0, Poster.count()
        assertEquals 0, WebSite.count()
        assertEquals 0, Movie.count()
    }
}
