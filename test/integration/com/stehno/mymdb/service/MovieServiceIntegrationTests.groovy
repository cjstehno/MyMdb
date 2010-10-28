package com.stehno.mymdb.service

import grails.test.*

import com.stehno.mymdb.domain.Movie

class MovieServiceIntegrationTests extends GrailsUnitTestCase {

    MovieService movieService

    protected void setUp() { }

    void testFindMovieTitleLetters(){
        def movies = [
            new Movie(title:'Bravo'),
            new Movie(title:'Alpha'),
            new Movie(title:'Delta')
        ]
        movies*.save( flush:true )

        def letters = movieService.findMovieTitleLetters()

        assertEquals 3, letters.size()
        assertEquals 'A', letters[0]
        assertEquals 'B', letters[1]
        assertEquals 'D', letters[2]
    }

    protected void tearDown() { }
}
