package com.stehno.mymdb.domain

import org.junit.Test

class ActorTests extends DomainTestCase {

    @Test
    void validation_valid() {
		assertValid actor(firstName:'Larry',middleName:'Q',lastName:'Stooge')
    }

    @Test
	void validation_single_name_actor(){
		assertValid actor(firstName:'',middleName:'',lastName:'Cher')
	}

    @Test
	void validation_firstName_missing() {
		assertValid actor(firstName:'',middleName:'Q',lastName:'Stooge')
    }

    @Test
	void validation_middleName_missing() {
		assertValid actor(firstName:'Larry',middleName:'',lastName:'Stooge')
    }

    @Test
	void validation_firstName_too_long() {
		assertInvalid actor(firstName:('x'*126),middleName:'Q',lastName:'Stooge'), 'firstName', 'not.nullorbetween'
    }

    @Test
	void validation_lastName_empty() {
		assertInvalid actor(firstName:'Larry',middleName:'Q',lastName:''), 'lastName', 'not.notnullandbetween'
    }


    @Test
	void validation_middleName_too_long() {
		assertInvalid actor(firstName:'Larry',middleName:('x'*26),lastName:'Stooge'), 'middleName', 'not.nullorbetween'
    }

    @Test
	void validation_lastName_short() {
		def actor = actor(firstName:'Larry',middleName:'Q',lastName:'x')
		assertTrue actor.validate()
    }

    @Test
	void validation_lastName_too_long() {
		assertInvalid actor(firstName:'Larry',middleName:'Q',lastName:('x'*26)), 'lastName', 'not.notnullandbetween'
    }

    @Test
    void displayName(){
        def actor = new Actor( firstName:'Abe', middleName:'Arthur', lastName:'Abraham' )
        assertEquals 'Abraham, Abe Arthur', actor.displayName

        actor.middleName = ''
        assertEquals 'Abraham, Abe', actor.displayName

        actor.firstName = ''
        assertEquals 'Abraham', actor.displayName

        actor.middleName = 'Q'
        assertEquals 'Abraham, Q', actor.displayName
    }

    @Test
    void fullName(){
        def actor = new Actor( firstName:'Abe', middleName:'Arthur', lastName:'Abraham' )
        assertEquals 'Abe Arthur Abraham', actor.fullName

        actor.middleName = ''
        assertEquals 'Abe Abraham', actor.fullName

        actor.firstName = ''
        assertEquals 'Abraham', actor.fullName

        actor.middleName = 'Q'
        assertEquals 'Q Abraham', actor.fullName
    }

	private Actor actor(params){
		def act = new Actor(params)
        mockForConstraintsTests Actor.class, [ act ]
        return act
	}
}
