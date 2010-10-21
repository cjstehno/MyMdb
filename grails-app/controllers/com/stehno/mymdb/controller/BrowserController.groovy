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
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor

class BrowserController {

    def index = { }
	
    def titles = {
        def results = Movie.executeQuery("select distinct(substring(m.title,1,1)) from Movie m")
        render(contentType:"text/json") {
            items = array {
                for(r in results) {
                    item cid:r, lbl:r
                }
            }
        }
    }

    def releaseYears = {
        def results = Movie.executeQuery("select distinct(m.releaseYear) from Movie m order by m.releaseYear asc")
        render(contentType:"text/json") {
            items = array {
                for(r in results) {
                    item cid:r, lbl:r
                }
            }
        }
    }

    def storage = {
        def results = Movie.executeQuery("select distinct(m.storage.name) from Movie m order by m.storage.name asc")
        render(contentType:"text/json") {
            items = array {
                for(r in results) {
                    item cid:r, lbl:r
                }
            }
        }
    }

    def genres = {
        def results = Genre.list( [sort:'name', order:'asc'] )
        render(contentType:"text/json") {
            items = array {
                for(r in results) {
                    item cid:r.id, lbl:r.name
                }
            }
        }
    }

    def actors = {
        def results = Actor.list( [sort:'lastName', order:'asc'] )
        render(contentType:"text/json") {
            items = array {
                for(r in results) {
                    item cid:r.id, lbl:"${r.lastName}, ${r.firstName} ${r.middleName}"
                }
            }
        }
    }

    def list = {
        def categoryId = params.cid
        def results
        switch(params.sid){
            case 'title_store':
                results = Movie.findAll("from Movie as m where substring(m.title,1,1)=? order by m.title", [categoryId])
                break
            case 'genre_store':
                results = Movie.executeQuery("from Movie m where ? in elements(m.genres) order by m.title asc", [categoryId])
                break
            case 'actor_store':
                results = Movie.executeQuery("from Movie m where ? in elements(m.actors) order by m.title asc", [categoryId])
                break
            case 'box_store':
                results = Movie.findAll("from Movie m where m.storage.name=? order by m.title asc", [categoryId])
                break
            case 'year_store':
                results = Movie.findAll("from Movie m where m.releaseYear=? order by m.title asc", [categoryId as Integer])
                break
            default:
                results = Movie.list( [sort:'title', order:'asc'] )
                break
        }

        render(contentType:"text/json") {
            movies = array {
                for(r in results) {
                    movie mid:r.id, ti:r.title, yr:r.releaseYear, bx:"${r.storage.name}-${r.storage.index}"
                }
            }
        }
    }
}
