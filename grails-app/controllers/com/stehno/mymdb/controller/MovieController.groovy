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
package com.stehno.mymdb.controller

import grails.converters.JSON
import org.springframework.dao.DataRetrievalFailureException

class MovieController {

    static allowedMethods = [delete:"POST"]

    def movieService

    def delete = {
        def outp = [:]

        try {
            movieService.deleteMovie params.id
            outp.success = true

        } catch(DataRetrievalFailureException drfe){
            populateErrors 'default.not.found.message', params.id

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            populateErrors 'default.not.deleted.message', params.id
        }

        render outp as JSON
    }

    private def populateErrors(code, movieId){
        outp.success = false
        outp.errors = ['general': "${message(code:code, args:[message(code:'movie.label', default:'Movie'), movieId])}"]
    }
}
