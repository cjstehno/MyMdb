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

import grails.test.GrailsUnitTestCase
import org.junit.Test

class PosterDtoTests extends GrailsUnitTestCase {

    @Test
    void asMap(){
        def dto = dto(posterId:100, posterName:'Foo')
        def map = dto as Map

        assertEquals dto.posterType.name(), map.posterType
        assertEquals dto.url, map.url
        assertEquals dto.posterId, map.posterId
        assertEquals dto.posterName, map.posterName
    }

	private PosterDto dto(params){
		def dto = new PosterDto(params)
		mockForConstraintsTests PosterDto.class, [ dto ]
		return dto
	}
}
