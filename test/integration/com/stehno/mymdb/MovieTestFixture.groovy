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

 /**
 * Movie creation has gotten complex enough that I need a good solid set of movie fixture data.
 *
 * @author cjstehno
 */
class MovieTestFixture {

    def movieId
    def posterId
    def storageUnitId

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

        def poster = new Poster( title:'A-Team', content:'fakedata'.getBytes() )
        poster.save(flush:true)

        this.posterId = poster.id

        def actor = new Actor( firstName:'Liam', middleName:'', lastName:'Neason' )
        actor.save(flush:true)

        new Actor( firstName:'Michael', middleName:'J', lastName:'Fox' ).save(flush:true)

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

        this.movieId = movie.id

        new Movie( title:'Kung Fu Panda', releaseYear:2000, description:'A Panda movie', broadcast:Broadcast.MOVIE, mpaaRating: MpaaRating.G, format:Format.DVD ).save(flush:true)

        def storageUnit = new StorageUnit( name:'X', indexed:false, capacity:10 )
        storageUnit.save(flush:true)
        
        this.storageUnitId = storageUnit.id
    }
}
