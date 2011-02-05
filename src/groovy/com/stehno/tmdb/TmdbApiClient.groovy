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

package com.stehno.tmdb

import grails.converters.JSON


 /**
 * This is not meant to be a general-use client for the TMDB API. It implements only
 * the functionality that I need.
 *
 * @author cjstehno
 */
class TmdbApiClient {

    def apiKey

    private static final def URL_BASE = 'http://api.themoviedb.org/2.1/'

    /**
     * Searches the movie database for movies the the given title and poss
     * @param title
     * @return a JSON object containing the search results
     */
    def movieSearch( title){
        call buildUrl( 'Movie.search', title )
    }

    /**
     * Retrieve the movie information for the movie with the specified id.
     *
     * @param movieId id of movie found in search
     * @return a JSON object containing the data for the movie
     */
    def movieGetInfo( movieId ){
        call buildUrl( 'Movie.getInfo', movieId )
    }

    private def call( url ){
        url.withReader { reader-> JSON.parse(reader) }
    }

    private def buildUrl( action, param ){
        "${URL_BASE}${action}/en/json/${apiKey}/${URLEncoder.encode(param)}".toURL()
    }    
}
