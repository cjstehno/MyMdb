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

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

class BrowserController {

    def movieService

    def index = { }
	
    def titles = {
        renderListAsJson movieService.findMovieTitleLetters().collect(identityCollector)
    }

    def releaseYears = {
        renderListAsJson movieService.findMovieReleaseYears().collect(identityCollector)
    }

    def storage = {
        renderListAsJson movieService.findMovieBoxes().collect(identityCollector)
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
                results = movieService.findMoviesForBox( categoryId )
                break
            case 'year_store':
                results = Movie.findAllByReleaseYear( year as Integer, [sort:'title', order:'asc'] )
                break
            default:
                results = Movie.list( [sort:'title', order:'asc'] )
                break
        }

        render(contentType:"text/json") {
            movies = array {
                for(r in results) {
                    movie mid:r.id, ti:r.title, yr:r.releaseYear, bx:r.storageLabel
                }
            }
        }
    }

    def about = { /* just routes to view */ }

    def details = {
        [ movieInstance:Movie.get( params.mid )]
    }

    /**
     *  Closure used in collect methods to transfor item into map with
     *  both the id and label set to the original object value.
     */
    private def identityCollector = {
        [id:it, label:it]
    }

    private def renderListAsJson( list ){
        render(contentType:"text/json") {
            items = array {
                for(r in list) {
                    item cid:r.id, lbl:r.label
                }
            }
        }
    }
}
