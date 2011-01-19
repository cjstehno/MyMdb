/*
   Copyright 2010 Christopher J. Stehno (chris@stehno.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.stehno.mymdb.domain

import grails.test.GrailsUnitTestCase
import org.junit.Test

class PosterTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertTrue poster( title:'Testing', content:content(10) ).validate()
        assertTrue poster( title:'xx', content:content(10) ).validate()
        assertTrue poster( title:('x'*100), content:content(10) ).validate()
        assertTrue poster( title:'Testing', content:content(2) ).validate()
        assertTrue poster( title:'Testing', content:content(1024000) ).validate()
    }

	@Test
    void validation_invalid_notitle() {
		assertFalse poster( content:content(10) ).validate()
    }

    @Test
    void validation_invalid_titletooshort() {
		assertFalse poster( title:'x', content:content(10) ).validate()
    }

    @Test
    void validation_invalid_titletoolong() {
		assertFalse poster( title:('x'*101), content:content(10) ).validate()
    }

	@Test
    void validation_invalid_nocontent() {
		assertFalse poster( title:'Foo' ).validate()
    }

    @Test
    void validation_invalid_contenttoolong() {
		assertFalse poster( title:'Foo', content:content(1024001) ).validate()
    }

	private Poster poster(params){
		def poster = new Poster(params)
		mockForConstraintsTests Poster.class, [ poster ]
		return poster
	}

    private static def content(size){
        ('x'*size).getBytes()
    }
}
