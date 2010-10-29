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

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Movie

class MobileController {

    def movieService

    static def categories = [
        [label:'Titles', category:'titles'], [label:'Genres', category:'genres'],
        [label:'Actors', category:'actors'], [label:'Release Years', category:'years'],
        [label:'Storage', category:'boxes']
    ]

    def index = {
        [listItems:categories]
    }

    def titles = {
        def items = movieService.findMovieTitleLetters().collect {
            [ label:it, category:'title', id:it ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Titles:"] )
    }

    def title = {
        def letter = params.id
        def items = movieService.findMoviesTitleStartingWith( letter ).collect {
            [ label:it.title, category:'movie', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Titles: $letter"] )
    }

    def genres = {
        def items = Genre.list( [sort:'name', order:'asc'] ).collect {
            [ label:it.name, category:'genre', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Genres:"] )
    }

    def genre = {
        def genreId = params.id as Long
        def items = movieService.findMoviesByGenre( genreId ).collect {
            [ label:it.title, category:'movie', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Genres: ${Genre.get(genreId).name}"] )
    }

    def actors = {
        def items = Actor.list( [sort:'lastName', order:'asc'] ).collect {
            [ label:"${it.lastName}, ${it.firstName} ${it.middleName}", category:'actor', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Actors:"] )
    }

    def actor = {
        def actorId = params.id as Long
        def items = movieService.findMoviesByActor( actorId ).collect {
            [ label:it.title, category:'movie', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Actors: ${Actor.get(actorId).displayName}"] )
    }

    def years = {
        def items = movieService.findMovieReleaseYears().collect {
            [ label:it, category:'year', id:it ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Release Years:"] )
    }

    def year = {
        def year = params.id as Integer
        def items = Movie.findAllByReleaseYear( year, [sort:'title', order:'asc'] ).collect {
            [ label:it.title, category:'movie', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Release Years: ${year}"] )
    }

    def boxes = {
        def items = movieService.findMovieBoxes().collect {
            [ label:it, category:'box', id:it ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Storage:"] )
    }

    def box = {
        def box = params.id
        def items = movieService.findMoviesForBox(box).collect {
            [ label:it.title, category:'movie', id:it.id ]
        }
        render( view:'index', model:[listItems:items, categoryName:"Storage: ${box}"] )
    }

    def movie = {
        [movie:Movie.get( params.id as Long)]
    }
}
