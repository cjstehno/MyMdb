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
import com.stehno.mymdb.dto.ActorDto
import grails.converters.JSON

/**
 * 
 *
 * @author cjstehno
 */
class MovieActorsController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET" ]

    def show = {
        renderSuccess( movieFlowService.retrieve(ActorDto.class) )
    }

    def list = {
        def dto = movieFlowService.retrieve(ActorDto.class)

        def items = Actor.list( sort:'lastName', order:'asc' ).collect { a->
            [ id:a.id, label:a.displayName, selected:( a.id in dto.actors ) ]
        }

        render( [ items:items ] as JSON)
    }

    def save = { ActorDto dto ->
        if( dto.hasErrors() ){
            renderErrors(request, dto)

        } else {
            movieFlowService.store(dto)
            if(params.finish){
                forward( controller:'movieSummary', action:'save' )
            } else {
                renderSuccess()
            }
        }
    }
}
