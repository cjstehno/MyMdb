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

 /**
 * This service gathers movie data from the available MovieDataProviders and aggregates the results for the controller.
 *
 * @author cjstehno
 */
class MovieFetchService {
    // TODO: make this easier to add new providers without rebuilding

    static transactional = false

    def tmdbMovieDataProvider
    def localMovieDataProvider

    def search( String title ){
        def results = []

        results.addAll( localMovieDataProvider.searchFor(title) )
        results.addAll( tmdbMovieDataProvider.searchFor(title) )

        return results
    }

    def fetch( String providerId, movieId ){
        if( localMovieDataProvider.getProviderId() == providerId ){
            return localMovieDataProvider.fetch(movieId)
        } else if(tmdbMovieDataProvider.getProviderId() == providerId){
            return tmdbMovieDataProvider.fetch(movieId)
        } else {
            throw new IllegalArgumentException('ProviderId not found!')
        }
    }
}
