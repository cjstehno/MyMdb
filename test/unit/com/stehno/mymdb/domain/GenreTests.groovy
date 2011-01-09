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

import grails.test.*
import org.junit.Test 

class GenreTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertTrue genre(name:'Testing').validate()
    }

	@Test
    void validation_no_name() {
		def genre = genre(name:null)
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	@Test
    void validation_name_too_long() {
		def genre = genre(name:'xxxxxxxxxxx')
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	@Test
	void validation_name_too_short() {
		def genre = genre(name:'x')
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	@Test
	void validation_name_empty() {
		def genre = genre(name:'')
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	private Genre genre(params){
		def genre = new Genre(params)
		mockForConstraintsTests Genre.class, [ genre ]
		return genre
	}
}
