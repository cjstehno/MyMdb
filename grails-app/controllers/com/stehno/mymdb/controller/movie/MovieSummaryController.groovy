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

import com.stehno.mymdb.ServiceValidationException
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.dto.ActorDto
import com.stehno.mymdb.dto.DetailsDto
import com.stehno.mymdb.dto.GenreDto
import grails.converters.JSON

/**
 * 
 *
 * @author cjstehno
 */
class MovieSummaryController extends MovieFlowControllerBase {

    def messageSource
    
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
        def resp = success()
        try {
            movieFlowService.save()

        } catch( ServiceValidationException sve ){
            resp.success = false
            resp.errors = [:]
            sve.errors.fieldErrors.each {
                resp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }
        render( resp as JSON )
    }
}