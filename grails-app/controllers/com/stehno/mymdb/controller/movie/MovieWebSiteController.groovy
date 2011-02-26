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

package com.stehno.mymdb.controller.movie

import com.stehno.mymdb.dto.WebSiteDto
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray

/**
 * 
 *
 * @author cjstehno
 */
class MovieWebSiteController extends MovieFlowControllerBase {

    static allowedMethods = [ save:"POST", show:"GET", sites:'POST' ]

    def show = {
        renderSuccess()
    }

    def sites = {
        def dto = movieFlowService.retrieve(WebSiteDto.class)

        if(params.xaction == 'read'){
            def items = dto.sites.collect { k,v -> [label:k, url:v ] }
            render( [ sites:items ] as JSON )

        } else if(params.xaction == 'create'){
            def jso = JSON.parse(params.sites)
            if(jso instanceof JSONArray){
                jso.each {
                    dto.sites[it.label] = it.url
                }
            } else {
                dto.sites[jso.label] = jso.url
            }

            movieFlowService.store(dto)
            
            renderSuccess()
        }
    }

    def save = {
        if(params.finish){
            forward( controller:'movieSummary', action:'save' )
        } else {
            renderSuccess()
        }
    }
}
