package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

import org.compass.core.engine.SearchEngineQueryParseException
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class PrintableController {

	def catalog = {
		def mainListing = Movie.list([sort:'title',order:'asc'])
		def byBox = Movie.findAll("from Movie m order by m.storage.name asc, m.storage.index asc")
		def activeGenres = Genre.findAll('from Genre g where size(g.movies) > 0 order by g.name asc')
		def activeActors = Actor.findAll('from Actor a where size(a.movies) > 0 order by a.lastName asc, a.firstName asc, a.middleName asc')
		
		[moviesByTitle:mainListing, moviesByBox:byBox, genres:activeGenres, actors:activeActors]
	}
}