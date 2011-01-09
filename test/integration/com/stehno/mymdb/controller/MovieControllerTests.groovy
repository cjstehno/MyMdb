package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.dto.FetchResultsDto
import grails.converters.JSON
import grails.test.GrailsUnitTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class MovieControllerTests extends GrailsUnitTestCase {
	
    def controller

    @Before
    void before() {
        super.setUp()
		
        [ new Genre(name:'Horror'), new Genre(name:'Action') ]*.save(flush:true)
		
        def mov = new Movie(title:'Some Movie', releaseYear:2000, description:'A movie.')
        mov.storage = new Storage(name:'A', index:100)
        mov.poster = null
        mov.save(flush:true)
		
        controller = new MovieController()
    }
	
//    @Test
//    void edit(){
//        def movie = Movie.findByTitle('Some Movie')
//        controller.params.id = movie.id
//
//        controller.edit()
//
//        def jso = parseJsonResponse()
//        assertTrue jso.success
//        assertNull jso.errors
//
//        def data = jso.data
//        assertNotNull data
//        assertEquals movie.id, data.id
//        assertEquals movie.version, data.version
//        assertEquals 'Some Movie', data.title
//        assertEquals 2000, data.releaseYear
//        assertEquals 'A movie.', data.description
//        assertEquals 'A', data['storage.name']
//        assertEquals 100, data['storage.index']
//    }
	
//    @Test
//    void update(){
//        controller.params.id = Movie.findByTitle('Some Movie').id
//        controller.params.title = 'Foovie'
//        controller.params.releaseYear = '2011'
//        controller.params.description = 'Some more text.'
//        controller.params['storage.name'] = 'A'
//        controller.params['storage.index'] = '2'
//        controller.params.poster = ''
//
//        def genre = Genre.findByName('Action')
//        controller.params.genres = genre.id as String
//
//        controller.update()
//
//        def jso = parseJsonResponse()
//        assertTrue jso.success
//        assertNull jso.errors
//
//        assertNull Movie.findByTitle('Some Movie')
//
//        def movie = Movie.findByTitle('Foovie')
//        assertNotNull movie
//        assertEquals 'Foovie', movie.title
//        assertEquals 2011, movie.releaseYear
//        assertEquals 'Some more text.', movie.description
//        assertEquals 'A', movie.storage.name
//        assertEquals 2, movie.storage.index
//
//        assertEquals 1, movie.genres.size()
//        assertEquals 'Action', movie.genres.iterator().next().name
//    }
	
    @Test
    void delete(){
        controller.params.id = Movie.findByTitle('Some Movie').id

        controller.delete()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def movies = Movie.list()
        assertEquals 0, movies.size()
    }

    @Test
    void delete_NotFound(){
        controller.delete()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
        assertEquals 1, jso.errors.size()
        assertEquals 'Movie not found with id null', jso.errors.general

        assertEquals 1, Movie.list().size()
    }

    @Test
    void fetchResults_GET(){
        controller.request.method = 'GET'

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data
        
        assertNotNull controller.session['movie.flow']
    }

    @Test
    void fetchResults_POST(){
        controller.session['movie.flow'] = [:]
        
        controller.request.method = 'POST'
        controller.params.title = 'Some title'

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def flow = controller.session['movie.flow']
        assertNotNull flow
        assertNotNull flow.fetchResults
        assertEquals 'Some title', flow.fetchResults.title
    }

    @Test
    void fetchResults_POST_Errors(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'POST'
        controller.params.title = ''

        controller.fetchResults()

        def jso = parseJsonResponse()
        assertFalse jso.success
        assertNotNull jso.errors
    }

    @Test
    void details_GET(){
        controller.session['movie.flow'] = [:]
        controller.session['movie.flow'].fetchResults = new FetchResultsDto( title:'A Title' )
        
        controller.request.method = 'GET'

        controller.details()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']
    }

    @Test
    void details_POST(){
        controller.session['movie.flow'] = [:]
        controller.session['movie.flow'].fetchResults = new FetchResultsDto( title:'A Title' )
        
        controller.request.method = 'POST'
        controller.params.title = 'Some title'
        controller.params.releaseYear = '2005'
        controller.params.storageName = 'A'
        controller.params.storageIndex = '1'
        controller.params.description = 'blah blah blah...'

        controller.details()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def flow = controller.session['movie.flow']
        assertNotNull flow
        assertNotNull flow.details
        assertEquals 'Some title', flow.details.title
        assertEquals 2005, flow.details.releaseYear
        assertEquals 'A', flow.details.storageName
        assertEquals 1, flow.details.storageIndex
        assertEquals 'blah blah blah...', flow.details.description
    }

    @Test
    void poster_GET(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'GET'

        controller.poster()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']
        assertNotNull controller.session['movie.flow'].poster
    }

    @Test
    void poster_POST_none(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'POST'
        controller.params.posterType = 'none'

        controller.poster()

        def jso = parseJsonResponse()
        assertTrue jso.success

        def flow = controller.session['movie.flow']
        assertNotNull flow
        assertNotNull flow.poster
        assertNull flow.file
    }

//    @Test
//    void poster_POST_url(){
//        controller.session['movie.flow'] = [:]
//
//        controller.request.method = 'POST'
//        controller.params.posterType = 'url'
//        controller.params.url = ''
//
//        controller.poster()
//
//        def jso = parseJsonResponse()
//        assertTrue jso.success
//
//        def flow = controller.session['movie.flow']
//        assertNotNull flow
//        assertNotNull flow.poster
//        assertNull flow.file
//    }

    @Test
    void genres_GET(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'GET'
        controller.genre()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']
        assertNotNull controller.session['movie.flow'].genre
    }

    @Test
    void genres_POST(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'GET'
        controller.params.genres = ['1','3']

        controller.genre()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']

        def genre = controller.session['movie.flow'].genre
        assertNotNull genre
        assertEquals( [1,3], genre.genres )
    }

    @Test
    void actors_GET(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'GET'
        controller.actor()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']
        assertNotNull controller.session['movie.flow'].actor
    }

    @Test
    void actors_POST(){
        controller.session['movie.flow'] = [:]

        controller.request.method = 'GET'
        controller.params.actors = ['1','3']

        controller.actor()

        def jso = parseJsonResponse()
        assertTrue jso.success
        assertNotNull jso.data

        assertNotNull controller.session['movie.flow']

        def actor = controller.session['movie.flow'].actor
        assertNotNull actor
        assertEquals( [1,3], actor.actors )
    }

    @Test
    void summary_GET(){
        def details = [
            title:'Some Title',
            releaseYear:2010,
            storageName:'x',
            storageIndex:23,
            description:'This is a description'
        ]

        controller.session['movie.flow'] = [
            details:details,
            genre:[genres:null],
            actor:[actors:null]
        ]

        controller.request.method = 'GET'
        def resp = controller.summary()

        assertNotNull resp
        assertEquals 'Some Title', resp.title
        assertEquals 2010, resp.releaseYear
        assertEquals 'x-23', resp.storage
        assertEquals 'This is a description', resp.description
    }

    @Test
    void extractExistingOrUse_Use(){
        def flow = [:]
        def dto = [name:'SomeBean']
        controller.extractExistingOrUse( flow, 'foo', dto)

        assertEquals dto, flow.foo
    }

    @Test
    void extractExistingOrUse_Existing(){
        def flow = [foo:'Bar']
        def dto = [name:'SomeBean']
        controller.extractExistingOrUse( flow, 'foo', dto)

        assertEquals 'Bar', flow.foo
    }

    @After
    void after(){
        super.tearDown();
    }
	
    private def parseJsonResponse(){
        JSON.parse( new StringReader(controller.response.contentAsString) )
    }
}
