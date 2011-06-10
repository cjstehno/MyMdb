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

package com.stehno.mymdb.controller.movie

import com.stehno.mymdb.service.MovieFlowService
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.dto.*
import com.stehno.mymdb.service.StorageUnitService
import com.stehno.mymdb.MovieTestFixture

class MovieSummaryControllerTests extends MovieFlowIntegrationTestBase {

    StorageUnitService storageUnitService

    @Before
    void before(){
        super.setUp()

        controller = new MovieSummaryController()
        controller.movieFlowService = new MovieFlowService( storageUnitService:storageUnitService )
    }

    @Test
    void show_no_data(){
        request('GET', '/movie/summary')

        def model = controller.show()

        assertNotNull model
        assertNull model.title
        assertNull model.releaseYear
        assertNull model.storage
        assertNull model.description
        assertNull model.genres
        assertNull model.actors
    }

    @Test
    void save_new(){
        def horror = genre( 'Horror' )
        def johnDoe = actor( 'John', 'Q', 'Doe' )

        def storageUnit = new StorageUnit( name:'X', indexed:false, capacity:3 )
        storageUnit.save(flush:true)

        controller.movieFlowService.start()

        def details = controller.movieFlowService.retrieve(DetailsDto.class)
        details.title = 'Save Testing'
        details.releaseYear = 2011
        details.storageId = storageUnit.id as String
        details.description = 'This is a test of movie saving.'
        controller.movieFlowService.store(details)

        def poster = controller.movieFlowService.retrieve(PosterDto.class)
        poster.posterType = PosterType.FILE
        poster.file = 'thisisposterdata'.getBytes()
        controller.movieFlowService.store(poster)

        def genre = controller.movieFlowService.retrieve(GenreDto.class)
        genre.genres = [horror.id as long]
        controller.movieFlowService.store(genre)

        def actor = controller.movieFlowService.retrieve(ActorDto.class)
        actor.actors = [johnDoe.id as long]
        controller.movieFlowService.store(actor)

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def moviePoster = Poster.list()[0]
        assertNotNull moviePoster
        assertEquals 'Save Testing', moviePoster.title
        assertTrue moviePoster.content.size() > 0

        def movie = Movie.findByTitle('Save Testing')
        assertNotNull movie
        assertEquals 2011, movie.releaseYear
        assertEquals 'This is a test of movie saving.', movie.description
        assertEquals 1, movie.genres.size()
        assertEquals 1, movie.actors.size()
        assertEquals moviePoster.id, movie.poster.id
    }

    @Test
    void save_update(){
        def fixture = new MovieTestFixture()
        fixture.clearDatabase()
        fixture.before()

        def horror = fixture.buildGenres('Horror')[0]
        def johnDoe = fixture.buildActors('John Q Doe')[0]

        storageUnitService.storeMovie( fixture.storageUnitId, fixture.movieId )

        assertEquals 2, Movie.list().size()

        controller.movieFlowService.start(fixture.movieId)

        def details = controller.movieFlowService.retrieve(DetailsDto.class)
        details.title = 'Save Testing'
        details.releaseYear = 2011
        details.storageId = "${fixture.storageUnitId}"
        details.description = 'This is a test of movie saving.'
        controller.movieFlowService.store(details)

        def poster = controller.movieFlowService.retrieve(PosterDto.class)
        poster.posterType = PosterType.FILE
        poster.file = 'thisisposterdata'.getBytes()
        controller.movieFlowService.store(poster)

        def genre = controller.movieFlowService.retrieve(GenreDto.class)
        genre.genres = [horror.id as long]
        controller.movieFlowService.store(genre)

        def actor = controller.movieFlowService.retrieve(ActorDto.class)
        actor.actors = [johnDoe.id as long]
        controller.movieFlowService.store(actor)

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        assertEquals 2, Movie.list().size()
        assertEquals 2, Poster.list().size()

        def moviePoster = Poster.list()[1]
        assertNotNull moviePoster
        assertEquals 'Save Testing', moviePoster.title
        assertTrue moviePoster.content.size() > 0

        def movie = Movie.findByTitle('Save Testing')
        assertNotNull movie
        assertEquals 2011, movie.releaseYear
        assertEquals 'This is a test of movie saving.', movie.description
//        assertEquals 'X', movie.storage.storageUnit.name
        assertEquals 1, movie.genres.size()
        assertEquals 1, movie.actors.size()
        assertEquals moviePoster.id, movie.poster.id
    }

    @After
    void after(){
        super.tearDown()
    }

    private def genre( name ){
        def g = new Genre( name:name )
        g.save(flush:true)
        g
    }

    private def actor( fname, mname, lname){
        def a = new Actor( firstName:fname, middleName:mname, lastName:lname)
        a.save(flush:true)
        a
    }
}
