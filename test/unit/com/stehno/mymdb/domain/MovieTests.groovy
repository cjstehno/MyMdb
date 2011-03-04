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

import org.junit.After
import org.junit.Before
import org.junit.Test

 /**
 * 
 *
 * @author cjstehno
 */
class MovieTests extends DomainTestCase {

    def movie

    @Before
    void before(){
        setUp()

        this.movie = movie(
            title:'Testing',
            releaseYear:2000,
            storage:new Storage(name:'A',index:2),
            description:'Something interesting',
            mpaaRating:MpaaRating.UNKNOWN,
            format:Format.UNKNOWN
        )
    }

	@Test
    void validation_valid() {
        assertValid movie
    }

    @Test
    void validation_title(){
        movie.title = ('x'*101)
        assertInvalid movie, 'title', 'size.toobig'

        movie.title = 'x'
        assertValid movie

        movie.title = null
        assertInvalid movie, 'title', 'nullable'
    }

    @Test
    void validation_description(){
        movie.description = null
        assertInvalid movie, 'description', 'nullable'

        movie.description = ('x'*2001)
        assertInvalid movie, 'description', 'size.toobig'
    }

    @Test
    void validation_releaseYear(){
        movie.releaseYear = 1899
        assertInvalid movie, 'releaseYear', 'range.toosmall'

        movie.releaseYear = 2101
        assertInvalid movie, 'releaseYear', 'range.toobig'
    }

    @Test
    void validation_sites(){
        movie.sites = [ new WebSite( label:'Foo', url:'http://foo.com') ]
        assertValid movie
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
