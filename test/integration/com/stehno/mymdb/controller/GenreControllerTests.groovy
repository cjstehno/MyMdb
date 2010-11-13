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

    void testList(){
        controller.list()

        assertEquals 'application/json;charset=UTF-8', controller.response.contentType

        def jso = parseJsonResponse()
        assertEquals 'Action', jso.items[0].label
        assertEquals 'Horror', jso.items[1].label
    }

    void testSave(){
        controller.params.name = 'Testing'

        controller.save()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

    void testSave_NoName(){
        controller.save()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Property [name] of class [class com.stehno.mymdb.domain.Genre] cannot be null', jso.errors.name
    }

    void testUpdate(){
        def theGenre = Genre.findByName('Action')
        controller.params.id = theGenre.id
        controller.params.version = theGenre.version
        controller.params.name = 'Excitement'

        controller.update()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

    void testDelete(){
        controller.params.id = Genre.findByName('Action').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def genres = Genre.list()
        assertEquals 1, genres.size()
        assertEquals 'Horror', genres[0].name
    }

    void testDelete_NotFound(){
        controller.delete()

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
