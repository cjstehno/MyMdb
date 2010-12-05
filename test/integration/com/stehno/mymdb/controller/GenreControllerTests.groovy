package com.stehno.mymdb.controller

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import grails.test.*
import grails.converters.JSON
import com.stehno.mymdb.domain.Genre

class GenreControllerTests extends GrailsUnitTestCase {

    def controller

	@Before
    void before() {
        super.setUp()

        def genreList = [
            new Genre(name:'Horror'),
            new Genre(name:'Action')
        ]
        genreList*.save(flush:true)

        controller = new GenreController()
    }

	@Test
    void list(){
        controller.list()

        assertEquals 'application/json;charset=UTF-8', controller.response.contentType

        def jso = parseJsonResponse()
        assertEquals 'Action', jso.items[0].label
        assertEquals 'Horror', jso.items[1].label
    }

	@Test
    void save(){
        controller.params.name = 'Testing'

        controller.save()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

	@Test
    void save_NoName(){
        controller.save()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Property [name] of class [class com.stehno.mymdb.domain.Genre] cannot be null', jso.errors.name
    }

	@Test
    void update(){
        def theGenre = Genre.findByName('Action')
        controller.params.id = theGenre.id
        controller.params.version = theGenre.version
        controller.params.name = 'Excitement'

        controller.update()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

	@Test
    void delete(){
        controller.params.id = Genre.findByName('Action').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def genres = Genre.list()
        assertEquals 1, genres.size()
        assertEquals 'Horror', genres[0].name
    }

	@Test
    void delete_NotFound(){
        controller.delete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Genre not found with id null', jso.errors.general

        assertEquals 2, Genre.list().size()
    }

	@After
    void after() {
        super.tearDown()
    }

    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
