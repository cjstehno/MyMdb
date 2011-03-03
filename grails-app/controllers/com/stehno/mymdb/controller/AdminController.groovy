/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.mymdb.controller

class AdminController {
	def exportService
	def importService
	
    def index = { }
	
	def exportData = {
		response.setContentType("text/xml")
		response.writer.withWriter {
			exportService.exportData( it )
		}
	}
	
	def importData = {
		def results = [:]
		
		params.data.inputStream.withStream {
			results = importService.importData( it )
		}
		
		[genres:results.importedGenres.values(), actors:results.importedActors.values(), movies:results.importedMovies.values()]
	}
	
	def batchAdd = {
		def results = [:]
		
		results = importService.batchAdd( params.data.inputStream.text )
		
		[movies:results.importedMovies]
	}	
}
