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
class StorageTests extends GrailsUnitTestCase {

    @Test
    void validate_name(){
        assertValid storage( name:'A', index:32 )
        assertInvalid storage( name:null, index:32 ), 'name', 'nullable'
        assertInvalid storage( name:'', index:32 ), 'name', 'blank'
        assertValid storage( name:str(20), index:32 )
        assertInvalid storage( name:str(21), index:32 ), 'name', 'size.toobig'
    }

    @Test
    void validate_index(){
        assertInvalid storage( name:'x', index:0 ), 'index', 'range.toosmall'
        assertInvalid storage( name:'x', index:121 ), 'index', 'range.toobig'
        assertValid storage( name:'x', index:120 )
        assertInvalid storage( name:'x', index:null ), 'index', 'range.toosmall'
    }

	private Storage storage(params){
		def sto = new Storage(params)
		mockForConstraintsTests Storage.class, [ sto ]
		return sto
	}
}
