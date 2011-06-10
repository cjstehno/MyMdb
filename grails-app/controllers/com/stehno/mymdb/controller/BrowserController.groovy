/*
   Copyright 2010 Christopher J. Stehno (chris@stehno.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.stehno.mymdb.controller

import grails.converters.JSON
import org.apache.shiro.SecurityUtils
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.service.StorageUnitService
import com.stehno.mymdb.service.MovieService

class BrowserController {

    MovieService movieService
    StorageUnitService storageUnitService

    def index = { /* just routes to view */ }
	
    def titles = {
        renderListAsJson movieService.findMovieTitleLetters().collect(identityCollector)
    }

    def releaseYears = {
        renderListAsJson movieService.findMovieReleaseYears().collect(identityCollector)
    }

    def genres = {
        renderListAsJson Genre.list( [sort:'name', order:'asc'] ).collect { [id:it.id, label:it.name] }
    }

    def actors = {
        renderListAsJson Actor.list( [sort:'lastName', order:'asc'] ).collect { [id:it.id, label:it.displayName] }
    }

    def list = {
        def categoryId = params.cid
        def results
        switch(params.sid){
            case 'title_store':
                results = movieService.findMoviesTitleStartingWith( categoryId )
                break
            case 'genre_store':
                results = movieService.findMoviesByGenre( categoryId )
                break
            case 'actor_store':
                results = movieService.findMoviesByActor( categoryId )
                break
            case 'box_store':
                results = movieService.findMoviesForBox( categoryId as Long )
                break
            case 'year_store':
                results = Movie.findAllByReleaseYear( categoryId as Integer, [sort:'title', order:'asc'] )
                break
            default:
                results = Movie.list( [sort:'title', order:'asc'] )
                break
        }

        def total = results.size()

        results = paginate( results, params.start as Integer ?: 0, params.limit as Integer ?: 60 )

        def user = MymdbUser.findByUsername(SecurityUtils.subject.principal as String)

        def movieList = results.collect { m->
            def movieGenres = m.genres.collect { g-> g.name }

            def favorite = /*Favorite.findByUserAndMovie(user, m) !=*/ null

            def storageSlot = storageUnitService.findStorageForMovie( m.id )

            [mid:m.id, ti:m.title, yr:m.releaseYear, bx:storageSlot?.storageLabel ?: '-', ge:movieGenres.sort().join(', '), fav:favorite]
        }

        render( [movies:movieList, total:total ] as JSON )
    }

    def about = { /* just routes to view */ }

    def details = {
        [ movieInstance:Movie.get( params.mid ), box:storageUnitService.findStorageForMovie(params.mid as long)]
    }

    /**
     *  Closure used in collect methods to transfor item into map with
     *  both the id and label set to the original object value.
     */
    private def identityCollector = {
        [id:it, label:it]
    }

    private def renderListAsJson( list ){
        render ([items:list] as JSON)
    }

    private paginate( list, int start, int limit ){
        def page = []
        if(list){
            def end = Math.min( list.size(), start+limit ) -1
            if(start <= end){
                page = list[start..end]
            }
        }
        return page
    }
}
