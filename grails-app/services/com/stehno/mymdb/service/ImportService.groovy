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
	
	def batchAdd( text ){
		def movies = []
		def currentMovie = null
		
		def handlers = [
			't':{ line ->
				currentMovie = new Movie(title:lineContent(line))
			},
			
			'y':{ line ->
				currentMovie.releaseYear = lineContent(line).toInteger()
			},
			
			'g':{ line ->
				currentMovie.addToGenres( genreForName( lineContent(line) ) )
			},
			
			'a':{ line ->
				currentMovie.addToActors( actorForName( lineContent(line) ) )
			},
			
			'd':{ line ->
				currentMovie.description = lineContent(line)
			},
			
			'p':{ line ->
				currentMovie.poster = importPoster(lineContent(line))
			},
			
			's':{ line ->
				def sparts = lineContent(line).split(',')
				currentMovie.storage = new Storage(name:sparts[0], index:sparts[1].toInteger())
			},
			
			'=':{ line ->
				movies << currentMovie.save()
			}
		]			
		
        text.eachLine { line ->
            if( line != null && line.size() > 0 ){
                def handler = handlers[line[0]]
                if(handler) handler(line)
            }
        }
		
		[importedMovies:movies]
    }
	
	def actorForName( fullname ){
		def actor = null
		def actname = fullname.split(' ')
		if( actname.size() == 1 ){
			def act = Actor.findByLastName( actname[0] )
			if( !act ){
				act = new Actor(firstName:'', middleName:'', lastName:actname[0]).save()
			}
			actor = act
			
		} else if( actname.size() == 2 ){
			def act = Actor.findByFirstNameAndLastName(actname[0],actname[1])
			if( !act ){
				act = new Actor(firstName:actname[0], middleName:'', lastName:actname[1]).save()
			}
			actor = act
			
		} else if( actname.size() == 3 ){
			def act = Actor.findWhere(firstName:actname[0],middleName:actname[1],lastName:actname[2])
			if( !act ){
				act = new Actor(firstName:actname[0], middleName:actname[1], lastName:actname[2]).save()
			}
			actor = act			
		}	
		actor
	}
	
	def genreForName( name ){
		def gen = Genre.findByName( name )
		if( !gen ){
			gen = new Genre(name:name).save()
		}
		gen
	}

    def lineContent( line ){
        line.substring(line.indexOf(':')+1).trim()
    }
	
	def importPoster( url ){
		def data = []
		new URL(url).withInputStream {
			it.each { b ->
				data << b
			}
		}
		data.toArray(new byte[0])
	}

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