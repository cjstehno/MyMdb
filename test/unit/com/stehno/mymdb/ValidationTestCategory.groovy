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

package com.stehno.mymdb

import grails.test.GrailsUnitTestCase

 /**
 * Category with useful methods for testing constraint validation.
 *
 * @author cjstehno
 */
@Category(GrailsUnitTestCase)
class ValidationTestCategory {

    /**
     * Asserts that the constraints are invalid for the given validatable object.
     *
     * @param dom the validatable object
     * @param fieldName the name of the invalid field
     * @param code the error code expected
     */
	void assertInvalid( dom, fieldName, code ){
		assertFalse dom.validate()
		assertLength 1, dom.errors
        assertEquals code, dom.errors.getFieldError(fieldName).code
	}

    /**
     * Creates a string of the specified length.
     *
     * @param n length of string
     * @return a string of length n
     */
    String str( n ){ 'x'*n }

    /**
     * Asserts that the constraints are valid for the given validatable object.
     *
     * @param dom
     */
    void assertValid( dom ){
        assertTrue dom.validate()
    }
}
