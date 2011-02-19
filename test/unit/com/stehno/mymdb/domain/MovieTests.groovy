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
package com.stehno.mymdb.domain

import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

 /**
 * 
 *
 * @author cjstehno
 */
class MovieTests extends GrailsUnitTestCase {

    def movie

    @Before
    void before(){
        setUp()

        this.movie = movie( title:'Testing', releaseYear:2000, storage:new Storage(name:'A',index:2), description:'Something interesting', mpaaRating: MpaaRating.UNKNOWN, format:Format.UNKNOWN )
    }

	@Test
    void validation_valid() {
        assertTrue movie.validate()
    }

    @Test
    void validation_title(){
        movie.title = ('x'*101)
        assertFalse movie.validate()

        movie.title = 'x'
        assertTrue movie.validate()

        movie.title = null
        assertFalse movie.validate()
    }

    @Test
    void validation_description(){
        movie.description = null
        assertFalse movie.validate()

        movie.description = ('x'*2001)
        assertFalse movie.validate()
    }

    @Test
    void validation_releaseYear(){
        movie.releaseYear = 1899
        assertFalse movie.validate()

        movie.releaseYear = 2101
        assertFalse movie.validate()
    }

    @Test
    void validation_sites(){
        movie.sites = [ new WebSite( label:'Foo', url:'http://foo.com') ]
        assertTrue movie.validate()
    }

    @After
    void after(){
        tearDown()
        
        this.movie = null
    }

	private def movie(params){
		def m = new Movie(params)
		mockForConstraintsTests Movie.class, [ m ]
		return m
	}
}
