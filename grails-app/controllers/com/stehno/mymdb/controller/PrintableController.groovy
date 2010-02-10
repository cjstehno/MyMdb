package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

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
	
	def drawer = {
		def moviesInBox = Movie.findAll("from Movie m where m.storage.name=:storage order by m.storage.index asc",[storage:params.box])
		
		def boxSlots = [:]
		moviesInBox.each { m ->
			def idx = m.storage.index
			def coll = boxSlots[idx]
			if( coll == null ){
				coll = []
				boxSlots.put idx, coll
			}
			coll << m
		}
		
		[slots:boxSlots,name:params.box]
	}
}