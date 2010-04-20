package com.stehno.mymdb.domain

import org.grails.taggable.*

class Movie implements Taggable {
	
    String title
    String description
    int releaseYear
	Storage storage
	byte[] poster
    
    static hasMany = [genres:Genre, actors:Actor]
	static embedded = ['storage']
	
    Date dateCreated
    Date lastUpdate

    static constraints = {
        title(size:1..100)
        description(size:0..2000)
        releaseYear(range:1930..2020)
        lastUpdate(nullable:true)
        dateCreated(nullable:true)
		storage(nullable:true)
    }
	
	static mapping = {
		cache true
	}	
}
