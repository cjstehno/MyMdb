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

import grails.converters.JSON

class MovieController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def messageSource

    def create = {
        def movieInstance = new Movie()
        movieInstance.properties = params
		
		def actorList = Actor.list([sort:'lastName',order:'asc'])
		def tabSets = tabbify(actorList, 30)
		
        return [movieInstance: movieInstance, genres:Genre.list([sort:'name',order:'asc']), tabRange:(0..<tabSets.size()), tabs:tabSets]
    }
	
	def tabbify( theList, groupCnt ){
		def tabCount = (int)(theList.size() / groupCnt)
		tabCount = (theList.size() % groupCnt) > 0 ? tabCount+1 : tabCount
		
		def tabSets = []
		tabCount.times {
			def end = (it*groupCnt+(groupCnt-1)) >= theList.size() ? theList.size()-1 : (it*groupCnt+(groupCnt-1))
			def sub = theList[it*groupCnt..end]
			tabSets.add sub
		}
		
		return tabSets
	}

    def save = { // done
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

//    def show = {
//        def movieInstance = Movie.get(params.id)
//        if (!movieInstance) {
//            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"
//            redirect(action: "list")
//        }
//        else {
//            [movieInstance: movieInstance]
//        }
//    }

    def edit = {
        def movieInstance = Movie.get(params.id)
        if (!movieInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"
            redirect(action: "list")
        }
        else {
			def tabSet = tabbify( Actor.list([sort:'lastName',order:'asc']) ,30)
            return [movieInstance: movieInstance, genres:Genre.list([sort:'name',order:'asc']), tabRange:(0..<tabSet.size()), tabs:tabSet]
        }
    }

    def update = {
        def movieInstance = Movie.get(params.id)
        if (movieInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (movieInstance.version > version) {
                    
                    movieInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code:'movie.label', default:'Movie')] as Object[], "Another user has updated this Movie while you were editing")
                    render(view: "edit", model: [movieInstance: movieInstance])
                    return
                }
            }
			
			// protect the exting image data
			def posterHolder = null
			if(params.poster.bytes.length == 0){
				posterHolder = movieInstance.poster
			}
			
            movieInstance.properties = params
			if(posterHolder){
				movieInstance.poster = posterHolder
			}
			
			if(movieInstance.storage){
				movieInstance.storage.name = movieInstance.storage.name.toUpperCase()
			}			
			
            if (!movieInstance.hasErrors() && movieInstance.save(flush: true)) {
                movieInstance.setTags( params.tags?.split().toList() )
                
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'movie.label', default: 'Movie'), movieInstance.id])}"
                redirect(action: "show", id: movieInstance.id)
            }
            else {
                render(view: "edit", model: [movieInstance: movieInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'movie.label', default: 'Movie'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def delete = { // done
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
}
