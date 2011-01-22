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

/**
 * This controller handles the state of the Movie Fetch panel of the movie flow.
 *
 * @author cjstehno
 */
class MovieFetchController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET" ]

    def show = {
        movieFlowService.start()
        renderSuccess()
    }

    def save = { FetchResultsDto dto ->
        if( dto.hasErrors() ){
            renderErrors(request, dto)

        } else {
            movieFlowService.store(dto)

            // TODO: movie fetch data will populate other DTOs here

            def detailsDto = movieFlowService.retrieve(DetailsDto.class)
            detailsDto.title = dto.title
            movieFlowService.store(detailsDto)

            renderSuccess()
        }
    }
}