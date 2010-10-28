package com.stehno.mymdb.service

import grails.test.*

import com.stehno.mymdb.domain.Movie

class MovieServiceTests extends GrailsUnitTestCase {
    
    protected void setUp() {
        super.setUp()
    }

    void testFindPoster(){
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

    protected void tearDown() {
        super.tearDown()
    }
}
