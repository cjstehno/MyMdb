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

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import org.springframework.dao.DataRetrievalFailureException
import com.stehno.mymdb.domain.StorageUnit

class MovieService {

    static transactional = true

    StorageUnitService storageUnitService

    private static def titleComparator = { a,b -> a.title <=> b.title } as Comparator

    /**
     * Deletes the movie with the given id. If no movie exists with the specified id,
     * an exception is thrown.
     *
     * @param movieId the id of the movie to be deleted.
     * @throws DataRetrievalFailureException if no movie is found with the id
     */
    def deleteMovie( movieId ){
        def movie = Movie.get(movieId)
        if(movie){
            storageUnitService.unstoreMovie movie
            movie.delete(flush:true)
        } else {
            throw new DataRetrievalFailureException('No movie with id found!')
        }
    }

    /**
     * Retrieves a unique set of all letters used as the first character of a movie
     * title.
     */
    def findMovieTitleLetters(){
        Movie.executeQuery("select distinct(substring(upper(m.title),1,1)) from Movie m").sort()
    }

    def findMoviesTitleStartingWith( letter ){
        Movie.findAll("from Movie as m where substring(upper(m.title),1,1)=? order by m.title asc", [letter.toUpperCase()])
    }
    
    def findMoviesByGenre( genreId ){
        def genre = Genre.get( genreId )
        genre ? (genre.movies as List).sort( titleComparator ) : []
    }

    def findMoviesByActor( actorId ){
        def actor = Actor.get( actorId )
        actor ? (actor.movies as List).sort( titleComparator ) : []
    }

    def findMovieReleaseYears(){
        Movie.executeQuery("select distinct(m.releaseYear) from Movie m order by m.releaseYear asc")
    }

    def findMoviesForBox( unitId ){
        def movies = []

        def storageUnit = StorageUnit.get(unitId)
        storageUnit?.slots?.each { slot->
            movies.addAll slot.movies
        }

        movies.sort { it.title }
    }
}
