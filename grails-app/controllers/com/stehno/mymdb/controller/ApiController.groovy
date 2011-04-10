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

package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.service.MovieService
import grails.converters.JSON
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.StorageUnit
import com.stehno.mymdb.domain.Movie

/**
 * 
 *
 * @author cjstehno
 */
class ApiController {

    static allowedMethods = [ categories:'GET', fetch:"GET", list:'GET' ]

    MovieService movieService

    private static final CATEGORIES = [
        [ id:'titles', label:'Titles' ],
        [ id:'genres', label:'Genres' ],
        [ id:'actors', label:'Actors' ],
        [ id:'years', label:'Release Years' ],
        [ id:'units', label:'Storage Units' ]
    ]

    /**
     * With no filer, lists all available categories.
     * With a filter, lists the available sub-categories for that filter
     */
    def categories = {
        def filter = params.filter

        def categories = []

        if( !filter ){
            categories.addAll CATEGORIES

        } else if( filter == 'titles'){
            movieService.findMovieTitleLetters().each { letter->
                categories << [ id:letter, label:letter ]
            }
        } else if( filter == 'genres'){
            Genre.list( [sort:'name', order:'asc'] ).each { gen->
                categories << [id:gen.id, label:gen.name]
            }
        } else if( filter == 'actors'){
            Actor.list( [sort:'lastName', order:'asc'] ).each { act->
                categories << [id:act.id, label:act.displayName]
            }
        } else if( filter == 'years'){
            movieService.findMovieReleaseYears().each { yr->
                categories << [id:yr, label:yr]
            }
        } else if( filter == 'units'){
            StorageUnit.list( [sort:'name', order:'asc'] ).each { unit->
                categories << [id:unit.id, label:unit.name]
            }
        }

        render( categories as JSON )
    }

    def list = {
        def category = params.category
        def filter = params.filter

        def movies = []

        if( category == 'titles'){
            movies = movieService.findMoviesTitleStartingWith( filter ).collect( movieTransformer )

        } else if( category == 'genres'){
            movies = movieService.findMoviesByGenre( filter as Long ).collect( movieTransformer )
            
        } else if( category == 'actors'){
            movies = movieService.findMoviesByActor( filter as Long ).collect( movieTransformer )

        } else if( category == 'years'){
            movies = Movie.findAllWhere( releaseYear: filter as Integer ).collect( movieTransformer )
            
        } else if( category == 'units'){
            movies = movieService.findMoviesForBox( filter as Long ).collect( movieTransformer )
        }

        render( movies as JSON )
    }

    /**
     * Fetch the movie with the given id.
     */
    def fetch = {
        def id = params.id

        def movie = Movie.get( id as Long )

        def dto = [
            title:movie.title,
            releaseYear:movie.releaseYear,
            description:movie.description,
            genres:movie.genres?.collect { g-> },
            actors:movie.actors?.collect { a-> },
            rating:movie.mpaaRating?.label,
            runtime:movie.runtime,
            format:movie.format?.label,
            sites:movie.sites?.collect { s-> [ label:s.label, url:s.url ] },
            broadcast:movie.broadcast?.label,
            poster:movie.poster?.id
        ]

        render( dto as JSON )
    }

    private movieTransformer = { movie -> [ id:movie.id, label:movie.title ] }
}
