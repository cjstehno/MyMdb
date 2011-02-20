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

import com.stehno.mymdb.domain.Movie
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import com.stehno.mymdb.domain.MpaaRating

/**
 * MovieDataProvider that provides data from the local database of existing movies.
 *
 * @author cjstehno
 */
class LocalMovieDataProvider implements MovieDataProvider {

    private static final String PROVIDER_ID = 'Local'

    MovieSearchResult[] searchFor(String movieTitle) {
//        Movie.findByTitleILike("%$movieTitle%").collect { mov-> TODO: may file bug about this ILike should work
        Movie.find('from Movie as m where upper(m.title) like ?',"%${movieTitle.toUpperCase()}%").collect { mov->
            new MovieSearchResult(
                providerId:PROVIDER_ID,
                id:mov.id,
                title:mov.title,
                releaseYear:mov.releaseYear,
                description:description(mov.description)
            )
        }
    }

    MovieData fetch(Object movieId) {
        def mov = Movie.get(movieId)

        def movieData = new MovieData(
            providerId:PROVIDER_ID,
            title:mov.title,
            releaseYear:mov.releaseYear,
            description:mov.description,
            genreNames:genres(mov),
            actorNames:actors(mov),
            posterUrl:poster(mov),
            rating:mov.mpaaRating?:MpaaRating.UNKNOWN,
            runtime:mov.runtime?:0
        )

        movieData.sites = new HashMap<String,String>()

        if(mov.sites){
            mov.sites.each {
                movieData.put(it.label, it.url)
            }
        }

        return movieData
    }

    String getProviderId() { PROVIDER_ID }

    private poster( movie ){ // TODO: this needs to be more generic
        "/mymdb/poster/show/${movie.poster.id}"
    }

    private String description( str ){
        if( StringUtils.length(str) < 55 ){
            return str
        } else {
            return WordUtils.abbreviate( str, 55, 60, '...')
        }
    }

    private genres( movie ){
        movie.genres.collect { it.name }
    }

    private actors( movie ){
        movie.actors.collect { it.fullName }
    }

    private sites( movie ){
        def s = [:]
        if(movie.sites){
            movie.sites.each {
                s[it.label] = it.url
            }
        }
        return s
    }
}
