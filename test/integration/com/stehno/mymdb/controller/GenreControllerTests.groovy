package com.stehno.mymdb.controller

import grails.test.*
import grails.converters.JSON
import com.stehno.mymdb.domain.Genre

class GenreControllerTests extends GrailsUnitTestCase {

    def controller

    protected void setUp() {
        super.setUp()

        def genreList = [
            new Genre(id:101, name:'Horror'),
            new Genre(id:102, name:'Action')
        ]
        genreList*.save(flush:true)

        controller = new GenreController()
    }

    void testAll(){
        controller.all()

        assertEquals 'application/json;charset=UTF-8', controller.response.contentType

        String contentStr = controller.response.contentAsString
        assertEquals '{"items":[{"cid":2,"lbl":"Action"},{"cid":1,"lbl":"Horror"}]}', contentStr

//        def jso = JSON.pase( contentStr ) -- FIXME: not sure why this does not work
    }

    protected void tearDown() {
        super.tearDown()
    }
}
