package com.stehno.mymdb.controller

import grails.test.*

import com.stehno.mymdb.domain.Movie

class BrowserControllerTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    // just assert no errors
    void testIndex() {
        controller.index()
    }

    // just assert no errors
    void testAbout() {
        controller.about()
    }

    void testDetails(){
        def movie = new Movie(id:123,title:'Foozilla')
        mockDomain Movie, [movie]

        controller.params.mid = 123

        def resp = controller.details()

        assertEquals movie, resp.movieInstance
    }

    protected void tearDown() {
        super.tearDown()
    }
}
