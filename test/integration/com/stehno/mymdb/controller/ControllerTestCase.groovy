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
import grails.test.GrailsUnitTestCase

class ControllerTestCase extends GrailsUnitTestCase {

    protected controller

    protected parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }

    protected void assertSuccessful( jso ){
        assertTrue jso.success
        assertNull jso.errors        
    }

    protected void assertFailure( jso, fieldName, errorMsg ){
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals errorMsg, jso.errors[fieldName]
    }
}
