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

import grails.converters.JSON
import grails.test.GrailsUnitTestCase

/**
 *  Base class for movie flow controller integration tests.
 *
 * @author cjstehno
 */
abstract class MovieFlowIntegrationTestBase extends GrailsUnitTestCase {

    protected def controller

    /**
     * Specified the request method and uri to be used in the mock request.
     *
     * @param method the request method
     * @param uri the request uri
     */
    protected def request( method, uri ){
        controller.request.method = method
        controller.request.requestURI = uri
    }

    /**
     * Converts the JSON response string into a Groovy JSON object.
     *
     * @return the populated JSON object
     */
    protected def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
