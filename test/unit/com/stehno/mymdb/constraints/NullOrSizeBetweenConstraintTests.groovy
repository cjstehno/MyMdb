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

import org.junit.Before
import org.junit.After
import org.junit.Test

class NullOrSizeBetweenConstraintTests extends GroovyTestCase {

    NullOrSizeBetweenConstraint constraint

    @Before
    void before() {
        super.setUp()

        this.constraint = new NullOrSizeBetweenConstraint()
    }

    @Test
    void validate_null(){
        assertFalse constraint.validate(null)
    }

    @Test
    void validate_string(){
        assertFalse constraint.validate('foo')
    }

        private def getConstraint() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }
        v.metaClass.getLog = {-> return log }

        return v
    }

    @After
    void after() {
        super.tearDown()
    }
}
