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

import com.stehno.mymdb.domain.Actor

class ActorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def messageSource

    def list = {
        def actors = Actor.list( sort:'lastName', order:'asc' ).collect {
            [id:it.id, label:it.displayName, count:it.movies?.size()]
        }
        render( [items:actors] as JSON )
    }

    def edit = {
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

    def save = {
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

    def update = {
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

    def delete = {
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
}
