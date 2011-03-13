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

package com.stehno.mymdb.service

import grails.test.GrailsUnitTestCase
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.domain.*

class IndexedLimitedStorageUnitServiceTests extends GrailsUnitTestCase {

    StorageUnitService storageUnitService

    private Long unitId

    @Before
    void before(){
        unitId = storageUnit( name:'Indexed:Limited', indexed:true, capacity:3 )
    }

    @Test
    void storeMovie(){
        def movieId = movie('Superman')

        storageUnitService.storeMovie( unitId, movieId, 2 )

        assertStorage 'Indexed:Limited', 2, Movie.get(movieId).storage

        def unit = StorageUnit.get(unitId)
        assertEquals 1, unit.slots.size()
    }

    @Test
    void storeMovie_two_movies(){
        def movieId = movie('Superman')

        storageUnitService.storeMovie( unitId, movieId, 2 )

        assertStorage 'Indexed:Limited', 2, Movie.get(movieId).storage

        def unit = StorageUnit.get(unitId)
        assertEquals 1, unit.slots.size()

        movieId = movie('Spiderman')

        storageUnitService.storeMovie( unitId, movieId, 3 )

        assertStorage 'Indexed:Limited', 3, Movie.get(movieId).storage

        assertEquals 2, StorageUnit.get(unitId).slots.size()
    }

    @Test(expected = IllegalArgumentException)
    void storeMovie_no_index(){
        storageUnitService.storeMovie( unitId, movie('Superman') )

        assertEquals 0, Storage.count()
        assertNull Movie.findByTitle('Superman').storage
        assertEquals 0, StorageUnit.get(unitId).slots?.size()
    }

    // FIXME: need to check storage and movie data to ensure no leakage!!

    @Test(expected = IllegalArgumentException)
    void storeMovie_zero_index(){
        storageUnitService.storeMovie( unitId, movie('Superman'), 0 )

        assertEquals 0, StorageUnit.get(unitId).slots?.size()
    }

    @Test(expected = IllegalArgumentException)
    void storeMovie_slot_in_use(){
        storageUnitService.storeMovie( unitId, movie('Superman'), 2 )
        storageUnitService.storeMovie( unitId, movie('Spiderman'), 2 )

        assertEquals 0, StorageUnit.get(unitId).slots?.size()
    }

    private void assertStorage( unitName, index, movieStorage ){
        assertEquals index, movieStorage.index
        assertEquals unitName, movieStorage.storageUnit.name
    }

    private Long storageUnit( params ){
        def unit = new StorageUnit( params )
        unit.save(flush:true)
        unit.id
    }

    private Long movie( title ){
        def movie = new Movie( title:title, description:"Something about $title" )
        movie.mpaaRating = MpaaRating.PG
        movie.format = Format.DVD
        movie.save()
        movie.id
    }
}