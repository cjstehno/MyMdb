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

package com.stehno.mymdb.fetch

/**
 * 
 *
 * @author cjstehno
 */
class TmdbMovieDataProvider implements MovieDataProvider {

    def api

    /**
     * Searches the data provider for movies with (or close to) the given title.
     *
     * @param movieTitle
     * @return
     */
    def searchFor( String movieTitle ){
        def results = api.movieSearch( movieTitle )

        // when no results the first element is a string message
        results[0] instanceof String ? [] : results
    }

    /**
     * Retrieves the data for the movie with the specified id. The id is specific to the underlying
     * provider implementation.
     *
     * @param movieId
     * @return
     */
    def fetch( movieId ){
        api.movieGetInfo( movieId )[0]
    }
}