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
class CatalogController {

    def index = {
		def actorList = Actor.list([sort:'lastName',order:'asc'])
		def tabSets = tabbify(actorList, 30)
		
		[
			storageBoxes:Movie.executeQuery("select distinct m.storage.name from Movie m"),
			releaseYears:Movie.executeQuery("select distinct m.releaseYear from Movie m"),
            movieTags:Movie.allTags,
			actorTabs:tabSets
		]
	}
	
	def tabbify( theList, groupCnt ){
		def tabCount = (int)(theList.size() / groupCnt)
		tabCount = (theList.size() % groupCnt) > 0 ? tabCount+1 : tabCount
		
		def tabSets = []
		tabCount.times {
			def end = (it*groupCnt+(groupCnt-1)) >= theList.size() ? theList.size()-1 : (it*groupCnt+(groupCnt-1))
			def sub = theList[it*groupCnt..end]
			tabSets.add sub
		}
		
		return tabSets
	}	
	
	def title = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		params.sort = 'title'
		params.order = 'desc'
		
		def movies = params.letter ? Movie.findAllByTitleIlike("${params.letter}%",params) : Movie.list(params)
        
        [movieInstanceList:movies, movieInstanceTotal:movies.size(), letter:params.letter]
	}

	def genre = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def selectedGenre = ''
		
		def movies = null;
		if( params.id ){
			movies = Movie.executeQuery("from Movie m where :genre in elements(m.genres) order by m.title desc", [genre:params.id])
			selectedGenre = Genre.findById(params.id)
		} else {
			movies = Movie.list(params);
		}
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), theGenre:selectedGenre]
	}
	
	def actor = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def actorList = Actor.list([sort:'lastName',order:'asc'])
		def tabSets = tabbify(actorList, 30)
		
		def selectedActor = ''
		
		def movies = null;
		if( params.id ){
			movies = Movie.executeQuery("from Movie m where :actor in elements(m.actors) order by m.title desc", [actor:params.id])
			selectedActor = Actor.findById(params.id)
		} else {
			movies = Movie.list(params);
		}
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), actorTabs:tabSets, theActor:selectedActor]
	}	

	def storage = {
		params.max = Math.min(params.max ? params.int('max') : 20, 100)
		
		def movies = null;
		if( params.id ){
			movies = Movie.findAll("from Movie m where m.storage.name = :box order by m.title desc", [box:params.id])
		} else {
			movies = Movie.findAll("from Movie m order by m.storage.name desc, m.title desc")
		}
		
		def boxes = Movie.executeQuery("select distinct m.storage.name from Movie m")
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), storageBoxes:boxes, theStorage:params.id]
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
		
		[movieInstanceList:movies, movieInstanceTotal:movies.size(), releaseYears:years, theYear:params.year]
	}

    def tag = {
        params.max = Math.min(params.max ? params.int('max') : 20, 100)

        def movies = null
        if( params.id ){
            movies = Movie.findAllByTag( params.id )
        } else {
            // TODO: group by tag
            movies = Movie.list()
        }

        [movieInstanceList:movies, movieInstanceTotal:movies.size(), theTag:params.id, allTags:Movie.allTags]
    }
}
