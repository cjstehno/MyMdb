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

package com.stehno.mymdb.func

import functionaltestplugin.FunctionalTestCase
import grails.converters.JSON
import org.junit.Test

/**
 * 
 *
 * @author cjstehno
 */
class ApiControllerFunctionalTests extends FunctionalTestCase {

    @Test
    void login(){
        post('/api/login'){
            username = 'admin'
            password = 'admin'
        }

        assertContentType 'application/json'

        def jso = parseJson()
        assertTrue jso.success
    }

    private parseJson(){
        JSON.parse( new StringReader(response.contentAsString) )
    }
}
