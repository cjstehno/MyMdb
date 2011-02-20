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

import com.stehno.mymdb.MovieTestFixture
import com.stehno.mymdb.service.MovieFlowService
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.dto.*

class MovieFetchControllerTests extends MovieFlowIntegrationTestBase {

    private movieFixture = new MovieTestFixture()

    @Before
    void before(){
        super.setUp()
        movieFixture.before()

        controller = new MovieFetchController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show(){
        request('GET', '/movie/fetch')
        
        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
    }

    @Test
    void save_create(){
        request('POST','/movie/fetch')

        controller.params.title = 'A Cool Title'

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[FetchResultsDto.class.name]
        assertNotNull dto
        assertEquals 'A Cool Title', dto.title

        def details = controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull details
        assertEquals 'A Cool Title', details.title
    }

    @Test
    void save_fetched(){
        request('POST','/movie/fetch')

        controller.params.selectedId = movieFixture.movieId
        controller.params.providerId = 'Local'

        controller.save()

        def details = controller.movieFlowService.flow[DetailsDto.class.name]
        assertNotNull details

        def poster = controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull poster

        def genres = controller.movieFlowService.flow[GenreDto.class.name]
        assertNotNull genres

        def actors = controller.movieFlowService.flow[ActorDto.class.name]
        assertNotNull actors
    }

    @After
    void after(){
        super.tearDown()
        movieFixture.after()
    }
}
