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

import com.stehno.mymdb.dto.DetailsDto

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
        }

        def dto = movieFlowService.retrieve(DetailsDto.class)

        renderSuccess( dto as Map )
    }

    def save = { DetailsDto dto ->
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