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

package com.stehno.mymdb.domain

/**
 * Represents a physical location where movies are stored.
 *
 * @author cjstehno
 */
class StorageUnit {

    String name
    boolean indexed
    int capacity

    static hasMany = [ slots:Storage ]

    static constraints = {
        name( nullable:false, blank:false, size:1..20, unique:true )
        capacity( min:0 )
    }

    static transients = ['full']

    boolean isFull(){
        capacity && ( (slots?.size() ?: 0 ) >= capacity )
    }
}
