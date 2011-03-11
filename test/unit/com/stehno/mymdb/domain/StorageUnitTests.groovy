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

package com.stehno.mymdb.domain

import com.stehno.mymdb.ValidationTestCategory
import grails.test.GrailsUnitTestCase
import org.junit.Test

@Mixin(ValidationTestCategory)
class StorageUnitTests extends GrailsUnitTestCase {

    @Test
    void validate_name(){
        def (indexed, capacity) = [true, 100]
        assertValid storage( name:'A', indexed:indexed, capacity:capacity )
        assertInvalid storage( name:'', indexed:indexed, capacity:capacity), 'name', 'blank'
        assertInvalid storage( name:null, indexed:indexed, capacity:capacity), 'name', 'nullable'
        assertInvalid storage( name:str(21), indexed:indexed, capacity:capacity), 'name', 'size.toobig'
    }

    @Test
    void validate_capacity(){
        def (indexed, name) = [false, 'X']
        assertValid storage( name:name, indexed:indexed, capacity:0 )
        assertValid storage( name:name, indexed:indexed, capacity:1000 )
        assertInvalid storage( name:name, indexed:indexed, capacity:-1 ), 'capacity', 'min.notmet'
    }

	private StorageUnit storage(params){
		def sto = new StorageUnit(params)
		mockForConstraintsTests StorageUnit.class, [ sto ]
		return sto
	}
}
