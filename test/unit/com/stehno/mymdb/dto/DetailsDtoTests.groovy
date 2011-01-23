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

class DetailsDtoTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertTrue dto(title:'Testing', releaseYear:2000).validate()
    }

	@Test
    void validation_name() {
		assertFalse dto(title:null, releaseYear:2000).validate()
        assertFalse dto(title:'', releaseYear:2000).validate()
        assertTrue dto(title:'x', releaseYear:2000).validate()
        assertTrue dto(title:('x'*100), releaseYear:2000).validate()
        assertFalse dto(title:('x'*101), releaseYear:2000).validate()
    }

    @Test
    void validation_releaseYear(){
        assertFalse dto(title:'It!', releaseYear:null).validate();
        assertFalse dto(title:'It!', releaseYear:1899).validate();
        assertTrue dto(title:'It!', releaseYear:1900).validate();
        assertTrue dto(title:'It!', releaseYear:2100).validate();
        assertFalse dto(title:'It!', releaseYear:2101).validate();
    }

    @Test
    void validation_description(){
        assertTrue dto(title:'Test', releaseYear:2000, description:null).validate()
        assertTrue dto(title:'Test', releaseYear:2000, description:'').validate()
        assertTrue dto(title:'Test', releaseYear:2000, description:('x'*2000)).validate()
        assertFalse dto(title:'Test', releaseYear:2000, description:('x'*2001)).validate()
    }

    @Test
    void validation_storageName(){
        def title = 'Cool!'
        def year = 2000

        assertTrue dto(title:title, releaseYear:year, storageName:null).validate()
        assertTrue dto(title:title, releaseYear:year, storageName:'').validate()
        assertTrue dto(title:title, releaseYear:year, storageName:'x').validate()
        assertTrue dto(title:title, releaseYear:year, storageName:('x'*40)).validate()
        assertFalse dto(title:title, releaseYear:year, storageName:('x'*41)).validate()
    }

    @Test
    void validation_storageIndex(){
        def title = 'Cool!'
        def year = 2000

        assertTrue dto(title:title, releaseYear:year, storageIndex:null).validate()
        assertFalse dto(title:title, releaseYear:year, storageIndex:(-1)).validate()
        assertTrue dto(title:title, releaseYear:year, storageIndex:0).validate()
        assertTrue dto(title:title, releaseYear:year, storageIndex:1234).validate()
    }

	private DetailsDto dto(params){
		def dto = new DetailsDto(params)
		mockForConstraintsTests DetailsDto.class, [ dto ]
		return dto
	}
}
