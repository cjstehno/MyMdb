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

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.dto.ActorDto
import com.stehno.mymdb.dto.DetailsDto
import com.stehno.mymdb.dto.GenreDto

 /**
 * 
 *
 * @author cjstehno
 */
class MovieSummaryController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET" ]

    def show = {
        def details = movieFlowService.retrieve(DetailsDto.class)

        def genresDto = movieFlowService.retrieve(GenreDto.class)
        def genres = genresDto?.genres?.collect { Genre.get(it) }

        def actorsDto = movieFlowService.retrieve(ActorDto.class)
        def actors = actorsDto?.actors?.collect { Actor.get(it) }

        [
            title:details?.title,
            releaseYear:details?.releaseYear,
            storage:"${details?.storageName}-${details?.storageIndex}",
            description:details?.description,
            genres:genres,
            actors:actors
        ]
    }

    def save = {
        movieFlowService.save()

//        def movie = flow.movieId ? Movie.get(flow.movieId) : new Movie()
//        movie.title = flow.details.title
//        movie.releaseYear = flow.details.releaseYear
//        movie.storage = new Storage( name:flow.details.storageName?.toUpperCase(), index:flow.details.storageIndex )
//        movie.description = flow.details.description
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
//
//        render( resp as JSON )
    }
}
