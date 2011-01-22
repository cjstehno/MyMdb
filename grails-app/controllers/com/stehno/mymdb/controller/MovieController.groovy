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
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.dto.*

class MovieController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def messageSource

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

    private def populateDtos(flow, movie){
        flow.clear()

        flow.movieId = movie.id

        flow.details = new DetailsDto(
            title:movie.title,
            releaseYear:movie.releaseYear,
            storageName:movie.storage.name,
            storageIndex:movie.storage.index,
            description:movie.description
        )

        flow.poster = new PosterDto(
            posterType:PosterType.EXISTING,
            posterId:movie.poster.id,
            posterName:movie.poster.title
        )

        flow.genre = new GenreDto(
            genres:movie.genres.collect { it.id }
        )

        flow.actor = new ActorDto(
            actors:movie.actors.collect { it.id }
        )
    }

    def details = { DetailsDto dto ->
        def flow = getFlow(session)

        if(!flow){
            flow = [:]
            session[FLOWKEY] = flow
        }

        if( isGet(request) ){
            if(params.id) populateDtos( flow, Movie.get(params.id) )

            dto = extractExistingOrUse(flow, 'details', dto)

            dto.title = dto.title ?: flow.fetchResults.title

            render( [ success:true, data:dto ] as JSON )
        } else {
            if( dto.hasErrors() ){
                render( errorResponse(dto,request) as JSON )

            } else {
                flow.details = dto
                render( [ success:true ] as JSON )
            }
        }
    }

    def poster = { PosterDto dto ->
        if( isGet(request) ){
            dto = extractExistingOrUse( getFlow(session), 'poster', dto )

            // TODO: this DTO is not really useful if I have to convert it anyway... reconsider
            def jdto = [:]
            if(dto.posterType) jdto.posterType = dto.posterType.name()
            if(dto.posterId) jdto.posterId = dto.posterId
            if(dto.posterName) jdto.posterName = dto.posterName

            render( contentType:'text/html', text:([ success:true, data:jdto ] as JSON).toString(false) )
        } else {
            if( dto.hasErrors() ){
                render( contentType:'text/html', text:(errorResponse(dto,request) as JSON).toString(false) )

            } else {
                getFlow(session).poster = dto

                if( dto.posterType == PosterType.URL ){
                    dto.file = dto.url.toURL().getBytes()
                }

                render( contentType:'text/html', text:([success:true] as JSON).toString(false) )
            }
        }
    }

    def fetchPosterUrl = {
        def url = params.url

        def flow = getFlow(session)
        if( !flow.poster ){
            flow.poster = new PosterDto()
        }

        flow.poster.posterType = PosterType.URL
        flow.poster.file = url.toURL().getBytes()

        render( [success:true] as JSON)
    }

    def savePosterSelection = {
        def pid = params.id

        def flow = getFlow(session)
        if( !flow.poster ){
            flow.poster = new PosterDto()
        }

        flow.poster.posterType = PosterType.EXISTING
        flow.poster.posterId = pid as Long

        render( [success:true] as JSON)
    }

    def clearSelectedPoster = {
        def flow = getFlow(session)
        flow.remove('poster')

        render( [success:true] as JSON)
    }

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
