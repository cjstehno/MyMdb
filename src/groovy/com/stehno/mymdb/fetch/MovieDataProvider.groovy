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
 * Interface for providers of movie meta information from external sources.
 *
 * @author cjstehno
 */
public interface MovieDataProvider {

    /**
     * Retrieves the unique id for this provider. This id is used to tie results back to the
     * provider.
     *
     * @return a unique provider id
     */
    String getProviderId()

    /**
     * Searches the data provider for movies with (or close to) the given title.
     *
     * @param movieTitle
     * @return
     */
    MovieSearchResult[] searchFor( String movieTitle )

    /**
     * Retrieves the data for the movie with the specified id. The id is specific to the underlying
     * provider implementation.
     * 
     * @param movieId
     * @return
     */
    MovieData fetch( movieId )
}