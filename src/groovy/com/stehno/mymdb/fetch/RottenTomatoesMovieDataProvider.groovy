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

import com.stehno.mymdb.domain.MpaaRating
import com.stehno.rotten.RottenTomatoesApiClient
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

/**
 *
 *
 * @author cjstehno
 */
class RottenTomatoesMovieDataProvider implements MovieDataProvider {

    RottenTomatoesApiClient api

    private static final String PROVIDER_ID = 'Rotten Tomatoes'

    String getProviderId() {
        return PROVIDER_ID
    }

    MovieSearchResult[] searchFor(String movieTitle) {
        if( !api.isAvailable() ) return []

        def results = api.movieSearch( movieTitle )
        return results.movies.collect { dat->
            new MovieSearchResult(
                providerId:PROVIDER_ID,
                id:dat.id,
                title:dat.title.trim(),
                releaseYear:dat.year ? dat.year as int : 0,
                description:description(dat)
            )
        }
    }

    MovieData fetch(Object movieId) {
        if( !api.isAvailable() ) return null

        def data = api.movieGetInfo( movieId )

        new MovieData(
            providerId:PROVIDER_ID,
            title:data.title,
            releaseYear:data.year as int,
            description:description(data),
            actorNames:actors(data),
            posterUrl:poster(data),
            rating:MpaaRating.fromLabel(data.mpaa_rating),
            runtime:runtime(data),
            sites:sites(data)
        )
    }

    private Map<String,String> sites( data ){
        def map = [:]
        if( data.alternate_ids?.imdb ){
            map['IMDB'] = "http://www.imdb.com/title/${data.alternate_ids.imdb}"
        }
        return map
    }

    private String[] actors( data ){
        data.abridged_cast?.collect { cast-> cast.name }
    }

    private String poster( data ){
        def post = null
        if(data.posters){
            post = data.posters.original

            if( !post ){
                post = data.posters.detailed
            }
        }
        return post
    }

    private int runtime( entry ){
        entry.isNull('runtime') ? 0 : entry.runtime as Integer
    }

    private String description( entry ){
        if( entry.isNull('synopsis') ){
            return ''
        } else if( StringUtils.length(entry.synopsis) < 55 ){
            return entry.synopsis
        } else {
            return WordUtils.abbreviate( entry.synopsis, 55, 60, '...')
        }
    }
}
