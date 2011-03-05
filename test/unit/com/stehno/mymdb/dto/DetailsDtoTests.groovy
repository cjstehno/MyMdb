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
import com.stehno.mymdb.domain.Format
import grails.test.GrailsUnitTestCase
import org.junit.Test
import com.stehno.mymdb.domain.MpaaRating
import org.junit.Ignore

@Mixin(ValidationTestCategory)
class DetailsDtoTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertValid dto( title:'Testing', releaseYear:2000, storageName:'X', storageIndex:12 )
    }

	@Test
    void validation_title() {
        def (releaseYear, storageName, storageIndex) = [ 2000, 'X', 42 ]

		assertInvalid dto(title:null, releaseYear:releaseYear, storageName:storageName, storageIndex:storageIndex ), 'title', 'nullable'
        assertInvalid dto(title:'', releaseYear:releaseYear, storageName:storageName, storageIndex:storageIndex ), 'title', 'blank'
        assertValid dto(title:'x', releaseYear:releaseYear, storageName:storageName, storageIndex:storageIndex )
        assertValid dto(title:str(100), releaseYear:releaseYear, storageName:storageName, storageIndex:storageIndex )
        assertInvalid dto(title:str(101), releaseYear:releaseYear, storageName:storageName, storageIndex:storageIndex ), 'title', 'size.toobig'
    }

    @Test
    void validation_releaseYear(){
        def (title, storageName, storageIndex) = [ 'The Thing', 'X', 42 ]

        assertValid dto( releaseYear:null, title:title, storageName:storageName, storageIndex:storageIndex )
        assertInvalid dto( releaseYear:1899, title:title, storageName:storageName, storageIndex:storageIndex ), 'releaseYear', 'range.toosmall'
        assertValid dto( releaseYear:1900, title:title, storageName:storageName, storageIndex:storageIndex )
        assertValid dto( releaseYear:2100, title:title, storageName:storageName, storageIndex:storageIndex )
        assertInvalid dto( releaseYear:2101, title:title, storageName:storageName, storageIndex:storageIndex ), 'releaseYear', 'range.toobig'
    }

    @Test
    void validation_description(){
        def (title, storageName, storageIndex) = [ 'The Thing', 'X', 42 ]

        assertValid dto( description:null, title:title, storageName:storageName, storageIndex:storageIndex )
        assertValid dto( description:'', title:title, storageName:storageName, storageIndex:storageIndex )
        assertValid dto( description:str(2000), title:title, storageName:storageName, storageIndex:storageIndex )
        assertInvalid dto( description:str(2001), title:title, storageName:storageName, storageIndex:storageIndex ), 'description', 'maxSize.exceeded'
    }

    @Test
    void validation_storageName(){
        def title = 'Cool!'

        assertInvalid dto(title:title, storageName:null, storageIndex:1 ), 'storageName', 'nullable'
        assertInvalid dto(title:title, storageName:'', storageIndex:1), 'storageName', 'blank'
        assertValid dto(title:title, storageName:'x', storageIndex:1)
        assertValid dto(title:title, storageName:str(20), storageIndex:1)
        assertInvalid dto(title:title, storageName:str(21), storageIndex:1), 'storageName', 'size.toobig'
    }

    @Test
    void validation_storageIndex(){
        def title = 'Cool!'
        def year = 2000

        assertInvalid dto(title:title, releaseYear:year, storageName:'X', storageIndex:null), 'storageIndex', 'nullable'
        assertInvalid dto(title:title, releaseYear:year, storageName:'X',  storageIndex:(-1)), 'storageIndex', 'range.toosmall'
        assertInvalid dto(title:title, releaseYear:year, storageName:'X', storageIndex:0), 'storageIndex', 'range.toosmall'
        assertInvalid dto(title:title, releaseYear:year, storageName:'X', storageIndex:1234), 'storageIndex', 'range.toobig'
    }

    @Test
    void validation_runtime(){
        assertValid dto( title:'Foo', runtime:123, storageName:'X', storageIndex:23 )
        assertInvalid dto( title:'Foo', runtime:-1, storageName:'X', storageIndex:23 ), 'runtime', 'min.notmet'
    }

    // FIXME: the two enum-based tests fail, they shouldnt

    @Test @Ignore
    void validation_mpaarating(){
        assertInvalid dto( title:'Foo', mpaaRating:null ), 'mpaaRating', 'nullable'

        def dto = dto( title:'Foo' )
        dto.mpaaRating = MpaaRating.PG
        assertValid dto
    }

    @Test @Ignore
    void validation_format(){
        assertInvalid dto( title:'Foo', format:null ), 'format', 'nullable'
        assertValid dto( title:'Foo', format:Format.DVD )
    }

	private DetailsDto dto(params){
		def dto = new DetailsDto(params)
		mockForConstraintsTests DetailsDto.class, [ dto ]
		return dto
	}
}
