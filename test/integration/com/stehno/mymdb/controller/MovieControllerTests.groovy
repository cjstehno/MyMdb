package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie 
import grails.test.GrailsUnitTestCase
import java.io.StringReader 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import grails.converters.JSON

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Storage

class MovieControllerTests extends GrailsUnitTestCase {
	
    def controller

    @Before
    void before() {
        super.setUp()
		
        [ new Genre(name:'Horror'), new Genre(name:'Action') ]*.save(flush:true)
		
        def mov = new Movie(title:'Some Movie', releaseYear:2000, description:'A movie.')
        mov.storage = new Storage(name:'A', index:100)
        mov.poster = ''
        mov.save(flush:true)
		
        controller = new MovieController()
    }
	
    @Test
    void edit(){
        def movie = Movie.findByTitle('Some Movie')
        controller.params.id = movie.id
		
        controller.edit()
		
        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
		
        def data = jso.data
        assertNotNull data
        assertEquals movie.id, data.id
        assertEquals movie.version, data.version
        assertEquals 'Some Movie', data.title
        assertEquals 2000, data.releaseYear
        assertEquals 'A movie.', data.description
        assertEquals 'A', data['storage.name']
        assertEquals 100, data['storage.index']
    }
	
    @Test
    void save(){
        controller.params.title = 'Testing'
        controller.params.releaseYear = '2010'
        controller.params.description = 'Some text.'
        controller.params['storage.name'] = 'a'
        controller.params['storage.index'] = '1'
        controller.params.poster = ''
		
        def genre = Genre.findByName('Horror')
        controller.params.genres = genre.id as String

        //		controller.params.actors = ''

        controller.save()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
		
        def movie = Movie.findByTitle('Testing')
        assertNotNull movie
        assertEquals 'Testing', movie.title
        assertEquals 2010, movie.releaseYear
        assertEquals 'Some text.', movie.description
        assertEquals 'A', movie.storage.name
        assertEquals 1, movie.storage.index
		
        assertEquals 1, movie.genres.size()
        assertEquals 'Horror', movie.genres.iterator().next().name
    }
	
    @Test
    void update(){
        controller.params.id = Movie.findByTitle('Some Movie').id
        controller.params.title = 'Foovie'
        controller.params.releaseYear = '2011'
        controller.params.description = 'Some more text.'
        controller.params['storage.name'] = 'A'
        controller.params['storage.index'] = '2'
        controller.params.poster = ''
		
        def genre = Genre.findByName('Action')
        controller.params.genres = genre.id as String
		
        controller.update()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
		
        assertNull Movie.findByTitle('Some Movie')
		
        def movie = Movie.findByTitle('Foovie')
        assertNotNull movie
        assertEquals 'Foovie', movie.title
        assertEquals 2011, movie.releaseYear
        assertEquals 'Some more text.', movie.description
        assertEquals 'A', movie.storage.name
        assertEquals 2, movie.storage.index
		
        assertEquals 1, movie.genres.size()
        assertEquals 'Action', movie.genres.iterator().next().name
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

    @Test
    void fetchResults_GET(){
        controller.request.method = 'GET'

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data
        
        assertNotNull controller.session['movie.flow']
    }

    @Test
    void fetchResults_POST(){
        controller.session['movie.flow'] = [:]
        
        controller.request.method = 'POST'
        controller.params.title = 'Some title'

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def flow = controller.session['movie.flow']
        assertNotNull flow
        assertNotNull flow.fetchResults
        assertEquals 'Some title', flow.fetchResults.title
    }

    @Test
    void fetchResults_POST_Errors(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'POST'
        controller.params.title = ''

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
    }

    @After
    void after(){
        super.tearDown();
    }
	
    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
