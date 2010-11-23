package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie 
import grails.test.GrailsUnitTestCase
import java.io.StringReader 

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import grails.converters.JSON

import com.stehno.mymdb.domain.Genre

class MovieControllerTests extends GrailsUnitTestCase {
	
	def controller

	@Before
	void before() {
		super.setUp()
		
		def genreList = [
			new Genre(name:'Horror'),
			new Genre(name:'Action')
		]
		genreList*.save(flush:true)
		
		controller = new MovieController()
	}
	
	@Test
	void toLongArray(){
		def ary = controller.toLongArray('1,3,8')
		assertEquals 3, ary.size()
		assertEquals( 1 as Long,  ary[0])
		assertEquals( 3 as Long,  ary[1])
		assertEquals( 8 as Long,  ary[2])
	}
	
	@Test
	void toLongArray_null(){
		def ary = controller.toLongArray(null)
		assertNull ary
	}
	
	@Test
    void save(){
        controller.params.title = 'Testing'
		controller.params.releaseYear = '2010'
		controller.params.description = 'Some text.'
		controller.params['storage.name'] = 'A'
		controller.params['storage.index'] = '1'
		controller.params.poster = ''
		
//		def genre = Genre.findByName('Horror')
//		controller.request.addParameter 'genres', [ genre.id as String ] as String[]
//		
////		controller.params.actors = ''

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
		
//		assertEquals 1, movie.genres.size()
//		assertEquals 'Horror', movie.genres[0].name
    }
	
	@After
	void after(){
		super.tearDown();
	}
	
	private def parseJsonResponse(){
		JSON.parse( new StringReader(controller.response.contentAsString) )
	}
}
