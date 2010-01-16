package com.stehno.mymdb.domain

class Actor {

    String firstName
    String middleName
    String lastName

    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
		firstName(validator:{ return it != null && (2..25).containsWithinBounds(it.size()) })
		middleName(validator:{ return it == null || (0..25).containsWithinBounds(it.size()) })
        lastName(validator:{ return it != null && (2..25).containsWithinBounds(it.size()) })
    }
}
