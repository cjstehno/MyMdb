package com.stehno.mymdb.domain

import grails.test.*

class GenreTests extends GrailsUnitTestCase {

    void testValidation_valid() {
		assertTrue genre(name:'Testing').validate()
    }

    void testValidation_no_name() {
		def genre = genre(name:null)
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

    void testValidation_name_too_long() {
		def genre = genre(name:'xxxxxxxxxxx')
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	void testValidation_name_too_short() {
		def genre = genre(name:'x')
		assertFalse genre.validate()
		assertLength 1, genre.errors
    }

	void testValidation_name_too_empty() {
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