/*
   Copyright 2010 Christopher J. Stehno (chris@stehno.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.stehno.mymdb.controller

import grails.converters.JSON
import com.stehno.mymdb.domain.Genre

class GenreController {

    def messageSource
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        def genres = Genre.list( sort:'name', order:'asc' ).collect {
            [id:it.id, label:it.name, count:it.movies?.size()]
        }
        render( [items:genres] as JSON )
    }

    def edit = {
        def outp = [:]

        def genreInstance = Genre.get(params.id)
        if (!genreInstance){
            outp.success = false
            outp.errorMessage = "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"

        } else {
            outp.success = true
            outp.data = [ id:genreInstance.id, name:genreInstance.name, version:genreInstance.version ]
        }

        render outp as JSON
    }

    def save = {
        def genreInstance = new Genre(params)
        def outp = [:]

        outp.success = genreInstance.save(flush:true) != null

        if( genreInstance.hasErrors()){
            outp.success = false

            outp.errors = [:]
            genreInstance.errors.fieldErrors.each {
                outp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }

        render outp as JSON
    }

    def update = {
        def outp = [:]

        def genreInstance = Genre.get(params.id)
        if (genreInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (genreInstance.version > version) {
                    outp.success = false
                    outp.errors = ['general':'Another user has updated this Genre while you were editing']
            
                    render outp as JSON
                    return
                }
            }

            genreInstance.properties = params
            if (!genreInstance.hasErrors() && genreInstance.save(flush: true)) {
                outp.success = true

            } else {
                outp.success = false
                outp.errors = [:]
                genreInstance.errors.fieldErrors.each {
                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }
            }
        } else {
            outp.success = false
            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"]
        }

        render outp as JSON
    }

    def delete = {
        def genreInstance = Genre.get(params.id)

        def outp = [:]

        if (genreInstance) {
            try {
                genreInstance.delete(flush: true)
                outp.success = true

            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                outp.success = false
                outp.errors = ['general': "${message(code: 'default.not.deleted.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"]
            }

        } else {
            outp.success = false
            outp.errors = ['general': "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"]
        }

        render outp as JSON
    }
}
