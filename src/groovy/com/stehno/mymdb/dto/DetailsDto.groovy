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
package com.stehno.mymdb.dto

import com.stehno.mymdb.domain.MpaaRating
import com.stehno.mymdb.domain.Format
import com.stehno.mymdb.domain.Broadcast

class DetailsDto {

    String title
    String description
    Integer releaseYear
    String storageId
    MpaaRating mpaaRating = MpaaRating.UNKNOWN
    Integer runtime = 0
    Format format = Format.UNKNOWN
    Broadcast broadcast = Broadcast.MOVIE

    static constraints = {
        title( nullable:false, blank:false, size:1..100 )
        description( nullable:true, blank:true, maxSize:2000)
        releaseYear( nullable:true, range:1900..2100 )
        storageId( nullable:false, blank:false )
        runtime( nullable:true, min:0 )
        mpaaRating( nullable:false )
        format( nullable:false )
        broadcast( nullable:false )
    }

    // TODO: see if there is a way to require both storage name and index if one is specified

    /**
     * Overridden to provide conversion to a Map. The map converts the enum properties
     * to their name() values. Properties with null values will be omitted from the map.
     *
     * @param type only Map is supported by the override
     * @return
     */
    @Override
    public Object asType( Class type ){
        if(type == Map.class){
            def map = [:]

            if(title) map.title = title
            if(description) map.description = description
            if(releaseYear) map.releaseYear = releaseYear
            map.storageId = storageId
            map.mpaaRating = mpaaRating.name()
            if(runtime) map.runtime = runtime
            map.format = format.name()
            map.broadcast = broadcast.name()

            return map
        }
        super.asType( type )
    }
}

