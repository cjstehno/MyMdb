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

package com.stehno.mymdb

import com.stehno.mymdb.domain.*

import static org.junit.Assert.assertEquals

 /**
 * Movie creation has gotten complex enough that I need a good solid set of movie fixture data.
 *
 * @author cjstehno
 */
class MovieTestFixture extends FixtureBuilder {

    def movieId
    def posterId
    def storageUnitId
    def genreId
    def actorId

    def fixtureIds = [
        actors:[:], storageUnits:[:], movies:[:]
    ]

    void before(){
        def movie = new Movie(
            title:'A-Team: Unrated',
            releaseYear:2010,
            description:'They were acused of a crime they didnt commit',
            mpaaRating:MpaaRating.UNRATED,
            format:Format.BLURAY,
            broadcast:Broadcast.MOVIE
        )
        movie.save(flush:true)
        fixtureIds.movies['A-Team: Unrated'] = movie.id

        def poster = new Poster( title:'A-Team', content:'fakedata'.getBytes() )
        poster.save(flush:true)

        this.posterId = poster.id

        def actors = buildActors('Liam Neason', 'Michael J Fox')
        this.actorId = actors[0].id
        fixtureIds.actors['Liam Neason'] = actors[0].id
        fixtureIds.actors['Michael J Fox'] = actors[1].id

        def genres = buildGenres('Action')
        this.genreId = genres[0].id

        def web = new WebSite( label:'TMDB', url:'http://tmdb.com')
        web.save(flush:true)

        movie.runtime = 120
        movie.poster = poster
        movie.addToActors(actors[0])
        movie.addToGenres(genres[0])
        movie.addToSites(web)
        movie.save(flush:true)

        this.movieId = movie.id

        def movie2 = new Movie( title:'Kung Fu Panda', releaseYear:2000, description:'A Panda movie', broadcast:Broadcast.MOVIE, mpaaRating: MpaaRating.G, format:Format.DVD )
        movie2.save(flush:true)

        fixtureIds.movies['Kung Fu Panda'] = movie2.id

        def storageUnit = buildStorageUnit('X', false, 10){
            buildStorage( 1 ){
                movie
            }
        }

        this.storageUnitId = storageUnit.id
        fixtureIds.storageUnits['X'] = storageUnit.id

        // sanity check the fixture
        assertEquals 2, Movie.count()
        assertEquals 1, Genre.count()
        assertEquals 2, Actor.count()
        assertEquals 1, WebSite.count()
        assertEquals 1, StorageUnit.count()
        assertEquals 1, Storage.count()
        assertEquals 1, Poster.count()
    }

    void clearDatabase(){
        Storage.list().each { storage->
            storage.movies?.clear()
        }

        StorageUnit.list().each deleteAll
        Movie.list().each deleteAll
        Genre.list().each deleteAll
        Actor.list().each deleteAll
        Poster.list().each deleteAll
        WebSite.list().each deleteAll

        // sanity check the fixture
        assertEquals 0, Movie.count()
        assertEquals 0, Genre.count()
        assertEquals 0, Actor.count()
        assertEquals 0, WebSite.count()
        assertEquals 0, StorageUnit.count()
        assertEquals 0, Storage.count()
        assertEquals 0, Poster.count()
    }

    private deleteAll = { x-> x.delete(flush:true) }
}
