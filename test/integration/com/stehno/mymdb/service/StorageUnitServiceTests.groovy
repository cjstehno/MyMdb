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

class StorageUnitServiceTests extends GrailsUnitTestCase {

    StorageUnitService storageUnitService

    private Long indexedLimited
    private Long unindexedLimited
    private Long unindexedUnlimited

    @Before
    void before(){
        indexedLimited = storageUnit( name:'Indexed:Limited', indexed:true, capacity:3 )
        unindexedLimited = storageUnit( name:'Unindexed:Limited', indexed:false, capacity:3 )
        unindexedUnlimited = storageUnit( name:'Unindexed:Unlimited', indexed:false, capacity:0 )
    }

    @Test
    void listAvailableSlots(){
        def slots = storageUnitService.listAvailableSlots()

        assertNotNull slots
        assertEquals 5, slots.size()

        assertAvailableSlot indexedLimited, 1, 'Indexed:Limited-1', slots[0]
        assertAvailableSlot indexedLimited, 2, 'Indexed:Limited-2', slots[1]
        assertAvailableSlot indexedLimited, 3, 'Indexed:Limited-3', slots[2]
        assertAvailableSlot unindexedLimited, null, 'Unindexed:Limited', slots[3]
        assertAvailableSlot unindexedUnlimited, null, 'Unindexed:Unlimited', slots[4]
    }

    @Test
    void listAvailableSlots_indexed_with_some_taken(){
        def movie = movie( 'Something' )

        storageUnitService.storeMovie indexedLimited, movie.id, 1

        def slots = storageUnitService.listAvailableSlots()

        assertNotNull slots
        assertEquals 5, slots.size()

        assertAvailableSlot indexedLimited, 1, 'Indexed:Limited-1 (1)', slots[0]
        assertAvailableSlot indexedLimited, 2, 'Indexed:Limited-2', slots[1]
        assertAvailableSlot indexedLimited, 3, 'Indexed:Limited-3', slots[2]
        assertAvailableSlot unindexedLimited, null, 'Unindexed:Limited', slots[3]
        assertAvailableSlot unindexedUnlimited, null, 'Unindexed:Unlimited', slots[4]
    }

    @Test
    void fullness(){
        def movieA = movie('Movie-A')
        def movieB = movie('Movie-B')
        def movieC = movie('Movie-C')

        storageUnitService.storeMovie indexedLimited, movieA.id, 1
        storageUnitService.storeMovie indexedLimited, movieB.id, 1
        storageUnitService.storeMovie indexedLimited, movieC.id, 2

        def unit = StorageUnit.get(indexedLimited)
        assertEquals 2, unit.slots.size()
        assertFalse unit.isFull()

        storageUnitService.storeMovie indexedLimited, movie('Movie-D').id, 3

        unit = StorageUnit.get(indexedLimited)
        assertEquals 3, unit.slots.size()
        assertTrue unit.isFull()
    }

    @Test
    void listAvailableSlots_indexed_with_multiple(){
        storageUnitService.storeMovie indexedLimited, movie( 'Something' ).id, 1
        storageUnitService.storeMovie indexedLimited, movie( 'Other' ).id, 1

        def slots = storageUnitService.listAvailableSlots()

        assertNotNull slots
        assertEquals 5, slots.size()

        assertAvailableSlot indexedLimited, 1, 'Indexed:Limited-1 (2)', slots[0]
        assertAvailableSlot indexedLimited, 2, 'Indexed:Limited-2', slots[1]
        assertAvailableSlot indexedLimited, 3, 'Indexed:Limited-3', slots[2]
        assertAvailableSlot unindexedLimited, null, 'Unindexed:Limited', slots[3]
        assertAvailableSlot unindexedUnlimited, null, 'Unindexed:Unlimited', slots[4]
    }

    @Test 
    void listAvailableSlots_unindexedlimited_with_some_taken(){
        def movie = movie( 'Something' )

        storageUnitService.storeMovie unindexedLimited, movie.id

        def slots = storageUnitService.listAvailableSlots()

        assertNotNull slots
        assertEquals 5, slots.size()

        assertAvailableSlot indexedLimited, 1, 'Indexed:Limited-1', slots[0]
        assertAvailableSlot indexedLimited, 2, 'Indexed:Limited-2', slots[1]
        assertAvailableSlot indexedLimited, 3, 'Indexed:Limited-3', slots[2]
        assertAvailableSlot unindexedLimited, null, 'Unindexed:Limited', slots[3]
        assertAvailableSlot unindexedUnlimited, null, 'Unindexed:Unlimited', slots[4]
    }



    @Test
    void storeMovie(){
        def movie = movie('Superman')
        def movieId = movie.id

        storageUnitService.storeMovie( indexedLimited, movieId, 2 )

        assertStorage 'Indexed:Limited', 2, storageUnitService.findStorageForMovie(movie.id)

        def unit = StorageUnit.get(indexedLimited)
        assertEquals 1, unit.slots.size()
    }

    @Test
    void storeMovie_two_movies(){
        def theMovie = movie('Superman')
        def movieId = theMovie.id

        storageUnitService.storeMovie( indexedLimited, movieId, 2 )

        assertEquals 1, StorageUnit.get(indexedLimited).slots.size()
        assertStorage 'Indexed:Limited', 2, storageUnitService.findStorageForMovie(theMovie.id)

        theMovie = movie('Spiderman')
        movieId = theMovie.id

        storageUnitService.storeMovie( indexedLimited, movieId, 3 )

        assertEquals 2, StorageUnit.get(indexedLimited).slots.size()
//        assertStorage 'Indexed:Limited', 3, storageUnitService.findStorageForMovie(theMovie.id)
    }

    @Test(expected = IllegalArgumentException)
    void storeMovie_no_index(){
        storageUnitService.storeMovie( indexedLimited, movie('Superman').id )

        assertEquals 0, Storage.count()
        assertNull storageUnitService.findStorageForMovie(Movie.findByTitle('Superman').id)
        assertEquals 0, StorageUnit.get(indexedLimited).slots?.size()
    }

    @Test(expected = IllegalArgumentException)
    void storeMovie_zero_index(){
        storageUnitService.storeMovie( indexedLimited, movie('Superman').id, 0 )

        assertEquals 0, Storage.count()
        assertNull storageUnitService.findStorageForMovie(Movie.findByTitle('Superman').id)
        assertEquals 0, StorageUnit.get(indexedLimited).slots?.size()
    }

    @Test
    void storeMovie_slot_in_use(){
        storageUnitService.storeMovie( indexedLimited, movie('Superman').id, 2 )
        storageUnitService.storeMovie( indexedLimited, movie('Spiderman').id, 2 )

        assertEquals 1, Storage.count()
        assertEquals 1, StorageUnit.get(indexedLimited).slots?.size()
        assertNotNull storageUnitService.findStorageForMovie(Movie.findByTitle('Superman').id)
        assertNotNull storageUnitService.findStorageForMovie(Movie.findByTitle('Spiderman').id)
    }

    private void assertStorage( unitName, index, movieStorage ){
        assertEquals index, movieStorage.index
        assertEquals unitName, movieStorage.unit.name
    }

    private void assertAvailableSlot( Long id, index, label, slot ){
        assertEquals id, slot.id as Long
        assertEquals index, slot.index
        assertEquals label, slot.label  
    }

    private Long storageUnit( params ){
        def unit = new StorageUnit( params )
        unit.save(flush:true)
        unit.id
    }

    private Movie movie( title ){
        def movie = new Movie( title:title, description:"Something about $title" )
        movie.mpaaRating = MpaaRating.PG
        movie.format = Format.DVD
        movie.broadcast = Broadcast.MOVIE
        movie.save(flush:true)
        movie
    }
}
