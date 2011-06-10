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

package com.stehno.mymdb

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.StorageUnit

/**
 *
 * @author cjstehno
 */
class FixtureBuilder {

    /**
     * Builds one or more Genre objects with the given names.
     *
     * @param names the names of the genres to be created
     * @return one or more created genres
     */

    def buildGenres( String... names ){
        names.collect { name->
            def genre = new Genre( name:name )
            genre.save(flush:true)
            genre
        }
    }

    /**
     * Builds one or more Actor objects from the given names. The names are parsed by the following rules:
     *
     * 1 word - lastName
     * 2 words - firstName lastName
     * 3 words - firstName middleName LastName
     *
     * The space character will be used as the delimiter.
     *
     * @param names one or more actor names
     * @return one or more Actor objects
     */
    def buildActors( String... names ){
        names.collect { name->
            def actor

            def fullName = name.split()
            if( fullName.size() == 1 ){
                actor = new Actor( lastName:fullName[0] )
            } else if( fullName.size() == 2 ){
                actor = new Actor( firstName:fullName[0], lastName:fullName[1] )
            } else {
                actor = new Actor( firstName:fullName[0], middleName:fullName[1], lastName:fullName[2] )
            }

            actor.save(flush:true)
            actor
        }
    }

    /**
     *
     *
     * storage closure no params and should return one or more storage objects
     *
     * @param name
     * @param indexed
     * @param capacity
     * @param storage
     * @return
     */
    StorageUnit buildStorageUnit( String name, boolean indexed=false, int capacity=0, Closure storage = null ){
        def storageUnit = new StorageUnit( name:name, indexed:indexed, capacity:capacity )

        if(storage){
            storage().each { s->
                storageUnit.addToSlots s
            }
        }

        storageUnit.save(flush:true)
        storageUnit
    }

    Storage buildStorage( Integer index, Closure movies=null ){
        def storage = new Storage( index:index )
        if( movies ){
            movies().each { m->
                storage.addToMovies m
            }
        }
        storage
    }
}
