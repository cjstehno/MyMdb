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

import com.stehno.tmdb.TmdbApiClient
import org.junit.Before
import org.junit.Test

 /**
 * 
 *
 * @author cjstehno
 */
class TmdbMovieDataProviderTests extends GroovyTestCase {

    def provider

    @Before
    void before(){
        super.setUp()

        def api = new TmdbApiClient()
        api.apiKey = 'testing'
        api.metaClass.call = { url->
            def ustr = url as String
            if(ustr == 'http://api.themoviedb.org/2.1/Movie.search/en/json/testing/Brewsters+Millions'){
                return [
                    [ name:'Brewsters Millions', id:100 ],
                    [ name:'Brewsters Millions: Uncut', id:200 ]
                ]
            } else if(ustr == 'http://api.themoviedb.org/2.1/Movie.getInfo/en/json/testing/200'){
                return [
                    name:'Brewsters Millions: Uncut'
                ]
            } else {
                throw new RuntimeException('Invalid URL')
            }
        }

        provider = new TmdbMovieDataProvider()
        provider.api = api
    }

    @Test
    void searchFor(){
        def results = provider.searchFor('Brewsters Millions')

        assertNotNull results
        assertEquals 2, results.size()

        assertEquals 'Brewsters Millions', results[0].title
        assertEquals 100, results[0].movieId

        assertEquals 'Brewsters Millions: Uncut', results[1].title
        assertEquals 200, results[1].movieId
    }

    @Test
    void fetch(){
        def info = provider.fetch('200')

        assertNotNull info
        assertEquals 'Brewsters Millions: Uncut', info.title
    }
}
