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

import com.stehno.mymdb.ServiceValidationException
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Poster
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.dto.*
import com.stehno.mymdb.domain.Actor

class MovieFlowService {

    static scope = "session" // TODO: not entirely happy with this
    static transactional = true

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
     * @throws ServiceValidationException if there are validation errors
     */
    def void save(){
        // TODO: need to account for pessimistic locking version
        // TODO: need to ensure that this is transactional

        def movie = flow.movieId ? Movie.get(flow.movieId) : new Movie()

        def details = retrieve(DetailsDto.class)
        movie.title = details.title
        movie.releaseYear = details.releaseYear
        movie.storage = new Storage( name:details.storageName?.toUpperCase(), index:details.storageIndex )
        movie.description = details.description

        // set poster
        def poster = retrieve(PosterDto.class)
        if( poster.posterType == PosterType.URL || poster.posterType == PosterType.FILE ){
            // the content is already in the dto
            def thePoster = new Poster( title:details.title, content:poster.file )
            if( !thePoster.save(flush:true) ) {
                // since its been validated along the way this should rarely happen
                throw new ServiceValidationException('Unable to persist poster.', thePoster.errors)
            } else {
                movie.poster = thePoster
            }

        } else if(poster.posterType == PosterType.EXISTING){
            movie.poster = Poster.get(poster.posterId)

        }

        // genres
        movie.genres?.clear()
        def selectedGenreIds = retrieve(GenreDto.class).genres ?: []
        selectedGenreIds.each { gid->
            movie.addToGenres Genre.get(gid)
        }

        movie.actors?.clear()
        def selectedActorIds = retrieve(ActorDto.class).actors ?: []
        selectedActorIds.each { aid->
            movie.addToActors Actor.get(aid)
        }

        movie.save(flush:true)
        if( movie.hasErrors() ){
            // since its been validated along the way this should rarely happen
            throw new ServiceValidationException('Unable to persist movie.', movie.errors)
        }
    }
}
