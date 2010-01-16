package com.stehno.mymdb.domain

class Actor {

    String firstName
    String middleName
    String lastName

    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
        firstName(size:0..25)
		middleName(validator:{ return(it == null || (it.size() >= 0 && it.size() < 25) ) })
        lastName(size:1..25)
    }
}
