package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import grails.converters.JSON
import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieControllerTests extends GrailsUnitTestCase {
	
    def controller

    @Before
    void before() {
        super.setUp()
		
        [ new Genre(name:'Horror'), new Genre(name:'Action') ]*.save(flush:true)
		
        def mov = new Movie(title:'Some Movie', releaseYear:2000, description:'A movie.')
        mov.storage = new Storage(name:'A', index:100)
        mov.poster = null
        mov.save(flush:true)
		
        controller = new MovieController()
    }

    @Test
    void delete(){
        controller.params.id = Movie.findByTitle('Some Movie').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def movies = Movie.list()
        assertEquals 0, movies.size()
    }

    @Test
    void delete_NotFound(){
        controller.delete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Movie not found with id null', jso.errors.general

        assertEquals 1, Movie.list().size()
    }
  
    @After
    void after(){
        super.tearDown();
    }
	
    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
