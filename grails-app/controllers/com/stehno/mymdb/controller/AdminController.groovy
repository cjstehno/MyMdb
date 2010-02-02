package com.stehno.mymdb.controller
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
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
