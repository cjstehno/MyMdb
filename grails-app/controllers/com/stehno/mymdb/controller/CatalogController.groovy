package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

import org.compass.core.engine.SearchEngineQueryParseException
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class CatalogController {

	def searchableService

    def index = {
		[
			storageBoxes:Movie.executeQuery("select distinct m.storage.name from Movie m"),
			releaseYears:Movie.executeQuery("select distinct m.releaseYear from Movie m")
		]
	}
	
	def search = {
        if (!params.q?.trim()) {
            return [:]
        }
        try {
            return [searchResult: searchableService.search(params.q, params)]
        } catch (SearchEngineQueryParseException ex) {
            return [parseException: true]
        }
	}
	
	def title = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		params.sort = 'title'
		params.order = 'desc'
		
		def movies = params.letter ? Movie.findAllByTitleIlike("${params.letter}%",params) : Movie.list(params)
        
        [movieInstanceList:movies, movieInstanceTotal:movies.size()]
	}
	
	def genre = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def movies = null;
		if( params.genre ){
			movies = Movie.executeQuery("from Movie m where :genre in elements(m.genres) order by m.title desc", [genre:params.genre])
		} else {
			movies = Movie.list(params);
		}
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size()]
	}
	
	def actor = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def movies = null;
		if( params.actor ){
			movies = Movie.executeQuery("from Movie m where :actor in elements(m.actors) order by m.title desc", [actor:params.actor])
		} else {
			movies = Movie.list(params);
		}
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size()]
	}	

	def storage = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def movies = null;
		if( params.box ){
			movies = Movie.findAll("from Movie m where m.storage.name = :box order by m.title desc", [box:params.box])
		} else {
			movies = Movie.findAll("from Movie m order by m.storage.name desc, m.title desc")
		}
		
		def boxes = Movie.executeQuery("select distinct m.storage.name from Movie m")
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), storageBoxes:boxes]
	}
	
	def year = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def movies = null;
		if( params.year ){
			movies = Movie.findAll("from Movie m where m.releaseYear = :year order by m.title desc", [year:params.year.toInteger()])
		} else {
			movies = Movie.findAll("from Movie m order by m.releaseYear desc, m.title desc")
		}
		
		def years = Movie.executeQuery("select distinct m.releaseYear from Movie m")
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), releaseYears:years]
	}
	
}
