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

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.StorageUnit
import com.stehno.mymdb.service.MovieService
import grails.converters.JSON
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import com.stehno.mymdb.service.StorageUnitService

/**
 * 
 *
 * @author cjstehno
 */
class ApiController {

    static allowedMethods = [ categories:'GET', fetch:"GET", list:'GET', login:'POST' ]

    private static final Log log = LogFactory.getLog(ApiController.class)
    MovieService movieService
    StorageUnitService storageUnitService

    private static final CATEGORIES = [
        [ id:'titles', label:'Titles' ],
        [ id:'genres', label:'Genres' ],
        [ id:'actors', label:'Actors' ],
        [ id:'years', label:'Release Years' ],
        [ id:'units', label:'Storage Units' ]
    ]

    def login = {
        if(log.isDebugEnabled()) log.debug "login-attempt: ${params.username}"

        def authToken = new UsernamePasswordToken(params.username, params.password as String)
        try {
            SecurityUtils.subject.login(authToken)

            if(log.isDebugEnabled()) log.debug "login-success: ${params.username}"

            render( [success:true] as JSON )

        } catch (AuthenticationException ex){
            if(log.isInfoEnabled()) log.info "login-failed: ${params.username}"
            
            render( [success:false, errors:['general':message(code:'login.failed')]] as JSON )
        }
    }

    def logout = {
        SecurityUtils.subject?.logout()

        render( [success:true] as JSON )
    }

    /**
     * With no filer, lists all available categories.
     * With a filter, lists the available sub-categories for that filter
     */
    def categories = {
        def filter = params.filter

        if(log.isDebugEnabled()) log.debug "categories: $filter"

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

        if(log.isDebugEnabled()) log.debug "list: $category $filter"

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

        if(log.isDebugEnabled()) log.debug "fetch: $id"

        def movie = Movie.get( id as Long )
        def storage = storageUnitService.findStorageForMovie( movie.id )

        def dto = [
            title:movie.title,
            releaseYear:movie.releaseYear,
            description:movie.description,
            genres:movie.genres?.collect { g-> [ id:g.id, name:g.name ] },
            actors:movie.actors?.collect { a-> [ id:a.id, name:a.fullName ] },
            rating:movie.mpaaRating?.label,
            runtime:movie.runtime,
            format:movie.format?.label,
            sites:movie.sites?.collect { s-> [ label:s.label, url:s.url ] },
            broadcast:movie.broadcast?.label,
            poster:movie.poster?.id,
            storage:storage.storageLabel
        ]

        render( dto as JSON )
    }

    private movieTransformer = { movie -> [ id:movie.id, label:movie.title ] }
}
