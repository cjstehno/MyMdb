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
import org.junit.Ignore
import com.stehno.mymdb.MovieTestFixture
import com.stehno.mymdb.service.StorageUnitService

class MovieDetailsControllerTests extends MovieFlowIntegrationTestBase {

    def fixture = new MovieTestFixture()
    StorageUnitService storageUnitService

    @Before
    void before(){
        super.setUp()
        fixture.before()

        controller = new MovieDetailsController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show_new(){
        request('GET', '/movie/details')

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
    }

    @Test
    void show_edit(){
        storageUnitService.storeMovie( fixture.storageUnitId, fixture.movieId )

        controller.params.id = fixture.movieId as String

        request('GET', "/movie/details")

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
        assertEquals 'A-Team: Unrated', jso.data.title
        assertEquals 2010, jso.data.releaseYear
        assertEquals fixture.storageUnitId as String, jso.data.storageId
        assertEquals 'They were acused of a crime they didnt commit', jso.data.description
        assertEquals MpaaRating.UNRATED.name(), jso.data.mpaaRating
        assertEquals Format.BLUERAY.name(), jso.data.format

        assertEquals fixture.movieId, controller.movieFlowService.flow.movieId
        assertNotNull controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull controller.movieFlowService.flow[GenreDto.class.name]
        assertNotNull controller.movieFlowService.flow[ActorDto.class.name]
        assertNotNull controller.movieFlowService.flow[WebSiteDto.class.name]
    }

    @Test
    void save(){
        request('POST','/movie/details')

        controller.params.title = 'A Cool Title'
        controller.params.releaseYear = '2007'
        controller.params.description = 'Some cool movie!'
        controller.params.storageId = "${fixture.storageUnitId}:2"

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull dto
        assertEquals 'A Cool Title', dto.title
        assertEquals 2007, dto.releaseYear
        assertEquals 'Some cool movie!', dto.description
        assertEquals "${fixture.storageUnitId}:2", dto.storageId
    }

    @Test
    void save_with_no_data(){
        request('POST','/movie/details')

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertFalse jso.success

        def errors = jso.errors
        assertEquals 2, errors.size()
        assertEquals 'Property [title] of class [class com.stehno.mymdb.dto.DetailsDto] cannot be null', errors.title
        assertEquals 'Property [storageId] of class [class com.stehno.mymdb.dto.DetailsDto] cannot be null', errors.storageId

        assertNull controller.movieFlowService.flow[DetailsDto.class.name]
    }

    @Test @Ignore("The forward is not handled properly - this should be a functional test")
    void save_with_finish(){
        request('POST','/movie/details')

        controller.params.title = 'A Cool Title'
        controller.params.releaseYear = '2007'
        controller.params.description = 'Some cool movie!'
        controller.params.storageName = 'T'
        controller.params.storageIndex = '23'
        controller.params.finish = true

        controller.save()

        assertEquals '', controller.response.contentAsString

//        def jso = parseJsonResponse()
//        assertNotNull jso
//        assertTrue jso.success

        def movie = Movie.findByTitle('A Cool Title')
        assertNotNull movie
        assertEquals 2007, movie.releaseYear
        assertEquals 'Some cool movie!', movie.description
        assertEquals 'T', movie.storage.name
        assertEquals 23, movie.storage.index
        assertNull movie.poster
        assertNull movie.genres
        assertNull movie.actors
    }

    @After
    void after(){
        super.tearDown()
    }
}
