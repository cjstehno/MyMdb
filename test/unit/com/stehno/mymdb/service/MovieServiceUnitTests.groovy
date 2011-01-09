package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Poster
import grails.test.GrailsUnitTestCase
import org.junit.Test

class MovieServiceUnitTests extends GrailsUnitTestCase {
    
	@Test
    void findPoster(){
        def movieInstances = [ 
            new Movie(id:100, poster:new Poster( title:'Foo', content:'someposter'.getBytes() )),
            new Movie(id:300 )
        ]
        mockDomain Movie, movieInstances

        def service = new MovieService()

        assertNull service.findPoster( 200 )
        assertNull service.findPoster( 300 )
        assertNotNull service.findPoster( 100 )
    }
}
