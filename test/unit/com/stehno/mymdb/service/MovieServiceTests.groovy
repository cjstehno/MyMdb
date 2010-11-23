package com.stehno.mymdb.service

import org.junit.Test;

import grails.test.*

import com.stehno.mymdb.domain.Movie

class MovieServiceTests extends GrailsUnitTestCase {
    
	@Test
    void findPoster(){
        def movieInstances = [ 
            new Movie(id:100, poster:'someposter'.getBytes()),
            new Movie(id:300 )
        ]
        mockDomain Movie, movieInstances

        def service = new MovieService()

        assertNull service.findPoster( 200 )
        assertNull service.findPoster( 300 )
        assertNotNull service.findPoster( 100 )
    }
}
