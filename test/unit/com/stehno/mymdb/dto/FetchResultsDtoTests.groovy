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

package com.stehno.mymdb.dto

import com.stehno.mymdb.ValidationTestCategory
import grails.test.GrailsUnitTestCase
import org.junit.Test

@Mixin(ValidationTestCategory)
class FetchResultsDtoTests extends GrailsUnitTestCase {

    @Test
    void validation_valid(){
        assertValid dto( title:'Some Movie' )
        assertValid dto( title:'Foo', selectedId:'someid', providerId:'provider' )
    }

    @Test
    void validation_title(){
        assertInvalid dto( title:null ), 'title', 'nullable'
        assertInvalid dto( title:'' ), 'title', 'blank'
        assertInvalid dto( title:str(101) ), 'title', 'size.toobig'
    }

    private FetchResultsDto dto(params){
		def dto = new FetchResultsDto(params)
		mockForConstraintsTests FetchResultsDto.class, [ dto ]
		return dto
	}
}
