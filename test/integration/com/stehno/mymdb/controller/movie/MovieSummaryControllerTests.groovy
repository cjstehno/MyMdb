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

import com.stehno.mymdb.service.MovieFlowService
import org.junit.Test
import org.junit.Before
import org.junit.After

class MovieSummaryControllerTests extends MovieFlowIntegrationTestBase {

    @Before
    void before(){
        super.setUp()

        controller = new MovieSummaryController()
        controller.movieFlowService = new MovieFlowService()
    }

    @Test
    void show_no_data(){
        request('GET', '/movie/summary')

        def model = controller.show()

        assertNotNull model
        assertNull model.title
        assertNull model.releaseYear
        assertEquals 'null-null', model.storage
        assertNull model.description
        assertNull model.genres
        assertNull model.actors
    }

    @After
    void after(){
        super.tearDown()
    }
}
