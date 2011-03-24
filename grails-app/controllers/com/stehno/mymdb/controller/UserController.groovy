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

import com.stehno.mymdb.domain.MymdbUser
import grails.converters.JSON
import org.apache.shiro.crypto.hash.Sha512Hash
import com.stehno.mymdb.domain.MymdbRole

/**
 * 
 *
 * @author cjstehno
 */
class UserController {

    static allowedMethods = [ list:'GET', edit:'GET', save:'POST', update:'POST', delete:'POST' ]

    def messageSource

    def list = {
        def data = MymdbUser.list(sort:"username", order:"asc").collect { su-> [ id:su.id, username:su.username, roles:su.roles.collect { r-> r.name }.join(',') ] }

        render( [ items:data ] as JSON )
    }

    def edit = {
        def outp = [:]

        def user = MymdbUser.get(params.id as Long)
        if (!user){
            outp.success = false
            outp.errorMessage = "${message(code: 'default.not.found.message', args: [message(code: 'mymdbUser.label', default: 'User'), params.id])}"

        } else {
            outp.success = true
            outp.data = [ id:user.id, username:user.username ]
        }

        render outp as JSON
    }

    def save = {
        def outp = [:]

        if( params.password1 == params.password2 ){
            params.passwordHash = new Sha512Hash(params.password1).toHex()
        } else {
            outp.success = false
            outp.errors = ['password2':'The two passwords do not match.']

            render outp as JSON
            return
        }

        def user = new MymdbUser(params)
        user.addToRoles MymdbRole.findByName('User')

        outp.success = user.save(flush:true) != null

        if(user.hasErrors()){
            outp.success = false

            outp.errors = [:]
            user.errors.fieldErrors.each {
                outp.errors[it.field] = messageSource.getMessage( it, request.locale)
            }
        }

        render( outp as JSON )
    }

    def update = {
        def outp = [:]

        if( params.password1 == params.password2 ){
            params.passwordHash = new Sha512Hash(params.password1).toHex()
        } else {
            outp.success = false
            outp.errors = ['password2':'The two passwords do not match.']

            render outp as JSON
            return
        }

        def user = MymdbUser.get(params.id)
        if (user) {
            if (params.version) {
                def version = params.version.toLong()
                if (user.version > version) {
                    outp.success = false
                    outp.errors = ['general':'Another user has updated this User while you were editing']

                    render outp as JSON
                    return
                }
            }

            user.properties = params
            if( user.hasErrors() ){
                outp.success = false

                outp.errors = [:]
                user.errors.fieldErrors.each {
                    outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                }

            } else {
                if(user.save(flush:true)){
                    outp.success = true

                } else {
                    outp.success = false

                    outp.errors = [:]
                    user.errors.fieldErrors.each {
                        outp.errors[it.field] = messageSource.getMessage( it, request.locale)
                    }
                }
            }
            
        } else {
            outp.success = false
            outp.errors = ['general':"${message(code: 'default.not.found.message', args: [message(code: 'mymdbUser.label', default: 'User'), params.id])}"]
        }

        render outp as JSON
    }

    def delete = {
        def outp = [:]

        def user = MymdbUser.get(params.items)
        if (user) {
            def isAdmin = user.roles?.find { role-> role.permissions?.contains('admin') }
            if( isAdmin ){
                outp.success = false
                outp.errors = ['general': "${message(code:'user.delete.notadmin', args:[user.username])}"]
                
            } else {
                try {
                    user.delete(flush: true)
                    outp.success = true

                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                    outp.success = false
                    outp.errors = ['general': "${message(code: 'default.not.deleted.message', args: [message(code: 'mymdbUser.label', default: 'User'), params.id])}"]
                }
            }

        } else {
            outp.success = false
            outp.errors = ['general': "${message(code: 'default.not.found.message', args: [message(code: 'mymdbUser.label', default: 'User'), params.id])}"]
        }

        render outp as JSON
    }
}
