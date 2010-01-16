package com.stehno.mymdb.domain

import grails.test.*

class ActorTests extends GrailsUnitTestCase {

    void testValidation_valid() {
		assertTrue actor(firstName:'Larry',middleName:'Q',lastName:'Stooge').validate()
    }

	void testValidation_firstName_missing() {
		def actor = actor(middleName:'Q',lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	void testValidation_firstName_empty() {
		def actor = actor(firstName:'',middleName:'Q',lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	void testValidation_firstName_too_short() {
		def actor = actor(firstName:'x',middleName:'Q',lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	void testValidation_firstName_too_long() {
		def actor = actor(firstName:('x'*26),middleName:'Q',lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	void testValidation_middleName_missing() {
		def actor = actor(firstName:'Larry',lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	void testValidation_middleName_empty() {
		def actor = actor(firstName:'Larry',middleName:'',lastName:'Stooge')
		assertTrue actor.validate()
    }

	void testValidation_middleName_too_long() {
		def actor = actor(firstName:'Larry',middleName:('x'*26),lastName:'Stooge')
		assertInvalidWithOneError actor
    }

	private void assertInvalidWithOneError( dom ){
		assertFalse dom.validate()
		assertLength 1, dom.errors
	}

	void testValidation_lastName_missing() {
		def actor = actor(firstName:'Larry',middleName:'Q')
		assertInvalidWithOneError actor
    }

	void testValidation_lastName_empty() {
		def actor = actor(firstName:'Larry',middleName:'Q',lastName:'')
		assertInvalidWithOneError actor
    }

	void testValidation_lastName_too_short() {
		def actor = actor(firstName:'Larry',middleName:'Q',lastName:'x')
		assertInvalidWithOneError actor
    }

	void testValidation_lastName_too_long() {
		def actor = actor(firstName:'Larry',middleName:'Q',lastName:('x'*26))
		assertInvalidWithOneError actor
    }

	private Actor actor(params){
		def actor = new Actor(params)
		mockForConstraintsTests Actor.class, [ actor ]
		return actor
	}
}
