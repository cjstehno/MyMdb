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
import grails.converters.JSON

/**
 * 
 *
 * @author cjstehno
 */
class MoviePosterController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET" ]

    def show = {
        def dto = movieFlowService.retrieve(PosterDto.class)
        render( contentType:'text/html', text:(success( dto as Map ) as JSON).toString(false) )
    }

    def save = { PosterDto dto ->
//            if( dto.hasErrors() ){
//                render( contentType:'text/html', text:(errorResponse(dto,request) as JSON).toString(false) )
//
//            } else {
//                getFlow(session).poster = dto
//
//                if( dto.posterType == PosterType.URL ){
//                    dto.file = dto.url.toURL().getBytes()
//                }
//
//                render( contentType:'text/html', text:([success:true] as JSON).toString(false) )
//            }
        if( dto.hasErrors() ){
            renderErrors(request, dto)

        } else {
            movieFlowService.store(dto)
            renderSuccess()
        }
    }

    // TODO: need url mappings for these
    def fetch = {
//        def url = params.url
//
//        def flow = getFlow(session)
//        if( !flow.poster ){
//            flow.poster = new PosterDto()
//        }
//
//        flow.poster.posterType = PosterType.URL
//        flow.poster.file = url.toURL().getBytes()
//
//        render( [success:true] as JSON)
    }

    def select = {
//        def pid = params.id
//
//        def flow = getFlow(session)
//        if( !flow.poster ){
//            flow.poster = new PosterDto()
//        }
//
//        flow.poster.posterType = PosterType.EXISTING
//        flow.poster.posterId = pid as Long
//
//        render( [success:true] as JSON)
    }

    def clear = {
//        def flow = getFlow(session)
//        flow.remove('poster')
//
//        render( [success:true] as JSON)
    }
}
