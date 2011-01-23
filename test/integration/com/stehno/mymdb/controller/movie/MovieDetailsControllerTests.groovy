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

class MovieDetailsControllerTests extends MovieFlowIntegrationTestBase {

    @Before
    void before(){
        super.setUp()

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
        def movie = new Movie(
            title:'The Thing!',
            releaseYear: 1984,
            storage: new Storage(name:'A', index:1),
            description: 'A cool movie!'
        )
        movie.save(flush:true)

        controller.params.id = movie.id as String

        request('GET', "/movie/details")

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
        assertEquals movie.title, jso.data.title
        assertEquals movie.releaseYear, jso.data.releaseYear
        assertEquals movie.storage.name, jso.data.storageName
        assertEquals movie.storage.index, jso.data.storageIndex
        assertEquals movie.description, jso.data.description

        assertEquals movie.id, controller.movieFlowService.flow.movieId
        assertNotNull controller.movieFlowService.flow[DetailsDto.class.name]
        assertNull controller.movieFlowService.flow[PosterDto.class.name]
        assertNull controller.movieFlowService.flow[GenreDto.class.name]
        assertNull controller.movieFlowService.flow[ActorDto.class.name]
    }

    @Test
    void show_edit_full(){
        def genre = new Genre(name: 'Horror')
        genre.save(flush:true);

        def actor = new Actor(firstName:'Wilford', middleName:'', lastName:'Brimley')
        actor.save(flush:true)

        def poster = new Poster(title:'Thing', content:'fake'.getBytes() )
        poster.save(flush:true)

        def movie = new Movie(
            title:'The Thing!',
            releaseYear: 1984,
            storage: new Storage(name:'A', index:1),
            description: 'A cool movie!'
        )
        movie.addToGenres genre
        movie.addToActors actor
        movie.poster = poster
        movie.save(flush:true)

        controller.params.id = movie.id as String

        request('GET', "/movie/details")

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
        assertEquals movie.title, jso.data.title
        assertEquals movie.releaseYear, jso.data.releaseYear
        assertEquals movie.storage.name, jso.data.storageName
        assertEquals movie.storage.index, jso.data.storageIndex
        assertEquals movie.description, jso.data.description

        assertEquals movie.id, controller.movieFlowService.flow.movieId
        assertNotNull controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull controller.movieFlowService.flow[GenreDto.class.name]
        assertNotNull controller.movieFlowService.flow[ActorDto.class.name]
    }

    @Test
    void save(){
        request('POST','/movie/details')

        controller.params.title = 'A Cool Title'
        controller.params.releaseYear = '2007'
        controller.params.description = 'Some cool movie!'
        controller.params.storageName = 'T'
        controller.params.storageIndex = '23'

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull dto
        assertEquals 'A Cool Title', dto.title
        assertEquals 2007, dto.releaseYear
        assertEquals 'Some cool movie!', dto.description
        assertEquals 'T', dto.storageName
        assertEquals 23, dto.storageIndex
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
        assertEquals 'Property [title] of class [class com.stehno.mymdb.dto.DetailsDto] with value [null] does not pass custom validation', errors.title
        assertEquals 'Property [releaseYear] of class [class com.stehno.mymdb.dto.DetailsDto] cannot be null', errors.releaseYear

        assertNull controller.movieFlowService.flow[DetailsDto.class.name]
    }

    @After
    void after(){
        super.tearDown()
    }
}
