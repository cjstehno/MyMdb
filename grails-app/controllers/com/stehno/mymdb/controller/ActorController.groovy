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

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie

class ActorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def messageSource

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [actorInstanceList: Actor.list(params), actorInstanceTotal: Actor.count()]
    }

    def all = {
        def actors = Actor.list( sort:'lastName', order:'asc' ).collect {
            [id:it.id, label:it.displayName, count:it.movies?.size()]
        }
        render( [items:actors] as JSON )
    }

    def jedit = {
        def outp = [:]

        def actor = Actor.get(params.id)
        if (!actor){
            outp.success = false
            outp.errorMessage = "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"

        } else {
            outp.success = true
            outp.data = [ id:actor.id, firstName:actor.firstName, middleName:actor.middleName, lastName:actor.lastName, version:actor.version ]
        }

        render outp as JSON
    }

    def jsave = {
        def actor = new Actor(params)
        def outp = [:]

        outp.success = actor.save(flush:true) != null

        if( actor.hasErrors()){
            outp.success = false

            outp.errors = [:]
            actor.errors.fieldErrors.each {
                outp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }

        render outp as JSON
    }

    def jupdate = {
        def outp = [:]

        def actor = Actor.get(params.id)
        if (actor) {
            if (params.version) {
                def version = params.version.toLong()
                if (actor.version > version) {
                    outp.success = false
                    outp.errors = ['general':'Another user has updated this Actor while you were editing']

                    render outp as JSON
                    return
                }
            }

            actor.properties = params
            if (!actor.hasErrors() && actor.save(flush: true)) {
                outp.success = true

            } else {
                outp.success = false
                outp.errors = [:]
                actor.errors.fieldErrors.each {
                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }
            }
        } else {
            outp.success = false
            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"]
        }

        render outp as JSON
    }

    def jdelete = {
        def actor = Actor.get(params.id)

        def outp = [:]

        if (actor) {
            try {
                actor.delete(flush: true)
                outp.success = true

            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                outp.success = false
                outp.errors = ['general': "${message(code: 'default.not.deleted.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"]
            }

        } else {
            outp.success = false
            outp.errors = ['general': "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"]
        }

        render outp as JSON
    }

    def create = {
        def actorInstance = new Actor()
        actorInstance.properties = params
        return [actorInstance: actorInstance]
    }

    def save = {
        def actorInstance = new Actor(params)
        if (actorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'actor.label', default: 'Actor'), actorInstance.id])}"
            redirect(action: "show", id: actorInstance.id)
        }
        else {
            render(view: "create", model: [actorInstance: actorInstance])
        }
    }

    def show = {
        def actorInstance = Actor.get(params.id)
        if (!actorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
            redirect(action: "list")
        }
        else {
            [actorInstance: actorInstance]
        }
    }

    def edit = {
        def actorInstance = Actor.get(params.id)
        if (!actorInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [actorInstance: actorInstance]
        }
    }

    def update = {
        def actorInstance = Actor.get(params.id)
        if (actorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (actorInstance.version > version) {
                    
                    actorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'actor.label', default: 'Actor')] as Object[], "Another user has updated this Actor while you were editing")
                    render(view: "edit", model: [actorInstance: actorInstance])
                    return
                }
            }
            actorInstance.properties = params
            if (!actorInstance.hasErrors() && actorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'actor.label', default: 'Actor'), actorInstance.id])}"
                redirect(action: "show", id: actorInstance.id)
            }
            else {
                render(view: "edit", model: [actorInstance: actorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def actorInstance = Actor.get(params.id)
        if (actorInstance) {
            try {
                actorInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'actor.label', default: 'Actor'), params.id])}"
            redirect(action: "list")
        }
    }
}
