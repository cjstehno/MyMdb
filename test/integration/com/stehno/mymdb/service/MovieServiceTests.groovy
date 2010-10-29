package com.stehno.mymdb.service

import grails.test.*

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Storage

class MovieServiceTests extends GrailsUnitTestCase {

    def movieService
    private def horror
    private def johnQPublic

    protected void setUp(){
        super.setUp()

        horror = new Genre(name:'Horror').save()
        johnQPublic = new Actor(firstName:'John', middleName:'Quincy', lastName:'Public').save()
    }

    void testFindMovieTitleLetters(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        assertEquals 5, Movie.list().size()

        def letters = movieService.findMovieTitleLetters()
        assertEquals 3, letters.size()
        assertEquals 'A', letters[0]
        assertEquals 'B', letters[1]
        assertEquals 'C', letters[2]
    }

    void testFindMoviesTitleStartingWith(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesTitleStartingWith( 'C' )
        assertEquals 'Charlie', movies[0].title
        assertEquals 'Choochoo', movies[1].title

        assertEquals 'Alpha', movieService.findMoviesTitleStartingWith( 'A' )[0].title

        assertEquals 2, movieService.findMoviesTitleStartingWith( 'B' ).size()
        assertEquals 2, movieService.findMoviesTitleStartingWith( 'b' ).size()
    }

    void testFindMoviesByGenre(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesByGenre( horror.id )
        assertEquals 5, movies.size()
        assertEquals 'Alpha', movies[0].title
        assertEquals 'Bravo', movies[1].title
        assertEquals 'Charlie', movies[2].title

        assertEquals 0, movieService.findMoviesByGenre( 123 ).size()
    }
    
    void testFindMoviesByActor(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesByActor( johnQPublic.id )
        assertEquals 5, movies.size()
        assertEquals 'Alpha', movies[0].title
        assertEquals 'Bravo', movies[1].title
        assertEquals 'Charlie', movies[2].title

        assertEquals 0, movieService.findMoviesByActor( 123 ).size()
    }

    void testFindMovieReleaseYears(){
        addMovie( 'Alpha', { it.releaseYear = 2000 } )
        addMovie( 'Bravo', { it.releaseYear = 2010 } )
        addMovie( 'Charlie', { it.releaseYear = 1980 } )
        addMovie( 'Delta', { it.releaseYear = 2010 } )

        def years = movieService.findMovieReleaseYears()
        assertEquals 3, years.size()
        assertEquals 1980, years[0]
        assertEquals 2000, years[1]
        assertEquals 2010, years[2]
    }

    void testFindMovieBoxes(){
        addMovie'Alpha', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'A', index:1 )
        }
        addMovie 'Bravo', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'B', index:1 )
        }
        addMovie 'Charlie', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'A', index:2 )
        }

        def boxes = movieService.findMovieBoxes()
        assertEquals 2, boxes.size()
        assertEquals 'A', boxes[0]
        assertEquals 'B', boxes[1]
    }

    void testFindMoviesForBox(){
        addMovie'Zed', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'A', index:1 )
        }
        addMovie 'Bravo', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'B', index:1 )
        }
        addMovie 'Charlie', {
            it.releaseYear = 2000
            it.storage = new Storage( name:'A', index:2 )
        }

        def movies = movieService.findMoviesForBox('A')
        assertEquals 2, movies.size()
        assertEquals 'Charlie', movies[0].title
        assertEquals 'Zed', movies[1].title

        assertEquals 1, movieService.findMoviesForBox('B').size()
        assertEquals 0, movieService.findMoviesForBox('C').size()
    }

    protected void tearDown(){
        super.tearDown()
    }

    private def addMovie( title, Closure c = defaultMovieBuilder ){
        def movie = new Movie(title:title, description:'something...',
            poster:'foo'.getBytes()
        )
        c.call movie
        movie.addToGenres horror
        movie.addToActors johnQPublic
        movie.save()
    }

    private static def defaultMovieBuilder = { m->
        m.releaseYear = 2000
    }
}
