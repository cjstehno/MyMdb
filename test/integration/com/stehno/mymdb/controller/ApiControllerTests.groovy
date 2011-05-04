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
import org.junit.Before
import org.junit.Test

 /**
 * 
 *
 * @author cjstehno
 */
class ApiControllerTests extends ControllerTestCase {

    private MovieTestFixture fixture = new MovieTestFixture()
    
    @Before
    void before(){
        fixture.before()

        controller = new ApiController()
    }

    @Test
    void categories_no_filter(){
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 5, jso

        assertItem 'titles','Titles',jso[0]
        assertItem 'genres','Genres',jso[1]
        assertItem 'actors','Actors',jso[2]
        assertItem 'years','Release Years',jso[3]
        assertItem 'units','Storage Units',jso[4]
    }

    @Test
    void categories_titles_filter(){
        controller.params.filter = 'titles'
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 2, jso

        assertItem 'A','A', jso[0]
        assertItem 'K','K', jso[1]
    }

    @Test
    void categories_genres_filter(){
        controller.params.filter = 'genres'
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso

        assertItem fixture.genreId,'Action', jso[0]
    }

    @Test
    void categories_actors_filter(){
        controller.params.filter = 'actors'
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 2, jso

        assertItem fixture.actorId,'Neason, Liam', jso[1]
    }

    @Test
    void categories_years_filter(){
        controller.params.filter = 'years'
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 2, jso

        assertItem 2000,2000, jso[0]
        assertItem 2010,2010, jso[1]
    }

    @Test
    void categories_units_filter(){
        controller.params.filter = 'units'
        controller.categories()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso

        assertItem fixture.storageUnitId,'X', jso[0]
    }

    @Test
    void list_nothing(){
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 0, jso
    }

    @Test
    void list_titles(){
        controller.params.category = 'titles'
        controller.params.filter = 'A'
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso
    }

    @Test
    void list_genres(){
        controller.params.category = 'genres'
        controller.params.filter = fixture.genreId as String
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso
    }

    @Test
    void list_actors(){
        controller.params.category = 'actors'
        controller.params.filter = fixture.actorId as String
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso
    }

    @Test
    void list_years(){
        controller.params.category = 'years'
        controller.params.filter = '2010'
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso
    }

    @Test
    void list_units(){
        controller.params.category = 'units'
        controller.params.filter = fixture.storageUnitId as String
        controller.list()

        def jso = parseJsonResponse()
        assertJsonCount 1, jso
    }

    @Test
    void fetch(){
        controller.params.id = fixture.movieId
        controller.fetch()

        def jso = parseJsonResponse()
        assertNotNull jso

        assertEquals 'A-Team: Unrated', jso.title
        assertEquals 'They were acused of a crime they didnt commit', jso.description
        assertEquals 2010, jso.releaseYear
        assertEquals 'Unrated', jso.rating
        assertEquals 'BluRay', jso.format
        assertEquals 'Movie', jso.broadcast
    }

    private void assertJsonCount( expected, jso ){
        assertNotNull jso
        assertEquals expected, jso.size()
    }

    private void assertItem( expectedId, expectedLabel, item ){
        assertEquals expectedId, item.id
        assertEquals expectedLabel, item.label
    }
}
