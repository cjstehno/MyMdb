package com.stehno.mymdb.domain

class Actor {

    String firstName
    String middleName
    String lastName

    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
		firstName(validator:{ it == null || (0..25).containsWithinBounds(it.size()) })
		middleName(validator:{ it == null || (0..25).containsWithinBounds(it.size()) })
        lastName(validator:{ it != null && (1..25).containsWithinBounds(it.size()) })
    }
}
