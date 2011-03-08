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

package com.stehno.mymdb.controller

import com.stehno.mymdb.MovieTestFixture
import com.stehno.mymdb.domain.Actor
import org.junit.Before
import org.junit.Test

class ActorControllerTests extends ControllerTestCase {

    private MovieTestFixture fixture = new MovieTestFixture()

    @Before
    void before(){
        fixture.before()

        controller = new ActorController()
    }

    @Test
    void list(){
        controller.list()

        def jso = parseJsonResponse()
        assertEquals 'Fox, Michael J', jso.items[0].label
        assertEquals 'Neason, Liam', jso.items[1].label
    }

    @Test
    void save(){
        nameParams( 'Curley', 'C', 'Stooge' )

        controller.save()

        assertSuccessful parseJsonResponse()

        assertNotNull Actor.findByLastName('Stooge')
    }

    @Test
    void save_firstName_null(){
        nameParams( null, 'C', 'Stooge' )

        controller.save()

        assertSuccessful parseJsonResponse()

        assertNotNull Actor.findByLastName('Stooge')
    }

    @Test
    void save_firstName_empty(){
        nameParams( '', 'C', 'Stooge' )

        controller.save()

        assertSuccessful parseJsonResponse()

        assertNotNull Actor.findByLastName('Stooge')
    }

    @Test
    void save_firstName_toolong(){
        nameParams( ('x'*26), 'C', 'Stooge' )

        controller.save()

        assertFailure parseJsonResponse(), 'firstName', 'Actor property first name length must be between 1 and 25.'
    }

    private nameParams( first, middle, last){
        controller.params.firstName = first
        controller.params.middleName = middle
        controller.params.lastName = last
    }

    @Test
    void save_NoName(){
        controller.save()

        assertFailure parseJsonResponse(), 'lastName', 'Actor last name is required.'
    }

    @Test
    void update(){
        assertEquals 2, Actor.count()
        
        def theActor = Actor.findByFirstName('Michael')
        controller.params.id = theActor.id
        controller.params.version = theActor.version
        controller.params.firstName = 'Moester'
        controller.params.middleName = 'Clarence'
        controller.params.lastName = 'Stooger'

        controller.update()

        assertSuccessful parseJsonResponse()

        assertEquals 2, Actor.count()
    }

    @Test
    void delete(){
        assertEquals 2, Actor.count()

        controller.params.id = Actor.findByFirstName('Michael').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def actors = Actor.list()
        assertEquals 1, actors.size()
        assertEquals 'Liam', actors[0].firstName

        assertEquals 1, Actor.count()
    }

    @Test
    void delete_NotFound(){
        assertEquals 2, Actor.count()
        
        controller.delete()

        assertFailure parseJsonResponse(), 'general', 'Actor not found with id null'

        assertEquals 2, Actor.count()
    }
}
