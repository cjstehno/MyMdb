package com.stehno.mymdb.domain

class Genre {

    String name
    
    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
        name(size:1..10)
    }
}
