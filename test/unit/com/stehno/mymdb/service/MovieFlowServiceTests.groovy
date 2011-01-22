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

import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieFlowServiceTests extends GrailsUnitTestCase {

    def movieFlowService

    @Before
    void before(){
        movieFlowService = new MovieFlowService()
    }

    @Test
    void start(){
        movieFlowService.flow.foo = 42
        
        movieFlowService.start()

        assertTrue movieFlowService.flow.isEmpty()
    }

    @Test
    void start_for_edit(){
        movieFlowService.flow.foo = 42

        movieFlowService.start('123')

        assertEquals 1, movieFlowService.flow.size()
        assertEquals 123, movieFlowService.flow.movieId
    }

    @Test
    void store(){
        def dto = [name:'Foo', value:42]

        def ret = movieFlowService.store(dto)

        assertEquals dto, ret
        assertEquals dto, movieFlowService.flow[dto.getClass().name]
    }

    @Test
    void retrieve_new(){
        def dto = movieFlowService.retrieve(FooDto.class)
        assertNotNull dto
        assertTrue dto instanceof FooDto
        assertNull dto.name
        assertNull dto.age
    }

    @Test
    void retrieve_existing(){
        movieFlowService.store(new FooDto(name:'Bob', age:32))

        def dto = movieFlowService.retrieve(FooDto.class)
        assertNotNull dto
        assertTrue dto instanceof FooDto
        assertEquals 'Bob', dto.name
        assertEquals 32, dto.age
    }

    @After
    void after(){
        movieFlowService = null
    }

    private static class FooDto {
        String name
        Integer age
    }
}
