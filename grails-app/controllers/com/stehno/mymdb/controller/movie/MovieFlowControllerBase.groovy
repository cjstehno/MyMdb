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

import grails.converters.JSON

/**
 * 
 *
 * @author cjstehno
 */
abstract class MovieFlowControllerBase {

    def movieFlowService
    def messageSource

    protected def renderErrors( request, dto ){
        render( errors(request,dto) as JSON )
    }

    protected def renderSuccess( dto = null ){
        render( success() as JSON )
    }

    protected def success( dto = null ){
        def data = [:]
        data.success = true
        if(dto) data.data = dto
        data
    }

    protected def errors( request, dto ){
        def outp = [success:false, errors:[:]]
        dto.errors.fieldErrors.each {
            outp.errors[it.field] = messageSource.getMessage( it, request.locale)
        }
        outp
    }
}
