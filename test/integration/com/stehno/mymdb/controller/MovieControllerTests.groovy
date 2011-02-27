package com.stehno.mymdb.controller

import com.stehno.mymdb.MovieTestFixture
import com.stehno.mymdb.domain.Movie
import grails.converters.JSON
import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieControllerTests extends GrailsUnitTestCase {
	
    def controller
    def fixture = new MovieTestFixture()

    @Before
    void before() {
        super.setUp()
        fixture.before()
		
        controller = new MovieController()
    }

    @Test
    void delete(){
        controller.params.id = fixture.movieId

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def movies = Movie.list()
        assertEquals 1, movies.size()
    }

    @Test
    void delete_NotFound(){
        controller.delete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Movie not found with id null', jso.errors.general

        assertEquals 2, Movie.list().size()
    }
  
    @After
    void after(){
        super.tearDown();
    }
	
    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
