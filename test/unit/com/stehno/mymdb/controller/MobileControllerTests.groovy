package com.stehno.mymdb.controller

import org.junit.Test;

import grails.test.*
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.service.MovieService

class MobileControllerTests extends ControllerUnitTestCase {

	@Test
    void index() {
        def resp = controller.index()
        assertEquals 5, resp.listItems.size()
    }

	@Test
    void titles(){
        def titleList = ['A','C','G']
        def movieService = [ findMovieTitleLetters:{ titleList } ] as MovieService

        controller.movieService = movieService
        controller.params.id = 359

        controller.titles()

        assertResponse 'Titles:', titleList.collect { [label:it, category:'title', id:it] }
    }

	@Test
    void title(){
        def movieList = [
            new Movie(id:100, title:'Chicago'),
            new Movie(id:200, title:'Cars'),
            new Movie(id:300, title:'Candaian Bacon'),
        ]
        def movieService = [ 
            findMoviesTitleStartingWith:{ letter->
                assertEquals 'C', letter
                movieList
            }
        ] as MovieService

        controller.movieService = movieService
        controller.params.id = 'C'

        controller.title()

        assertResponse 'Titles: C', movieList.collect { [label:it.title, category:'movie', id:it.id] }
    }

	@Test
    void genres(){
        def genreList = [
            new Genre(id:100, name:'Action'),
            new Genre(id:200, name:'Comedy')
        ]
        mockDomain Genre, genreList

        controller.genres();

        assertResponse 'Genres:', genreList.collect { [label:it.name, category:'genre', id:it.id] }
    }

	@Test
    void genre(){
        mockDomain Genre, [
            new Genre(id:100, name:'Action'),
            new Genre(id:200, name:'Comedy')
        ]

        def movieList = [
            new Movie(id:100, title:'Chicago'),
            new Movie(id:200, title:'Cars'),
            new Movie(id:300, title:'Candaian Bacon'),
        ]
        def movieService = [
            findMoviesByGenre:{ g->
                assertEquals 200, g
                movieList
            }
        ] as MovieService

        controller.movieService = movieService
        controller.params.id = 200

        controller.genre();

        assertResponse 'Genres: Comedy', movieList.collect { [label:it.title, category:'movie', id:it.id] }
    }

	@Test
    void actors(){
        def actorList = [
            new Actor(id:200, firstName:'Abe', middleName:'Bernard', lastName:'Ableman'),
            new Actor(id:100, firstName:'John', middleName:'Quincy', lastName:'Public')
        ]
        mockDomain Actor, actorList

        controller.actors();

        assertResponse 'Actors:', actorList.collect {
            [label:"${it.lastName}, ${it.firstName} ${it.middleName}", category:'actor', id:it.id]
        }
    }

	@Test
    void actor(){
        mockDomain Actor, [
            new Actor(id:200, firstName:'Abe', middleName:'Bernard', lastName:'Ableman'),
            new Actor(id:100, firstName:'John', middleName:'Quincy', lastName:'Public')
        ]

        def movieList = [
            new Movie(id:100, title:'Chicago'),
            new Movie(id:200, title:'Cars'),
            new Movie(id:300, title:'Candaian Bacon'),
        ]
        def movieService = [
            findMoviesByActor:{ a->
                assertEquals 200, a
                movieList
            }
        ] as MovieService

        controller.movieService = movieService
        controller.params.id = 200

        controller.actor();

        assertResponse 'Actors: Ableman, Abe Bernard', movieList.collect { [label:it.title, category:'movie', id:it.id] }
    }

	@Test
    void movie(){
        controller.params.id = 135

        def movieList = [
            new Movie(id:123, title:'Nothing'),
            new Movie(id:135, title:'Some Movie'),
            new Movie(id:246, title:'Foo')
        ]
        mockDomain Movie, movieList

        def resp = controller.movie()

        assertEquals movieList[1], resp.movie
    }

    private def assertResponse( categoryName, expectedItems ){
        assertEquals 'index', controller.modelAndView.viewName
        assertEquals categoryName, controller.modelAndView.model.linkedHashMap.categoryName
        assertEquals expectedItems, controller.modelAndView.model.linkedHashMap.listItems
    }
}
