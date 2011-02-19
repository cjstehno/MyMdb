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
import com.stehno.mymdb.dto.FetchResultsDto
import grails.converters.JSON

 /**
 * This controller handles the state of the Movie Fetch panel of the movie flow.
 *
 * @author cjstehno
 */
class MovieFetchController extends MovieFlowControllerBase {

    def movieFetchService
    
    static allowedMethods = [ save:"POST", show:"GET", search:'POST', preview:'GET' ]

    def show = {
        movieFlowService.start()
        renderSuccess()
    }

    def save = { FetchResultsDto dto ->
        if( dto.hasErrors() ){
            renderErrors(request, dto)

        } else {
            if( dto.selectedId && dto.providerId ){
                def movieData = movieFetchService.fetch(dto.providerId, dto.selectedId)
                
                movieFlowService.populate(movieData)

                renderSuccess()

            } else {
                movieFlowService.store(dto)

                def detailsDto = movieFlowService.retrieve(DetailsDto.class)
                detailsDto.title = dto.title
                movieFlowService.store(detailsDto)

                renderSuccess()
            }
        }
    }

    def search = {
        def results = movieFetchService.search( params.title )
        render( [items:results] as JSON)
    }

    def preview = {
        movieFetchService.fetch( params.provider, params.id )
    }
}