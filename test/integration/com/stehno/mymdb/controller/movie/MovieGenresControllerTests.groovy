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

import com.stehno.mymdb.dto.GenreDto
import com.stehno.mymdb.service.MovieFlowService
import org.junit.After
import org.junit.Before
import org.junit.Test

 /**
 * 
 *
 * @author cjstehno
 */
class MovieGenresControllerTests extends MovieFlowIntegrationTestBase {

    @Before
    void before(){
        super.setUp()

        controller = new MovieGenresController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show(){
        request('GET', '/movie/genres')

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
    }

    @Test
    void save(){
        request('POST','/movie/genres')

        controller.params.genres = ['1','5','9']

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[GenreDto.class.name]
        assertNotNull dto
        assertEquals 3, dto.genres.size()
        assertEquals 1, dto.genres[0]
        assertEquals 5, dto.genres[1]
        assertEquals 9, dto.genres[2]
    }

    @After
    void after(){
        super.tearDown()
    }
}
