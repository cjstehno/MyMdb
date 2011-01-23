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
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.dto.*

class MovieController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def messageSource
    def movieFlowService

//    def update = {
//        def outp = [:]
//
//        def movie = Movie.get(params.id)
//        if (movie) {
//            if (params.version) {
//                def version = params.version.toLong()
//                if (movie.version > version) {
//                    outp.success = false
//                    outp.errors = ['general':'Another user has updated this Movie while you were editing']
//
//                    render outp as JSON
//                    return
//                }
//            }
//
//            // protect the existing image data
//            def posterHolder = null
//            if(params.poster.bytes.length == 0){
//                posterHolder = movie.poster
//            }
//
//            movie.properties = params
//            if(posterHolder){
//                movie.poster = posterHolder
//            }
//
//            if(movie.storage){
//                movie.storage.name = movie.storage.name.toUpperCase()
//            }
//
//            if (!movie.hasErrors() && movie.save(flush: true)) {
//                //movie.setTags( params.tags?.split().toList() )
//
//                outp.success = true
//
//            } else {
//                outp.success = false
//                outp.errors = [:]
//                movie.errors.fieldErrors.each {
//                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
//                }
//            }
//
//        } else {
//            outp.success = false
//            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"]
//        }
//
//        render( contentType:'text/html', text:(outp as JSON).toString(false) )
//    }

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


    def genre = { GenreDto dto ->
        if( isGet(request) ){
            def flow = getFlow(session)
            dto = extractExistingOrUse(flow, 'genre', dto)

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
            def flow = getFlow(session)
            dto = extractExistingOrUse(flow, 'actor', dto)

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
    def summary = {
        if( isGet(request) ){
            def flow = getFlow(session)

            if(!flow){
                flow = [:]
                session[FLOWKEY] = flow
            }

            def genres = flow.genre?.genres?.collect { Genre.get(it) }
            def actors = flow.actor?.actors?.collect { Actor.get(it) }

            [
                title:flow.details?.title,
                releaseYear:flow.details?.releaseYear,
                storage:"${flow.details?.storageName}-${flow.details?.storageIndex}",
                description:flow.details?.description,
                genres:genres,
                actors:actors
            ]

        } else {
            def flow = getFlow(session)

            // TODO: need to account for pessimistic locking version

            def movie = flow.movieId ? Movie.get(flow.movieId) : new Movie()
            movie.title = flow.details.title
            movie.releaseYear = flow.details.releaseYear
            movie.storage = new Storage( name:flow.details.storageName?.toUpperCase(), index:flow.details.storageIndex )
            movie.description = flow.details.description

            // set poster
            if( flow.poster.posterType == PosterType.URL || flow.poster.posterType == PosterType.FILE ){
                // the content is already in the dto
                def thePoster = new Poster( title:movie.title, content:flow.poster.file )
                if( !thePoster.save(flush:true) ) {
                   thePoster.errors.each {
                       // TODO: these need to be sent to front end!
                        println it
                   }
                } else {
                    movie.poster = thePoster
                }

            } else if(flow.poster.posterType == PosterType.EXISTING){
                movie.poster = Poster.get(flow.poster.posterId)

            }

            flow.genre.genres?.each {
                def gen = Genre.get(it)
                if( !movie.genres.contains(gen) ){
                    movie.addToGenres( gen )
                }
            }

            flow.actor.actors?.each {
                def act = Actor.get(it)
                if( !movie.actors.contains(act) ){
                    movie.addToActors( act )    
                }
            }

            def resp = [success:(movie.save(flush:true) != null)]
            if( movie.hasErrors() ){
                resp.success = false

                resp.errors = [:]
                movie.errors.fieldErrors.each {
                    resp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }
            }

            render( resp as JSON )
        }
    }

    private def extractExistingOrUse( flow, dtoName, dto ){
        def existing = flow[dtoName]
        if( !existing ){
            flow[dtoName] = dto
            existing = dto
        }
        existing
    }

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
