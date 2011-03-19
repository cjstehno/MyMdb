package com.stehno.mymdb.service

import com.stehno.mymdb.MovieTestFixture
import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import com.stehno.mymdb.domain.*

class MovieServiceTests extends GrailsUnitTestCase {

    def movieService
    StorageUnitService storageUnitService
    private def horror
    private def johnQPublic
    private def moviePoster

	@Before
    void before(){
        super.setUp()

        horror = new Genre(name:'Horror').save()
        johnQPublic = new Actor(firstName:'John', middleName:'Quincy', lastName:'Public').save()
        moviePoster = new Poster(title:'Movie Poster', content:'foo'.getBytes()).save()
    }

    @Ignore @Test // TODO: figure out why deleteMovie is not accepting a movie id
    void deleteAMovie(){
        [ 'Charlie', 'Alpha', 'Bravo' ].each { addMovie it }

        assertEquals 3, Movie.list().size()

        def movieId = Movie.findByTitle('Alpha').id
        movieService.deleteMovie( movieId )

        def movieList = Movie.list()
        assertEquals 2, movieList.size()
    }

	@Test
    void findMovieTitleLetters(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        assertEquals 5, Movie.list().size()

        def letters = movieService.findMovieTitleLetters()
        assertEquals 3, letters.size()
        assertEquals 'A', letters[0]
        assertEquals 'B', letters[1]
        assertEquals 'C', letters[2]
    }

	@Test
    void findMoviesTitleStartingWith(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesTitleStartingWith( 'C' )
        assertEquals 'Charlie', movies[0].title
        assertEquals 'Choochoo', movies[1].title

        assertEquals 'Alpha', movieService.findMoviesTitleStartingWith( 'A' )[0].title

        assertEquals 2, movieService.findMoviesTitleStartingWith( 'B' ).size()
        assertEquals 2, movieService.findMoviesTitleStartingWith( 'b' ).size()
    }

	@Test
    void findMoviesByGenre(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesByGenre( horror.id )
        assertEquals 5, movies.size()
        assertEquals 'Alpha', movies[0].title
        assertEquals 'Bravo', movies[1].title
        assertEquals 'Charlie', movies[2].title

        assertEquals 0, movieService.findMoviesByGenre( 123 ).size()
    }
    
	@Test
    void findMoviesByActor(){
        [ 'Charlie', 'Alpha', 'Bravo', 'Choochoo', 'boo'].each { addMovie it }

        def movies = movieService.findMoviesByActor( johnQPublic.id )
        assertEquals 5, movies.size()
        assertEquals 'Alpha', movies[0].title
        assertEquals 'Bravo', movies[1].title
        assertEquals 'Charlie', movies[2].title

        assertEquals 0, movieService.findMoviesByActor( 123 ).size()
    }

	@Test
    void findMovieReleaseYears(){
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

	@Test
    void findMoviesForBox(){
        def fixture = new MovieTestFixture()
        fixture.before()

        storageUnitService.storeMovie( fixture.storageUnitId, fixture.movieId )

        def movies = movieService.findMoviesForBox( fixture.storageUnitId )
        assertEquals 1, movies.size()
        assertEquals 'A-Team: Unrated', movies[0].title

        assertEquals 0, movieService.findMoviesForBox(123L).size()
    }

	@After
    void after(){
        super.tearDown()
    }

    private def addMovie( title, Closure c = defaultMovieBuilder ){
        def movie = new Movie( title:title, description:'something...', poster:moviePoster )
        c.call movie
        movie.mpaaRating = MpaaRating.PG
        movie.format = Format.DVD
        movie.addToGenres horror
        movie.addToActors johnQPublic
        movie.save()
    }

    private static def defaultMovieBuilder = { m->
        m.releaseYear = 2000
    }
}
