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
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

/**
 * Implements a MovieDataProvider for http://themoviedb.org using their
 * developer API.
 *
 * @author cjstehno
 */
class TmdbMovieDataProvider implements MovieDataProvider {

    def api

    private static final String PROVIDER_ID = 'TMDB'

    /**
     * Searches the data provider for movies with (or close to) the given title.
     *
     * @param movieTitle
     * @return
     */
    MovieSearchResult[] searchFor( String movieTitle ){
        def results = api.movieSearch( movieTitle )
        if( results[0] instanceof String ){
            return []
        } else {
            return results.collect { dat->
                new MovieSearchResult(
                    providerId:PROVIDER_ID,
                    id:dat.id,
                    title:dat.name.trim(),
                    releaseYear:released(dat),
                    description:description(dat)
                )
            }
        }
    }

    /**
     * Retrieves the data for the movie with the specified id. The id is specific to the underlying
     * provider implementation.
     *
     * @param movieId
     * @return
     */
    MovieData fetch( movieId ){
        def data = api.movieGetInfo( movieId )[0]

        new MovieData(
            providerId:PROVIDER_ID,
            title:data.name,
            releaseYear:released(data),
            description:data.overview,
            genreNames:genres(data),
            actorNames:actors(data),
            posterUrl:poster(data),
            rating:rating(data),
            runtime:data.runtime,
            sites:sites(data)
        )
    }

    String getProviderId() { PROVIDER_ID }

    private MpaaRating rating( entry ){
        def rat = entry.certification
        if( rat == 'G' ) return MpaaRating.G
        else if( rat == 'PG') return MpaaRating.PG
        else if( rat == 'PG-13') return MpaaRating.PG_13
        else if( rat == 'R') return MpaaRating.R
        else if( rat == 'NC-17') return MpaaRating.NC_17
        else if( rat.equalsIgnoreCase('unrated')) return MpaaRating.UNRATED
        else return MpaaRating.UNKNOWN
    }

    private int released( entry ){
        entry.released.split('-')[0] as Integer
    }

    private String description( entry ){
        if( entry.isNull('overview') ){
            return ''
        } else if( StringUtils.length(entry.overview) < 55 ){
            return entry.overview
        } else {
            return WordUtils.abbreviate( entry.overview, 55, 60, '...')
        }
    }

    private String poster( entry ){
        def url = null
        entry.posters.each {
            def image = it.image
            if( image.type == 'poster' ){
                if( !url || image.size == 'cover' ){
                    url = image.url
                }
            }
        }
        url
    }

    private genres( entry ){
        entry.genres.collect { it.name }
    }

    private actors( entry ){
        def actors = []
        entry.cast.each { cast->
            if( cast.job == 'Actor' ){
                actors << cast.name
            }
        }
        actors
    }

    private def sites( entry ){
        def sites = [:]

        if( fieldExists(entry,'url') ){
            sites['TMDB'] = entry.url
        }

        if( fieldExists(entry,'homepage') ){
            sites['Movie'] = entry.homepage
        }

        if( fieldExists(entry,'trailer') ){
            sites[ 'Trailer'] = entry.trailer
        }

        if( fieldExists(entry,'imdb_id') ){
            sites[ 'IMDB'] = "http://www.imdb.com/title/${entry.imdb_id}"
        }

        sites
    }

    private boolean fieldExists( entry, field ){
        !entry.isNull(field) && StringUtils.isNotBlank(entry.getString(field))
    }
}
