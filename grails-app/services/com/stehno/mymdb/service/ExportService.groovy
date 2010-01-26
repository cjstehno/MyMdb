package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Movie

import groovy.xml.MarkupBuilder
import groovy.util.XmlParser

class ExportService {

    boolean transactional = true

    def exportData(writer){
		def xml = new MarkupBuilder(writer)
		
		xml.catalog(){
			actors(){
				export_collection xml, Actor.list(), export_actor
			}
			genres(){
				export_collection xml, Genre.list(), export_genre
			}
			movies(){
				export_collection xml, Movie.list(), export_movie
			}
		}
	}
	
	def importData(stream){
		def catalog = new XmlParser().parse(stream)
		
		def genreCnt = 0
		
		catalog.genres.genre.iterator.each {
			genreCnt++;
		}
		
		def actorCnt = catalog.actors.actor.size() 
		def movieCnt = catalog.movies.movie.size() 
		
		[genres:genreCnt, actors:actorCnt, movies:movieCnt]
	}
	
	private export_collection(builder,coll,exporter){
		coll.each {
			exporter(builder,it)
		}
	}
	
	private export_actor = { builder,actor ->
		builder.actor(aid:actor.id){ 
			builder.firstName(actor.firstName)
			builder.middleName(actor.middleName)
			builder.lastName(actor.lastName)
		}
	}
	
	private export_genre = { builder,genre ->
		builder.genre(gid:genre.id){ 
			builder.name(genre.name)
		}
	}
	
	private export_movie = { builder,movie ->
		builder.movie(mid:movie.id, releaseYear:movie.releaseYear){
			builder.title(movie.title)
			builder.description(movie.description)
			builder.storage(name:movie.storage.name, index:movie.storage.index)
			builder.genres(){
				movie.genres.each { mg ->
					builder.genre(ref:mg.id)
				}
			}
			builder.actors(){
				movie.actors.each { ma -> 
					builder.actor(ref:ma.id)
				}
			}
			builder.poster(movie.poster?.encodeBase64(true))
		}
	}
}
