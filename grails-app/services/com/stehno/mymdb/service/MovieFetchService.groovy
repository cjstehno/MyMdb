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

import org.apache.commons.lang.WordUtils

/**
 * 
 *
 * @author cjstehno
 */
class MovieFetchService {

    def movieDataProvider

    def search( title ){
        movieDataProvider.searchFor( title ).collect { dat->
            [ movieId:dat.id, title:dat.name.trim(), releaseYear:released(dat), description:description(dat) ]
        }
    }

    private def released( entry ){
        entry.released.split('-')[0]
    }

    private def description( entry ){
        def text = entry.overview
        if( !text || text.size() < 25 ){
            return text
        } else {
            return WordUtils.abbreviate( text, 55, 60, '...')
        }
    }
}
