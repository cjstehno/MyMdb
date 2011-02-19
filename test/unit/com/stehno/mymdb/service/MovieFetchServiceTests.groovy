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
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.fetch.MovieDataProvider
import com.stehno.mymdb.fetch.MovieSearchResult
import com.stehno.mymdb.fetch.MovieData

class MovieFetchServiceTests extends GrailsUnitTestCase {

    def service

    @Before
    void before(){
        super.setUp()

        this.service = new MovieFetchService()

        this.service.tmdbMovieDataProvider = [
            'getProviderId':{'TMDB'},
            'searchFor':{
                if( it == 'Some Movie' ){
                    [ new MovieSearchResult( providerId:'TMDB', title:'Some Movie') ] as MovieSearchResult[]
                } else {
                    return [] as MovieSearchResult[]
                }
            },
            'fetch':{ new MovieData( title:'Some Movie' ) }
        ] as MovieDataProvider

        this.service.localMovieDataProvider = [
            'getProviderId':{'Local'},
            'searchFor':{
                if( it.startsWith('Some Movie') ){
                    [ new MovieSearchResult( providerId:'Local', title:'Some Movie: Special Features') ]  as MovieSearchResult[]
                } else {
                    return [] as MovieSearchResult[]
                }
            },
            'fetch':{ new MovieData( title:'Some Movie: Special Features' ) }
        ] as MovieDataProvider
    }

    @Test
    void search(){
        def results = service.search('Some Movie')
        assertNotNull results
        assertEquals 2, results.size()

        results = service.search('Some Movie: Special Features')
        assertNotNull results
        assertEquals 1, results.size()
    }

    @Test
    void search_FoundNone(){
        def results = service.search('Foo')
        assertNotNull results
        assertEquals 0, results.size()
    }

    @Test(expected = IllegalArgumentException)
    void fetch_BadProviderId(){
        service.fetch('asdf', 234)
    }

    @Test
    void fetch_Local(){
        def mov = service.fetch('Local', 123)
        assertEquals 'Some Movie: Special Features', mov.title
    }

    @Test
    void fetch_tmdb(){
        def mov = service.fetch('TMDB', 213)
        assertEquals 'Some Movie', mov.title
    }
}
