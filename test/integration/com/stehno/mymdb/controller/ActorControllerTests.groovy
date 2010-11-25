package com.stehno.mymdb.controller

import grails.test.*
import grails.converters.JSON
import com.stehno.mymdb.domain.Actor

class ActorControllerTests extends GrailsUnitTestCase {

    def controller

    protected void setUp() {
        super.setUp()

        def actorList = [
            new Actor(firstName:'Larry',middleName:'L',lastName:'Stooge'),
            new Actor(firstName:'Moe',middleName:'M',lastName:'Stooge')
        ]
        actorList*.save(flush:true)

        controller = new ActorController()
    }

    void testList(){
        controller.list()

        assertEquals 'application/json;charset=UTF-8', controller.response.contentType

        def jso = parseJsonResponse()
        assertEquals 'Stooge, Larry L', jso.items[0].label
        assertEquals 'Stooge, Moe M', jso.items[1].label
    }

    void testSave(){
        controller.params.firstName = 'Curley'
        controller.params.middleName = 'C'
        controller.params.lastName = 'Stooge'

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
        assertEquals 3, jso.errors.size()
        assertEquals 'Property [lastName] of class [class com.stehno.mymdb.domain.Actor] cannot be null', jso.errors.lastName
    }

    void testUpdate(){
        def theActor = Actor.findByMiddleName('M')
        controller.params.id = theActor.id
        controller.params.version = theActor.version
        controller.params.firstName = 'Moester'
        controller.params.middleName = 'Clarence'
        controller.params.lastName = 'Stooger'

        controller.update()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNull jso.errors
    }

    void testJDelete(){
        controller.params.id = Actor.findByMiddleName('M').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def actors = Actor.list()
        assertEquals 1, actors.size()
        assertEquals 'Larry', actors[0].firstName
    }

    void testDelete_NotFound(){
        controller.delete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Actor not found with id null', jso.errors.general

        assertEquals 2, Actor.list().size()
    }

    protected void tearDown() {
        super.tearDown()
    }

    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}