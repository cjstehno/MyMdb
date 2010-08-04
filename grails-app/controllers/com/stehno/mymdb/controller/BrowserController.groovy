package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie

class BrowserController {

    def index = { }
	
	def titles = {
	}
	
	def genres = {
	}
	
	def list = {
		def results = Movie.list( [sort:'title', order:'asc'] )
		render(contentType:"text/json") {
			movies = array {
				for(r in results) {
					movie mid:r.id, ti:r.title, yr:r.releaseYear, bx:"${r.storage.name}-${r.storage.index}"
				}
			}	
		}
	
	}
}
