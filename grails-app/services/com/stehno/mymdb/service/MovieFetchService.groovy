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
import org.apache.commons.lang.StringUtils

/**
 * 
 *
 * @author cjstehno
 */
class MovieFetchService {

    static transactional = false

    def movieDataProvider

    def search( title ){
        movieDataProvider.searchFor( title ).collect { dat->
            [ movieId:dat.id, title:dat.name.trim(), releaseYear:released(dat), description:description(dat) ]
        }
    }

    def fetch( movieId ){
        def movieData = movieDataProvider.fetch(movieId)

        [
            title:movieData.name,
            releaseYear:released(movieData),
            description:movieData.overview,
            genres:genres(movieData),
            actors:actors(movieData),
            poster:poster(movieData),
            mpaaRating:movieData.certification,
            runtime:movieData.runtime,
            sites:sites(movieData)
        ]
    }

    private def sites( entry ){
        def sites = []

        if( fieldExists(entry,'url') ){
            sites << [ name:'TMDB', url:entry.url ]
        }

        if( fieldExists(entry,'homepage') ){
            sites << [ name:'Movie', url:entry.homepage ]
        }

        if( fieldExists(entry,'trailer') ){
            sites << [ name:'Trailer', url:entry.trailer ]
        }

        if( fieldExists(entry,'imdb_id') ){
            sites << [ name:'IMDB', url:"http://www.imdb.com/title/${entry.imdb_id}" ]
        }

        sites
    }

    private def fieldExists( entry, field ){
        !entry.isNull(field) && StringUtils.isNotBlank(entry.getString(field))
    }

    private def poster( entry ){
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

    private def genres( entry ){
        entry.genres.collect { it.name }
    }

    private def actors( entry ){
        def actors = []
        entry.cast.each { cast->
            if( cast.job == 'Actor' ){
                actors << cast.name
            }
        }
        actors
    }

    private def released( entry ){
        entry.released.split('-')[0]
    }

    private def description( entry ){
        if( entry.isNull('overview') ){
            return ''
        } else if( StringUtils.length(entry.overview) < 25 ){
            return entry.overview
        } else {
            return WordUtils.abbreviate( entry.overview, 55, 60, '...')
        }
    }
}
