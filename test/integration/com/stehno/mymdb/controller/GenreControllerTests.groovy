package com.stehno.mymdb.controller

import grails.test.*
import grails.converters.JSON
import com.stehno.mymdb.domain.Genre

class GenreControllerTests extends GrailsUnitTestCase {

    def controller

    protected void setUp() {
        super.setUp()

        def genreList = [
            new Genre(name:'Horror'),
            new Genre(name:'Action')
        ]
        genreList*.save(flush:true)

        controller = new GenreController()
    }

    void testAll(){
        controller.all()

        assertEquals 'application/json;charset=UTF-8', controller.response.contentType

        def jso = parseJsonResponse()
        assertEquals 'Action', jso.items[0].label
        assertEquals 'Horror', jso.items[1].label
    }

    void testJSave(){
        controller.params.name = 'Testing'

        controller.jsave()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

    void testJSave_NoName(){
        controller.jsave()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Property [name] of class [class com.stehno.mymdb.domain.Genre] cannot be null', jso.errors.name
    }

    void testJUpdate(){
        def theGenre = Genre.findByName('Action')
        controller.params.id = theGenre.id
        controller.params.version = theGenre.version
        controller.params.name = 'Excitement'

        controller.jupdate()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

    void testJDelete(){
        controller.params.id = Genre.findByName('Action').id

        controller.jdelete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def genres = Genre.list()
        assertEquals 1, genres.size()
        assertEquals 'Horror', genres[0].name
    }

    void testJDelete_NotFound(){
        controller.jdelete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Genre not found with id null', jso.errors.general

        assertEquals 2, Genre.list().size()
    }

    protected void tearDown() {
        super.tearDown()
    }

    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
