package com.stehno.mymdb.domain

import grails.test.*

class ActorTests extends GrailsUnitTestCase {

    void testValidation_valid() {
		assertTrue actor(firstName:'Larry',middleName:'Q',lastName:'Stooge').validate()
    }

	void testValidation_firstName_empty() {
		def actor = actor(middleName:'Q',lastName:'Stooge')
		assertFalse actor.validate()
		assertLength 1, actor.errors
    }

	private Actor actor(params){
		def actor = new Actor(params)
		mockForConstraintsTests Actor.class, [ actor ]
		return actor
	}
}
