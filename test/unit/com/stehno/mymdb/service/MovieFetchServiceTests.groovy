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

import com.stehno.mymdb.fetch.MovieData
import com.stehno.mymdb.fetch.MovieDataProvider
import com.stehno.mymdb.fetch.MovieSearchResult
import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test

class MovieFetchServiceTests extends GrailsUnitTestCase {

    MovieFetchService service

    @Before
    void before(){
        super.setUp()

        this.service = new MovieFetchService()
        this.service.providers['Fake'] = [
            'getProviderId':{'Fake'},
            'searchFor':{
                if( it == 'Some Movie' ){
                    [ new MovieSearchResult( providerId:'TMDB', title:'Some Movie') ] as MovieSearchResult[]
                } else {
                    return [] as MovieSearchResult[]
                }
            },
            'fetch':{ new MovieData( title:'Some Movie' ) }
        ] as MovieDataProvider
    }

    @Test
    void search(){
        def results = service.search('Some Movie')
        assertNotNull results
        assertEquals 1, results.size()

        results = service.search('Some Movie: Special Features')
        assertNotNull results
        assertEquals 0, results.size()
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
    void fetch(){
        def mov = service.fetch('Fake', 123)
        assertEquals 'Some Movie', mov.title
    }
}
