package com.stehno.mymdb.controller

import org.junit.Test;

import grails.test.*

import com.stehno.mymdb.domain.Movie

class BrowserControllerTests extends ControllerUnitTestCase {

	@Test
    void index() {	// just assert no errors
        controller.index()
    }

    @Test
    void about() {	// just assert no errors
        controller.about()
    }

	@Test
    void details(){
        def movie = new Movie(id:123,title:'Foozilla')
        mockDomain Movie, [movie]

        controller.params.mid = 123

        def resp = controller.details()

        assertEquals movie, resp.movieInstance
    }
}