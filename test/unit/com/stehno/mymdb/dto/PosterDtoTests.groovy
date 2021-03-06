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
class PosterDtoTests extends GrailsUnitTestCase {

    @Test
    void validation_invalid(){
        assertInvalid dto( posterType:null ), 'posterType', 'nullable'
    }

    @Test
    void validation_none(){
        assertValid dto( posterType:PosterType.NONE )
    }

    @Test
    void validation_url(){
        assertValid dto( posterType:PosterType.URL, url:'' )
        assertValid dto( posterType:PosterType.URL, url:'http://www.foo.com' )
        assertInvalid dto( posterType:PosterType.URL, url:'www.foo.com' ), 'url', 'url.invalid'
        assertInvalid dto( posterType:PosterType.URL, url:'sdfgsdfg' ), 'url', 'url.invalid'
    }

    @Test
    void validation_existing(){
        assertValid dto( posterType:PosterType.EXISTING, posterId:123, posterName:'Some poster' )
        assertInvalid dto( posterType:PosterType.EXISTING, posterId:123, posterName:str(101) ), 'posterName', 'size.toobig'
    }

    @Test
    void validation_file(){
        assertValid dto( posterType:PosterType.FILE, file:'somebytes'.getBytes() )
    }

    @Test
    void asMap(){
        assertEquals( [posterType:'NONE' ], dto( posterType:PosterType.NONE ) as Map )
        assertEquals( [posterType:'FILE' ], dto( posterType:PosterType.FILE, content:str(10).bytes ) as Map )
        assertEquals( [posterType:'URL', url:'http://foo.com' ], dto( posterType:PosterType.URL, url:'http://foo.com') as Map )
        assertEquals( [posterType:'EXISTING', posterId:103L, posterName:'Some Movie' ], dto( posterType:PosterType.EXISTING, posterId:103, posterName:'Some Movie' ) as Map )
    }

	private PosterDto dto(params){
		def dto = new PosterDto(params)
		mockForConstraintsTests PosterDto.class, [ dto ]
		return dto
	}
}
