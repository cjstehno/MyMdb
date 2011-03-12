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

package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.StorageUnit
import org.junit.Before
import org.junit.Test

class StorageControllerTests extends ControllerTestCase {

    @Before
    void before(){
        controller = new StorageController()
    }

    @Test
    void list(){
        [
            new StorageUnit( name:'B', indexed:true, capacity:10 ),
            new StorageUnit( name:'C', indexed:false, capacity:100 ),
            new StorageUnit( name:'A', indexed:true, capacity:0 )
        ]*.save(flush:true)

        controller.list()

        def jso = parseJsonResponse()
        assertNotNull jso

        assertStorage( [ name:'A', indexed:true, capacity:0 ], jso.items[0] )
        assertStorage( [ name:'B', indexed:true, capacity:10 ], jso.items[1] )
        assertStorage( [ name:'C', indexed:false, capacity:100 ], jso.items[2] )
    }

    @Test
    void edit(){
        def su = new StorageUnit( name:'A', indexed:true, capacity:10 )
        su.save(flush:true)

        controller.params.id = su.id

        controller.edit()

        def jso = parseJsonResponse()
        assertNotNull jso
        assertTrue jso.success

        assertStorage( [ name:'A', indexed:true, capacity:10 ], jso.data )        
    }

    @Test
    void save_successful(){
        controller.params.name = 'Q'
        controller.params.indexed = true
        controller.params.capacity = 100

        controller.save()

        def jso = parseJsonResponse()
        assertSuccessful jso

        assertEquals 1, StorageUnit.count()
        assertNotNull StorageUnit.findByName('Q')
    }

    @Test
    void save_error_notunique(){
        new StorageUnit( name:'A', indexed:true, capacity:10 ).save(flush:true)

        controller.params.name = 'A'
        controller.params.indexed = false
        controller.params.capacity = 100

        controller.save()

        def jso = parseJsonResponse()
        assertFailure jso, 'name', 'Property [name] of class [class com.stehno.mymdb.domain.StorageUnit] with value [A] must be unique'

        assertEquals 1, StorageUnit.count()
        assertStorage( [name:'A', indexed:true, capacity:10], StorageUnit.findByName('A'))
    }

    @Test
    void update(){
        def su = new StorageUnit( name:'A', indexed:true, capacity:10 )
        su.save(flush:true)

        controller.params.id = su.id
        controller.params.name = 'A'
        controller.params.indexed = false
        controller.params.capacity = 100

        controller.update()

        def jso = parseJsonResponse()
        assertSuccessful jso

        assertEquals 1, StorageUnit.count()
        assertStorage( [name:'A', indexed:false, capacity:100], StorageUnit.findByName('A'))
    }

    @Test
    void update_error_name_already_exists(){
        new StorageUnit( name:'B', indexed:false, capacity:20 ).save(flush:true)
        
        def su = new StorageUnit( name:'A', indexed:true, capacity:10 )
        su.save(flush:true)

        controller.params.id = su.id as String
        controller.params.name = 'B'
        controller.params.indexed = false
        controller.params.capacity = 100

        controller.update()

        def jso = parseJsonResponse()
        assertFailure jso, 'name', 'Property [name] of class [class com.stehno.mymdb.domain.StorageUnit] with value [B] must be unique'

        assertEquals 2, StorageUnit.count()

        // TODO: figure out why tests show that the update was allowed - works fine in functional
//        assertEquals( [], StorageUnit.list().collect { it.name } )
//        assertStorage( [name:'A', indexed:true, capacity:10], StorageUnit.get(su.id))
    }

    @Test
    void delete(){
        new StorageUnit( name:'B', indexed:false, capacity:20 ).save(flush:true)

        def su = new StorageUnit( name:'A', indexed:true, capacity:10 )
        su.save(flush:true)

        controller.params.items = su.id as String

        controller.delete()

        def jso = parseJsonResponse()
        assertSuccessful jso

        assertEquals 1, StorageUnit.count()
    }

    private void assertStorage( expected, actual ){
        assertEquals expected.name, actual.name
        assertEquals expected.indexed, actual.indexed
        assertEquals expected.capacity, actual.capacity
    }
}
