package com.stehno.mymdb.controller

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.stehno.mymdb.service.MovieService 
import grails.test.*

class PosterControllerTests extends ControllerUnitTestCase {
	
	@Before
	void before(){
		super.setUp()
		
		def movieService = mockFor( MovieService )
		
		movieService.demand.findPoster(){ id->
			id == 123 ? [1,3,5,7,9] as byte[] : null
		}
		
		controller.movieService = movieService.createMock()
	}
	
	@Test	
    void image(){
		controller.params.id = '123'
		
		controller.image()
		
		assertEquals( [1,3,5,7,9] as byte[], controller.response.contentAsByteArray )
    }
	
	@Test
	void image_not_found(){
		controller.request.contextPath = '/unit'
		controller.params.id = '357'
		
		controller.image()
		
		assertEquals( '/unit/images/nocover.jpg', controller.response.redirectedUrl )
	}
	
	@After
	void after(){
		super.tearDown()
		
		controller.movieService = null
	}
}
