package com.stehno.mymdb.domain

class Genre {

    String name
    
    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
        name(validator:{ return it != null && (2..10).containsWithinBounds(it.size()) })
    }
}
