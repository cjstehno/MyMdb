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

 /**
 * 
 *
 * @author cjstehno
 */
class LocalMovieDataProviderTests extends GrailsUnitTestCase {

    def movieId, posterId
    def provider

    @Before
    void before(){
        super.setUp()

        def movie = new Movie(
            title:'A-Team: Unrated',
            releaseYear:2010,
            storage:new Storage(name:'A',index:2),
            description:'They were acused of a crime they didnt commit',
            mpaaRating:MpaaRating.UNRATED,
            format:Format.BLUERAY
        )
        movie.save(flush:true)

        def poster = new Poster( title:'A-Team', content:'fakedata'.getBytes() )
        poster.save(flush:true)

        posterId = poster.id
        
        def actor = new Actor( firstName:'Liam', middleName:'', lastName:'Neason' )
        actor.save(flush:true)

        def genre = new Genre( name:'Action' )
        genre.save(flush:true)

        def web = new WebSite( label:'TMDB', url:'http://tmdb.com')
        web.save(flush:true)

        movie.runtime = 120
        movie.poster = poster
        movie.addToActors(actor)
        movie.addToGenres(genre)
        movie.addToSites(web)
        movie.save(flush:true)

        movieId = movie.id

        new Movie( title:'Kung Fu Panda', releaseYear:2000, storage:new Storage(name:'B',index:6), description:'A Panda movie', mpaaRating: MpaaRating.G, format:Format.DVD ).save(flush:true)

        this.provider = new LocalMovieDataProvider()
    }

    @Test
    void searchFor(){
        def results = provider.searchFor('A-Team')

        assertNotNull results
        assertEquals 1, results.size()

        assertEquals 'Local', results[0].providerId
        assertEquals movieId, results[0].id
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
        def mov = provider.fetch(movieId)

        assertNotNull mov

        assertEquals 'Local', mov.providerId
        assertEquals 'A-Team: Unrated', mov.title
        assertEquals 2010, mov.releaseYear
        assertEquals 'They were acused of a crime they didnt commit', mov.description
        assertEquals( ['Action'], mov.genreNames )
        assertEquals( ['Liam  Neason'], mov.actorNames )
        assertEquals "/poster/show/${posterId}", mov.posterUrl
        assertEquals MpaaRating.UNRATED, mov.rating
        assertEquals 120, mov.runtime
        assertEquals( ['TMDB':'http://tmdb.com'], mov.sites )
    }
}
