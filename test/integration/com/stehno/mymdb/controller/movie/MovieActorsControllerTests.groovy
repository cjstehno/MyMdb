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

import com.stehno.mymdb.dto.ActorDto
import com.stehno.mymdb.service.MovieFlowService
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * 
 *
 * @author cjstehno
 */
class MovieActorsControllerTests extends MovieFlowIntegrationTestBase {

    @Before
    void before(){
        super.setUp()

        controller = new MovieActorsController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show(){
        request('GET', '/movie/actors')

        controller.show()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success
        assertNotNull jso.data
    }

    @Test
    void save(){
        request('POST','/movie/actors')

        controller.params.actors = ['1','5','9']

        controller.save()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        def dto = controller.movieFlowService.flow[ActorDto.class.name]
        assertNotNull dto
        assertEquals 3, dto.actors.size()
        assertEquals 1, dto.actors[0]
        assertEquals 5, dto.actors[1]
        assertEquals 9, dto.actors[2]
    }

    @After
    void after(){
        super.tearDown()
    }
}
