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

import org.grails.taggable.Taggable

class Movie implements Taggable {
	
    String title
    String description
    int releaseYear
    Storage storage
    Poster poster
    MpaaRating mpaaRating
    Format format
    Integer runtime

    static hasMany = [genres:Genre, actors:Actor, sites:WebSite]
    static embedded = ['storage']
	
    Date dateCreated
    Date lastUpdate

    static constraints = {
        title(size:1..100)
        description(size:0..2000)
        releaseYear(range:1900..2100)
        storage(nullable:true)  // TODO: make this required when I do the storage refactoring
        poster(nullable:true)
        runtime(nullable:true)

        lastUpdate(nullable:true)
        dateCreated(nullable:true)
    }

    static transients = ['storageLabel']

    def getStorageLabel(){
        storage != null ? "${storage.name}-${storage.index}" : 'N/A'
    }
}

enum MpaaRating {
    UNKNOWN, G, PG, PG_13, R, NC_17, UNRATED
}

enum Format {
    UNKNOWN, VCD, DVD, DVD_R, BLUERAY
}

