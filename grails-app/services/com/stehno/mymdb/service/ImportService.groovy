package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Storage

import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource
import java.io.FileReader
import org.w3c.dom.Node

class ImportService {

	boolean transactional = true

	def importData( instream ){
		def doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(instream)

		def catalog = doc.documentElement
		
		def genres = [:]
		def actors = [:]
		def movies = [:]
		
		catalog.childNodes.each { elt ->
			if( elt.nodeType == Node.ELEMENT_NODE ){
				if( 'genres'.equalsIgnoreCase(elt.tagName) ){
					elt.getElementsByTagName('genre').each { e ->
						genres.put e.getAttribute('gid'), new Genre(name:getFirstElementText(e, 'name')).save()
					}
					
				} else if( 'actors'.equalsIgnoreCase(elt.tagName) ){
					elt.getElementsByTagName('actor').each { e ->
						def a = new Actor(
							firstName:getFirstElementText(e,'firstName'), 
							middleName:getFirstElementText(e,'middleName'), 
							lastName:getFirstElementText(e,'lastName')
						).save()
						actors.put e.getAttribute('aid'), a
					}
					
				} else if( 'movies'.equalsIgnoreCase(elt.tagName) ){
					elt.getElementsByTagName('movie').each { e ->
					
						def movieGenres = []
						e.getElementsByTagName('genre').each { gelt ->
							movieGenres << genres[gelt.getAttribute('ref')]
						}
						
						def movieActors = []
						e.getElementsByTagName('actor').each { aelt ->
							movieActors << actors[aelt.getAttribute('ref')]
						}
					   
					    def m = new Movie(
							title:getFirstElementText(e,'title'),
							description:getFirstElementText(e,'description'),
							storage:getFirstElement(e,'storage').with { obj ->
								new Storage(name:obj.getAttribute('name'), index:obj.getAttribute('index'))
							},
							releaseYear:e.getAttribute('releaseYear'),
							poster:getFirstElementText(e,'poster').decodeBase64(),
							genres:movieGenres,
							actors:movieActors
						).save()
					
						movies.put e.getAttribute('mid'), m
					}
				}
			} 
		}
		
		[importedGenres:genres, importedActors:actors, importedMovies:movies]
	}
	
	def getFirstElement(elt, name){
		def nodes = elt.getElementsByTagName(name)
		return nodes.length > 0 ? nodes.item(0) : null
	}

	def getFirstElementText(elt, name){
		getFirstElement(elt,name)?.textContent?.trim()
	}	
}