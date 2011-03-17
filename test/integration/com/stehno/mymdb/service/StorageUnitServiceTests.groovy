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
        assertEquals 4, slots.size()

        assertAvailableSlot indexedLimited, 2, 'Indexed:Limited-2', slots[0]
        assertAvailableSlot indexedLimited, 3, 'Indexed:Limited-3', slots[1]
        assertAvailableSlot unindexedLimited, null, 'Unindexed:Limited', slots[2]
        assertAvailableSlot unindexedUnlimited, null, 'Unindexed:Unlimited', slots[3]
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
        movie.save()
        movie
    }
}
