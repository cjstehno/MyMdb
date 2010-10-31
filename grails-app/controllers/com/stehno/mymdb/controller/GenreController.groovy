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

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [genreInstanceList: Genre.list(params), genreInstanceTotal: Genre.count()]
    }

    def all = {
        def genres = Genre.list( sort:'name', order:'asc' )
        render(contentType:"text/json") {
            items = array {
                for(g in genres) {
                    item cid:g.id, lbl:g.name, cnt:g.movies.size()
                }
            }
        }
    }

    def create = {
        def genreInstance = new Genre()
        genreInstance.properties = params
        return [genreInstance: genreInstance]
    }

    def save = {
        def genreInstance = new Genre(params)
        if (genreInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'genre.label', default: 'Genre'), genreInstance.id])}"
            redirect(action: "show", id: genreInstance.id)
        }
        else {
            render(view: "create", model: [genreInstance: genreInstance])
        }
    }

    def show = {
        def genreInstance = Genre.get(params.id)
        if (!genreInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
            redirect(action: "list")
        }
        else {
            [genreInstance: genreInstance]
        }
    }

    def edit = {
        def genreInstance = Genre.get(params.id)
        if (!genreInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [genreInstance: genreInstance]
        }
    }

    def update = {
        def genreInstance = Genre.get(params.id)
        if (genreInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (genreInstance.version > version) {

                    genreInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'genre.label', default: 'Genre')] as Object[], "Another user has updated this Genre while you were editing")
                    render(view: "edit", model: [genreInstance: genreInstance])
                    return
                }
            }
            genreInstance.properties = params
            if (!genreInstance.hasErrors() && genreInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'genre.label', default: 'Genre'), genreInstance.id])}"
                redirect(action: "show", id: genreInstance.id)
            }
            else {
                render(view: "edit", model: [genreInstance: genreInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def genreInstance = Genre.get(params.id)
        if (genreInstance) {
            try {
                genreInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'genre.label', default: 'Genre'), params.id])}"
            redirect(action: "list")
        }
    }
}
