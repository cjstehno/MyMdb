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
package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Movie

class MovieService {

    static transactional = true

    /**
     * Retrieves the poster bytes for the movie with the specified id.
     */
    def findPoster( movieId ){
        def movie = Movie.get(movieId)
        movie && movie.poster && movie.poster.size() != 0 ? movie.poster : null
    }

    /**
     * Retrieves a unique set of all letters used as the first character of a movie
     * title.
     */
    def findMovieTitleLetters(){
        Movie.executeQuery("select distinct(substring(m.title,1,1)) from Movie m")
    }

    def findMoviesTitleStartingWith( letter ){
        Movie.findAll("from Movie as m where substring(m.title,1,1)=? order by m.title", [letter])
    }
    
    def findMoviesByGenre( genreId ){
        Movie.executeQuery("from Movie m where ? in elements(m.genres) order by m.title asc", [genreId])
    }

    def findMoviesByActor( actorId ){
        Movie.executeQuery("from Movie m where ? in elements(m.actors) order by m.title asc", [actorId])
    }

    def findMovieReleaseYears(){
        Movie.executeQuery("select distinct(m.releaseYear) from Movie m order by m.releaseYear asc")
    }

    def findMovieBoxes(){
        Movie.executeQuery("select distinct(m.storage.name) from Movie m order by m.storage.name asc")
    }

    def findMoviesForBox( boxName ){
        Movie.findAll("from Movie m where m.storage.name=? order by m.title asc", [boxName])
    }
}
