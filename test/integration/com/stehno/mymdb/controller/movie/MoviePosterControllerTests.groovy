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

import com.stehno.mymdb.dto.PosterDto
import com.stehno.mymdb.dto.PosterType
import com.stehno.mymdb.service.MovieFlowService
import org.junit.After
import org.junit.Before
import org.junit.Test

class MoviePosterControllerTests extends MovieFlowIntegrationTestBase {

    @Before
    void before(){
        super.setUp()

        controller = new MoviePosterController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show(){
        request('GET', '/movie/poster')

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
    }

    @Test
    void save_existing(){
        request('POST','/movie/poster')

        controller.params.posterType = PosterType.EXISTING.name()
        controller.params.posterId = '123'

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull dto
        assertEquals PosterType.EXISTING, dto.posterType
        assertEquals 123, dto.posterId
    }

    @Test
    void save_none(){
        request('POST','/movie/poster')

        controller.params.posterType = PosterType.NONE.name()

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull dto
        assertEquals PosterType.NONE, dto.posterType
    }

    @Test
    void select(){
        request('POST','/movie/poster/select')

        controller.params.id = '12'

        controller.select()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull dto
        assertEquals PosterType.EXISTING, dto.posterType
        assertEquals 12, dto.posterId
    }

    @Test
    void clear(){
        request('POST','/movie/poster/clear')

        controller.clear()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[PosterDto.class.name]
        assertNotNull dto
        assertEquals PosterType.URL, dto.posterType
        assertEquals 'http://', dto.url
        assertEquals 0, dto.posterId
    }

    @After
    void after(){
        super.tearDown()
    }
}
