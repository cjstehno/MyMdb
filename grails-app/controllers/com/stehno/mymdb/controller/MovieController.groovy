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

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.dto.*

import grails.converters.JSON

class MovieController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def messageSource

    def save = { 
        def movie = new Movie(params)
        if(movie.storage){
            movie.storage.name = movie.storage.name.toUpperCase()
        }

        def resp = [success:(movie.save(flush:true) != null)]

        if( movie.hasErrors() ){
            resp.success = false

            resp.errors = [:]
            movie.errors.fieldErrors.each {
                resp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }

        render( contentType:'text/html', text:(resp as JSON).toString(false) )
    }
	
    def update = {
        def outp = [:]

        def movie = Movie.get(params.id)
        if (movie) {
            if (params.version) {
                def version = params.version.toLong()
                if (movie.version > version) {
                    outp.success = false
                    outp.errors = ['general':'Another user has updated this Movie while you were editing']

                    render outp as JSON
                    return
                }
            }
			
            // protect the existing image data
            def posterHolder = null
            if(params.poster.bytes.length == 0){
                posterHolder = movie.poster
            }
			
            movie.properties = params
            if(posterHolder){
                movie.poster = posterHolder
            }
			
            if(movie.storage){
                movie.storage.name = movie.storage.name.toUpperCase()
            }
			
            if (!movie.hasErrors() && movie.save(flush: true)) {
                //movie.setTags( params.tags?.split().toList() )
				
                outp.success = true
				
            } else {
                outp.success = false
                outp.errors = [:]
                movie.errors.fieldErrors.each {
                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }
            }

        } else {
            outp.success = false
            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"]
        }

        render( contentType:'text/html', text:(outp as JSON).toString(false) )
    }

    def edit = {
        def outp = [:]

        def movie = Movie.get(params.id)
        if (!movie){
            outp.success = false
            outp.errorMessage = "${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"

        } else {
            outp.success = true
            outp.data = [
                id:movie.id,
                version:movie.version,
                title:movie.title,
                description:movie.description,
                releaseYear:movie.releaseYear,
				'storage.name':movie.storage.name, 
				'storage.index':movie.storage.index,
				'genres':movie.genres?.collect { g-> g.id },
				'actors':movie.actors?.collect {a-> a.id }
            ]
        }

        render outp as JSON
    }
	
    def delete = {
        def movie = Movie.get(params.id)

        def outp = [:]

        if (movie) {
            try {
                movie.delete(flush: true)
                outp.success = true

            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                outp.success = false
                outp.errors = ['general': "${message(code: 'default.not.deleted.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"]
            }

        } else {
            outp.success = false
            outp.errors = ['general': "${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"]
        }

        render outp as JSON
    }
	
    // flow
	
    private static final def FLOWKEY = 'movie.flow'
	
    def fetchResults = { FetchResultsDto dto ->
        if( isGet(request) ){
            // prepare flow session (or clear out existing)
            session[FLOWKEY] = [:]
            
            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                getFlow(session).fetchResults = dto
                render( [ success:true ] as JSON )
            }
        }
    }

    def details = { DetailsDto dto ->
        if( isGet(request) ){
            def flow = getFlow(session)
            if( !flow.details ){
                flow.details = dto
            } else {
                dto = flow.details
            }

            dto.title = dto.title ?: flow.fetchResults.title

            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                getFlow(session).details = dto
                render( [ success:true ] as JSON )
            }
        }
    }

    def poster = { PosterDto dto ->
        if( isGet(request) ){
            getFlow(session).poster = dto

            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                getFlow(session).poster = dto
                render( [ success:true ] as JSON )
            }
        }
    }

    def genre = { GenreDto dto ->
        if( isGet(request) ){
            getFlow(session).genre = dto

            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                getFlow(session).genre = dto
                render( [ success:true ] as JSON )
            }
        }
    }

    def actor = { ActorDto dto ->
        if( isGet(request) ){
            getFlow(session).actor = dto

            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                getFlow(session).actor = dto
                render( [ success:true ] as JSON )
            }
        }
    }
	
    // this is where the form data would be finally submitted (validation should be done at each step
    def finish = {
        render( [ success:true, data:[ flowkey:100 ] ] as JSON )
    }

    ///

    private def errorResponse( dto, request ){
        def outp = [success:false, errors:[:]]
        dto.errors.fieldErrors.each {
            outp.errors[it.field] = messageSource.getMessage( it, request.locale)
        }
        outp
    }

    private def isGet( req ){
        req.method.equalsIgnoreCase('get')
    }

    private def getFlow( session ){
        session[FLOWKEY]
    }
}
