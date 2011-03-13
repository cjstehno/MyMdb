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

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.MpaaRating
import com.stehno.mymdb.domain.StorageUnit
import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.stehno.mymdb.domain.Format

class StorageUnitServiceTests extends GrailsUnitTestCase {

    StorageUnitService service

    private Long indexedLimited
    private Long unindexedLimited
    private Long indexedUnlimited
    private Long unindexedUnlimited

    @Before
    void before(){
        super.setUp()

        indexedLimited =storageUnit( name:'Indexed:Limited', indexed:true, capacity:3 )
        unindexedLimited = storageUnit( name:'Unindexed:Limited', indexed:false, capacity:3 )
        indexedUnlimited = storageUnit( name:'Indexed:Unlimited', indexed:true, capacity:0 )
        unindexedUnlimited = storageUnit( name:'Unindexed:Unlimited', indexed:false, capacity:0 )

        this.service = new StorageUnitService()
    }

    @Test
    void storeMovie_indexed_limited(){
        def movieId = movie('Superman')

        service.storeMovie( indexedLimited, movieId, 2 )

        def mov = Movie.get(movieId)
        assertEquals 2, mov.storage.index
        assertEquals 'Indexed:Limited', mov.storage.storageUnit.name

        def unit = StorageUnit.get(indexedLimited)
        assertEquals 1, unit.slots.size()
    }

    @After
    void after(){
        super.tearDown()
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
