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

import com.stehno.mymdb.ValidationTestCategory
import grails.test.GrailsUnitTestCase
import org.junit.Test

@Mixin(ValidationTestCategory)
class PosterTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertValid poster( title:'Testing', content:content(10) )
        assertValid poster( title:'xx', content:content(10) )
        assertValid poster( title:('x'*100), content:content(10) )
        assertValid poster( title:'Testing', content:content(2) )
        assertValid poster( title:'Testing', content:content(1024000) )
    }

	@Test
    void validation_title() {
		assertInvalid poster( content:content(10) ), 'title', 'nullable'
        assertInvalid poster( title:'', content:content(10) ), 'title', 'blank'
        assertInvalid poster( title:'x', content:content(10) ), 'title', 'size.toosmall'
        assertInvalid poster( title:str(101), content:content(10) ), 'title', 'size.toobig'
    }

	@Test
    void validation_content() {
		assertInvalid poster( title:'Foo' ), 'content', 'nullable'
        assertInvalid poster( title:'Foo', content:content(1024001) ), 'content', 'maxSize.exceeded'
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
