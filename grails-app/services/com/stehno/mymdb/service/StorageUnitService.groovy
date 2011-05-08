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
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.StorageUnit

/**
 *  Service used to manage the StorageUnit and Movie interactions. This is the recommended
 * means of managing movie storage since there are constraints implied by the business logic,
 * rather than simply the database.
 */
class StorageUnitService {

    static transactional = true

    /**
     * Adds the specified movie to the given storage unit under the given index.
     * 
     * @param storageUnitId the id of the storage unit
     * @param movieId the id of the movie
     * @param index the index to be used by the movie in storage
     */
    void storeMovie( Long storageUnitId, Long movieId, Integer index = null ){
        def unit = StorageUnit.get(storageUnitId)

        if( unit.isFull() ) throw new IllegalArgumentException('StorageUnit is full!')

        if( unit.indexed && !index ) throw new IllegalArgumentException('Index must be specified for indexed storage')

        def movie = Movie.get(movieId)

        if( movie.storage ){
            def oldStorage = movie.storage

            // if the old storage is not the same as new storage, remove it
            if( oldStorage.storageUnit.id != unit.id || oldStorage.index != index ){
                movie.storage = null

                unit.removeFromSlots oldStorage

                // remove any existing storage for movie
                oldStorage.delete()

                createAndSaveStorage movie, unit, index
            }
        } else {
            createAndSaveStorage movie, unit, index
        }
    }

    private void createAndSaveStorage( Movie movie, StorageUnit unit, Integer index = null ){
        def newStorage = new Storage( storageUnit:unit, index:index )
        unit.addToSlots newStorage
        unit.save()

        movie.storage = newStorage
        movie.save()
    }

    /**
     * Lists all available (unused) storage slots. Each item will have a label, id, and optional index.
     *
     * @return
     */
    List listAvailableSlots(){
        def slots = []
        StorageUnit.list( sort:'name', order:'ASC' ).each { unit->
            if( !unit.isFull() ){
                if( unit.indexed ){
                    if( unit.capacity ){
                        def avails = []

                        avails.addAll( (1..unit.capacity) as List )

                        avails.each { n->
                            def count = unit.slots?.findAll { s-> s.index == n }?.size()
                            def countStr = count ? " ($count)" : ''

                            slots << [ id:unit.id, index:n, label:"${unit.name}-$n$countStr" ]
                        }
                    } else {
                        // this use case is not allowed
                        // FIXME: enforce this in the manager UI
                    }
                } else {
                    slots << [ id:unit.id as String, label:unit.name ]
                }
            }
        }
        slots
    }
}
