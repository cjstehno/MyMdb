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

package com.stehno.mymdb.controller.movie

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.dto.*

/**
 * This controller handles the state for the movie details panel of the
 * movie flow.
 *
 * @author cjstehno
 */
class MovieDetailsController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET" ]

    def show = {
        def movieId = params.id
        if(movieId){
            // editing movie - this is flow start
            movieFlowService.start(movieId)

            populateFlowData( Movie.get(movieId) )
        }

        renderSuccess( movieFlowService.retrieve(DetailsDto.class) )
    }

    def save = { DetailsDto dto ->
        if( dto.hasErrors() ){
            renderErrors(request, dto)

        } else {
            movieFlowService.store(dto)
            renderSuccess()
        }
    }

    /**
     * Copies the movie data into the appropriate DTO objects for use
     * in the flow
     *
     * @param movie the movie being editied
     */
    private def void populateFlowData( movie ){
        movieFlowService.store(new DetailsDto(
            title:movie.title,
            releaseYear:movie.releaseYear,
            storageName:movie.storage.name,
            storageIndex:movie.storage.index,
            description:movie.description
        ))

        if(movie.poster){
            movieFlowService.store(new PosterDto(
                posterType:PosterType.EXISTING,
                posterId:movie.poster.id,
                posterName:movie.poster.title
            ))
        }

        if(movie.genres){
            movieFlowService.store(new GenreDto(
                genres:movie.genres.collect { it.id }
            ))
        }

        if(movie.actors){
            movieFlowService.store(new ActorDto(
                actors:movie.actors.collect { it.id }
            ))
        }
    }
}