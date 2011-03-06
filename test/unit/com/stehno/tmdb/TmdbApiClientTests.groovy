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

package com.stehno.tmdb

import com.stehno.mymdb.service.MymdbConfigService
import org.junit.Before
import org.junit.Test

class TmdbApiClientTests extends GroovyTestCase {

    TmdbApiClient client
    String responseContent

    @Before
    void before(){
        def mymdbConfigService = [
            getTmdbApiKey:{ 'someapikey' }
        ] as MymdbConfigService

        this.client = new TmdbApiClient( mymdbConfigService:mymdbConfigService )

        // TODO: see if I can do this with a category/mixin instead
        URL.metaClass.withReader = { Closure c->
            new StringReader("{ success:true }").withReader c
        }
    }

    @Test void buildUrl(){
        assertEquals 'http://api.themoviedb.org/2.1/Movie.action/en/json/someapikey/Something+interesting', client.buildUrl( 'Movie.action', 'Something interesting').toString()
    }

    @Test void call(){
        assertSuccess client.call( 'http://localhost/test'.toURL() )
    }

    @Test
    void movieSearch(){
        assertSuccess client.movieSearch( 'The Thing' )
    }

    @Test
    void movieGetInfo(){
        assertSuccess client.movieGetInfo( 'someid' )
    }

    private void assertSuccess( resp ){
        assertNotNull resp
        assertTrue resp.success
    }
}