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

import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class PrintableController {

	def titlesection = {
		def movieList = Movie.list([sort:'title',order:'asc'])
		[movies:movieList]
	}
	
	def genresection = {
		def activeGenres = Genre.findAll('from Genre g where size(g.movies) > 0 order by g.name asc')
		[genres:activeGenres]
	}
	
	def actorsection = {
		def activeActors = Actor.findAll('from Actor a where size(a.movies) > 0 order by a.lastName asc, a.firstName asc, a.middleName asc')
		[actors:activeActors]
	}
	
	def storagesection = {
		def byBox = Movie.findAll("from Movie m order by m.storage.name asc, m.storage.index asc")
		[storage:byBox]
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