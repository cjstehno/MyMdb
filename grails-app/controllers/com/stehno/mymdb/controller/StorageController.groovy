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

import com.stehno.mymdb.domain.StorageUnit
import grails.converters.JSON

 /**
 * 
 *
 * @author cjstehno
 */
class StorageController {

    static allowedMethods = [ list:'GET', edit:'GET', save:'POST', update:'POST', delete:'POST' ]

    def messageSource

    def list = {
        def data = StorageUnit.list(sort:"name", order:"asc").collect { su->
            [ id:su.id, name:su.name, indexed:su.indexed, capacity:su.capacity, count:0 ]
        }

        render( [ items:data ] as JSON )
    }

    def edit = {
        def outp = [:]

        def storage = StorageUnit.get(params.id as Long)
        if (!storage){
            outp.success = false
            outp.errorMessage = "${message(code: 'default.not.found.message', args: [message(code: 'storageUnit.label', default: 'Storage Unit'), params.id])}"

        } else {
            outp.success = true
            outp.data = [ id:storage.id, name:storage.name, version:storage.version, indexed:storage.indexed, capacity:storage.capacity ]
        }

        render outp as JSON
    }

    def save = {
        def storage = new StorageUnit(params)
        def outp = [:]

        if( storage.indexed && !storage.capacity ){
            outp.success = false
            outp.errors = [ 'capacity':'Indexed storage must have a capacity.' ]
            render( outp as JSON)
            return
        }

        outp.success = storage.save(flush:true) != null

        if(storage.hasErrors()){
            outp.success = false

            outp.errors = [:]
            storage.errors.fieldErrors.each {
                outp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }

        render( outp as JSON )
    }

    def update = {
        def outp = [:]

        def storage = StorageUnit.get(params.id)
        if (storage) {
            if (params.version) {
                def version = params.version.toLong()
                if (storage.version > version) {
                    outp.success = false
                    outp.errors = ['general':'Another user has updated this Storage Unit while you were editing']

                    render outp as JSON
                    return
                }
            }

            storage.properties = params
            if( storage.hasErrors() ){
                outp.success = false

                outp.errors = [:]
                storage.errors.fieldErrors.each {
                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }

            } else {
                if(storage.save(flush:true)){
                    outp.success = true

                } else {
                    outp.success = false

                    outp.errors = [:]
                    storage.errors.fieldErrors.each {
                        outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                    }
                }
            }
            
        } else {
            outp.success = false
            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'storageUnit.label', default: 'Storage Unit'), params.id])}"]
        }

        render outp as JSON
    }

    def delete = {
        def storage = StorageUnit.get(params.items)

        def outp = [:]

        if (storage) {
            try {
                storage.delete(flush: true)
                outp.success = true

            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                outp.success = false
                outp.errors = ['general': "${message(code: 'default.not.deleted.message', args: [message(code: 'storageUnit.label', default: 'Storage Unit'), params.id])}"]
            }

        } else {
            outp.success = false
            outp.errors = ['general': "${message(code: 'default.not.found.message', args: [message(code: 'storageUnit.label', default: 'Storage Unit'), params.id])}"]
        }

        render outp as JSON
    }
}
