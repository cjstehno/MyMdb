package com.stehno.mymdb.domain

import grails.test.*

class GenreTests extends GrailsUnitTestCase {

    void testValidation_valid() {
		def genre = new Genre(name:'Testing')
		mockForConstraintsTests Genre.class, [ genre ]
		
		if( !genre.validate() ) fail 'Genre did not pass validation!'
    }

    void testValidation_invalid_name() {
		def genre = new Genre()
		mockForConstraintsTests Genre.class, [ genre ]
		
		if(genre.validate()){
			fail 'Genre passed validation with invalid state!'
		} else {
			assertLength 1, genre.errors
		}
    }	
}
