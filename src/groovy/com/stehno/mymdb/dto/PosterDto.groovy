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

/**
 * MovieManager wizard DTO for the poster panel. The posterType field value will specify which of the other properties should be specified.
 *
 * NONE - no value
 * FILE - file property (uploaded file data)
 * URL - url property (url of image to be copied), should populate file data
 * EXISTING - posterId and posterName of existing poster
 */
class PosterDto {

    PosterType posterType = PosterType.NONE
    String url
    byte[] file
    Long posterId
    String posterName

    static constraints = {
        posterType( nullable:false )
        url( nullable:true, url:true )
        posterName( nullable:true, size:1..100 )
        file( nullable:true, maxSize:1024000 )
    }

    // TODO: would be nice to group constraints by selection type

    /**
     * Overridden to provide conversion to a Map. The map will not contain the file
     * byte data, and the posterType property will be the name() value of the enum.
     * 
     * @param type only Map is suppored by the override
     * @return
     */
    @Override
    public Object asType( Class type ){
        if(type == Map.class){
            return [
                posterType: posterType.name(),
                url: url,
                posterId: posterId,
                posterName: posterName
            ]
        }
        super.asType( type )
    }
}

/**
 * A simple enum of poster types available.
 */
enum PosterType {
    NONE, FILE, EXISTING, URL
}

