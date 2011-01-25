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

import com.stehno.mymdb.dto.PosterDto
import com.stehno.mymdb.dto.PosterType
import grails.converters.JSON

 /**
 * 
 *
 * @author cjstehno
 */
class MoviePosterController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET", fetch:"GET", select:"POST", clear:"POST" ]

    def show = {
        def dto = movieFlowService.retrieve(PosterDto.class)
        render( contentType:'text/html', text:(success( dto as Map ) as JSON).toString(false) )
    }

    def save = { PosterDto dto ->
        if( dto.hasErrors() ){
            render( contentType:'text/html', text:(errorResponse( dto as Map ) as JSON).toString(false) )

        } else {
            if( dto.posterType == PosterType.URL ){
                dto.file = dto.url.toURL().getBytes()
            }

            movieFlowService.store(dto)
            render( contentType:'text/html', text:(success() as JSON).toString(false) )
        }
    }

    def fetch = {
        def url = params.url

        def poster = movieFlowService.retrieve(PosterDto.class)

        poster.posterType = PosterType.URL
        poster.file = url.toURL().getBytes()

        movieFlowService.store(poster)

        renderSuccess()
    }

    def select = {
        def poster = movieFlowService.retrieve(PosterDto.class)

        poster.posterType = PosterType.EXISTING
        poster.posterId = params.id as Long

        movieFlowService.store(poster)

        renderSuccess()
    }

    def clear = {
        movieFlowService.store(new PosterDto())
        renderSuccess()
    }
}
