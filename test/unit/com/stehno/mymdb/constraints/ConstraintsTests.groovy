/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.mymdb.constraints

import org.junit.Test

/**
 * 
 *
 * @author cjstehno
 */
class ConstraintsTests extends GroovyTestCase {

    @Test
    void nullOrBetween(){
        def range = 2..10
        def code = 'not.nullorbetween'
        assertValid Constraints.nullOrBetween(range), null
        assertValid Constraints.nullOrBetween(range), ''
        assertInvalid Constraints.nullOrBetween(range), 'x', code
        assertValid Constraints.nullOrBetween(range), str(2)
        assertValid Constraints.nullOrBetween(range), str(3)
        assertValid Constraints.nullOrBetween(range), str(10)
        assertInvalid Constraints.nullOrBetween(range), str(11), code
    }

    @Test
    void notNullAndBetween(){
        def range = 2..10
        def code = 'not.notnullandbetween'
        assertInvalid Constraints.notNullAndBetween(range), null, code
        assertInvalid Constraints.notNullAndBetween(range), '', code
        assertInvalid Constraints.notNullAndBetween(range), 'x', code
        assertValid Constraints.notNullAndBetween(range), str(2)
        assertValid Constraints.notNullAndBetween(range), str(3)
        assertValid Constraints.notNullAndBetween(range), str(10)
        assertInvalid Constraints.notNullAndBetween(range), str(11), code
    }

    private String str( n ){
        'x'*n
    }

    private void assertValid( constr, value ){
        assertTrue validate(constr,value)
    }

    private void assertInvalid( constr, value, code ){
        assertEquals code, validate(constr,value)
    }

    private validate( constr, value ){
        constr.validator(value)
    }
}
