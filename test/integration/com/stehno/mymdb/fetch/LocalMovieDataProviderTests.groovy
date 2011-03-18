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

package com.stehno.mymdb.fetch

import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.MovieTestFixture
import org.junit.After

/**
 * 
 *
 * @author cjstehno
 */
class LocalMovieDataProviderTests extends GrailsUnitTestCase {

    private movieTestFixture = new MovieTestFixture()
    def provider

    @Before
    void before(){
        super.setUp()
        movieTestFixture.before()

        this.provider = new LocalMovieDataProvider()
    }

    @Test
    void searchFor(){
        def results = provider.searchFor('A-Team')

        assertNotNull results
        assertEquals 1, results.size()

        assertEquals 'Local', results[0].providerId
        assertEquals movieTestFixture.movieId, results[0].id
        assertEquals 'A-Team: Unrated', results[0].title
        assertEquals 2010, results[0].releaseYear
        assertEquals 'They were acused of a crime they didnt commit', results[0].description
    }

    @Test
    void searchFor_NotFound(){
        def results = provider.searchFor('Blah')
        assertNotNull results
        assertEquals 0, results.size()
    }

    @Test
    void fetch(){
        def mov = provider.fetch(movieTestFixture.movieId)

        assertNotNull mov

        assertEquals 'Local', mov.providerId
        assertEquals 'A-Team: Unrated', mov.title
        assertEquals 2010, mov.releaseYear
        assertEquals 'They were acused of a crime they didnt commit', mov.description
        assertEquals( ['Action'], mov.genreNames )
        assertEquals( ['Liam Neason'], mov.actorNames )
        assertEquals "/null/poster/show/${movieTestFixture.posterId}", mov.posterUrl
        assertEquals MpaaRating.UNRATED, mov.rating
        assertEquals 120, mov.runtime
        assertEquals( ['TMDB':'http://tmdb.com'], mov.sites )
    }

    @After
    void after(){
        super.tearDown()
    }
}
