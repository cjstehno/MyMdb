/*
   Copyright 2010 Christopher J. Stehno (chris@stehno.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.stehno.mymdb.domain

import org.grails.taggable.*

class Movie implements Taggable {
	
    String title
    String description
    int releaseYear
	Storage storage
	byte[] poster
    
    static hasMany = [genres:Genre, actors:Actor]
	static embedded = ['storage']
	
    Date dateCreated
    Date lastUpdate

    static constraints = {
        title(size:1..100)
        description(size:0..2000)
        releaseYear(range:1930..2020)
        lastUpdate(nullable:true)
        dateCreated(nullable:true)
		storage(nullable:true)
    }
	
	static mapping = {
		cache true
	}

    static transients = ['storageLabel']

    def getStorageLabel(){
        "${storage.name}-${storage.index}"
    }
}
