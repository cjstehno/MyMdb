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

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.dto.DetailsDto

class MovieFlowService {

    static scope = "session" // TODO: not entirely happy with this
    static transactional = false

    private final flow = [:]

    /**
     * Starts the flow and cleans out any pre-existing data. IF a movieId
     * is specified, it will be stored and the flow will be in "edit" mode.
     *
     * @param movieId id of movie being edited (null for new movie)
     */
    def start( movieId = null ){
        flow.clear()
        if(movieId) flow.movieId = (movieId as Long)
    }

    /**
     * Retrieve the stored DTO, or an empty new instance if none exists for that
     * type. IF a new DTO instance is created, it will not be stored automatically.
     *
     * @param dto the DTO type to be retrieved, should be a class instance
     * @return either the existing populated reference or a new instance of the dto
     */
    def retrieve( Class dto ){
        flow[dto.name] ?: dto.newInstance()
    }

    /**
     * Stores the given DTO in the flow, using its classname as its key. Any
     * pre-existing data for the DTO will be over-written.
     *
     * @param dto the DTO to be stored
     * @return the DTO object will be returned (as a convenience)
     */
    def store( dto ){
        flow[dto.getClass().name] = dto
    }

    /**
     * Saves or updates the movie current stored in the flow data.
     *
     * @param movie
     * @return
     */
    def save(){
        // TODO: need to account for pessimistic locking version

        def movie = flow.movieId ? Movie.get(flow.movieId) : new Movie()

        def details = retrieve(DetailsDto.class)
        movie.title = details.title
        movie.releaseYear = details.releaseYear
        movie.storage = new Storage( name:details.storageName?.toUpperCase(), index:details.storageIndex )
        movie.description = details.description
//
//        // set poster
//        if( flow.poster.posterType == PosterType.URL || flow.poster.posterType == PosterType.FILE ){
//            // the content is already in the dto
//            def thePoster = new Poster( title:movie.title, content:flow.poster.file )
//            if( !thePoster.save(flush:true) ) {
//               thePoster.errors.each {
//                   // TODO: these need to be sent to front end!
//                    println it
//               }
//            } else {
//                movie.poster = thePoster
//            }
//
//        } else if(flow.poster.posterType == PosterType.EXISTING){
//            movie.poster = Poster.get(flow.poster.posterId)
//
//        }
//
//        flow.genre.genres?.each {
//            def gen = Genre.get(it)
//            if( !movie.genres.contains(gen) ){
//                movie.addToGenres( gen )
//            }
//        }
//
//        flow.actor.actors?.each {
//            def act = Actor.get(it)
//            if( !movie.actors.contains(act) ){
//                movie.addToActors( act )
//            }
//        }
//
//        def resp = [success:(movie.save(flush:true) != null)]
//        if( movie.hasErrors() ){
//            resp.success = false
//
//            resp.errors = [:]
//            movie.errors.fieldErrors.each {
//                resp.errors[it.field] = messageSource.getMessage( it, request.locale)
//            }
//        }
    }
}
