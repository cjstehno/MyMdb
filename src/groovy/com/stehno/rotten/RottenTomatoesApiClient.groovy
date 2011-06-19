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

package com.stehno.rotten

import com.stehno.mymdb.service.MymdbConfigService
import grails.converters.JSON

/**
 * Lightweight wrapper over the Rottoen Tomatoes API.
 *
 * @author cjstehno
 */
class RottenTomatoesApiClient {

    MymdbConfigService mymdbConfigService

    private static final def URL_BASE = 'http://api.rottentomatoes.com/api/public/v1.0/movies'

    /**
     * Searches the movie database for movies the the given title
     * 
     * @param title
     * @return a JSON object containing the search results
     */
    def movieSearch( title){
        call "${URL_BASE}.json?apikey=${mymdbConfigService.getRottenTomatoesApiKey()}&q=${URLEncoder.encode(title)}".toURL()
    }

    boolean isAvailable(){
        mymdbConfigService.getRottenTomatoesApiKey() != null
    }

    /**
     * Retrieve the movie information for the movie with the specified id.
     *
     * @param movieId id of movie found in search
     * @return a JSON object containing the data for the movie
     */
    def movieGetInfo( movieId ){
        call "${URL_BASE}/${URLEncoder.encode(movieId)}.json?apikey=${mymdbConfigService.getRottenTomatoesApiKey()}".toURL()
    }

    private def call( url ){
        url.withReader { reader-> JSON.parse(reader) }
    }
}
