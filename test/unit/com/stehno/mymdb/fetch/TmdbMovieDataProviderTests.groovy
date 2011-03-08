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

import com.stehno.mymdb.domain.MpaaRating
import com.stehno.mymdb.service.MymdbConfigService
import com.stehno.tmdb.TmdbApiClient
import org.junit.Before
import org.junit.Test

class TmdbMovieDataProviderTests extends GroovyTestCase {

    def provider

    @Before
    void before(){
        super.setUp()

        provider = new TmdbMovieDataProvider( api:mockTmdbApi() )
    }

    @Test
    void searchFor(){
        def results = provider.searchFor('Brewsters Millions')

        assertNotNull results
        assertEquals 3, results.size()

        assertEquals 'Brewsters Millions', results[0].title
        assertEquals 'TMDB', results[0].providerId
        assertEquals 100, results[0].id
        assertEquals 1983, results[0].releaseYear
        assertEquals 'A movie about a guy who spends a lot of money.', results[0].description

        assertEquals 'Brewsters Millions', results[1].title
        assertEquals 'TMDB', results[1].providerId
        assertEquals 300, results[1].id
        assertEquals 1983, results[1].releaseYear
        assertEquals '', results[1].description

        assertEquals 'Brewsters Millions: Uncut', results[2].title
        assertEquals 'TMDB', results[2].providerId
        assertEquals 200, results[2].id
        assertEquals 2000, results[2].releaseYear
        assertEquals 'A movie about a guy who spends a lot of money.', results[2].description
    }

    @Test
    void searchFor_NotFound(){
        def results = provider.searchFor('Intelligent Life')
        assertNotNull results
        assertEquals 0, results.size()
    }

    @Test
    void description(){
        def entry = [ isNull:{ false }, overview:('x'*10) ]
        assertEquals 'xxxxxxxxxx', provider.description( entry )

        entry.overview = ('x'*54)
        assertEquals 'x'*54, provider.description( entry )

        entry.overview = ('x'*55)
        assertEquals 'x'*55, provider.description( entry )

        entry.overview = ('x'*58)
        assertEquals 'x'*58, provider.description( entry )

        entry.overview = ('x'*100)
        assertEquals 'x'*60 + '...', provider.description( entry )

        entry.overview = 'A movie about a guy who spends a lot of money.'
        assertEquals 'A movie about a guy who spends a lot of money.', provider.description( entry )
    }

    @Test
    void fetch(){
        def info = provider.fetch('200')

        assertNotNull info
        assertEquals 'Brewsters Millions: Uncut', info.title
        assertEquals 'TMDB', info.providerId
        assertEquals 2000, info.releaseYear
        assertEquals 'A movie about a guy who spends a lot of money.', info.description
        assertEquals( ['Comedy'], info.genreNames)
        assertEquals( ['Richard Pryor'], info.actorNames)
        assertEquals 'http://poster.url', info.posterUrl
        assertEquals MpaaRating.PG_13, info.rating
        assertEquals 92, info.runtime
        assertEquals( [ 'TMDB':'http://tmdb.url' ], info.sites)
    }

    private mockTmdbApi(){
        def api = new TmdbApiClient()

        api.mymdbConfigService = ['getTmdbApiKey':{ 'testing' }] as MymdbConfigService

        api.metaClass.call = { url->
            def ustr = url as String
            if(ustr == 'http://api.themoviedb.org/2.1/Movie.search/en/json/testing/Brewsters+Millions'){
                return [
                    [ name:'Brewsters Millions', id:100, released:'1983-02-25', overview:'A movie about a guy who spends a lot of money.', isNull:{ false } ],
                    [ name:'Brewsters Millions', id:300, released:'1983-02-25', isNull:{n-> n != 'released' } ],
                    [ name:'Brewsters Millions: Uncut', id:200, released:'2000-02-25', overview:'A movie about a guy who spends a lot of money.', isNull:{ false } ],
                ]
            } else if(ustr == 'http://api.themoviedb.org/2.1/Movie.search/en/json/testing/Intelligent+Life'){
                return ['']
            } else if(ustr == 'http://api.themoviedb.org/2.1/Movie.getInfo/en/json/testing/200'){
                return [
                    [
                        id:200,
                        name:'Brewsters Millions: Uncut',
                        released:'2000-02-25',
                        overview:'A movie about a guy who spends a lot of money.',
                        certification:'PG-13',
                        runtime:92,
                        posters:[ [image:[ type:'poster', size:'cover', url:'http://poster.url' ] ] ],
                        genres:[ [name:'Comedy'] ],
                        cast:[ [job:'Actor', name:'Richard Pryor'] ],
                        url:'http://tmdb.url',
                        isNull:{ it != 'url' && it != 'released' }, getString:{'http://tmdb.url'}
                    ]
                ]
            } else {
                throw new RuntimeException('Invalid URL')
            }
        }
        return api
    }
}
